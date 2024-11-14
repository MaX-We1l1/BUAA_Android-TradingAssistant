package com.example.myapplication.square;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.bumptech.glide.Glide;
import com.example.myapplication.InputNumberView;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Tools;
import com.example.myapplication.chat.ChatMsgView;
import com.example.myapplication.database.Commodity;
import com.example.myapplication.database.DBFunction;
import com.example.myapplication.database.Hobby;
import com.example.myapplication.database.User;
import com.example.myapplication.profile.cart.CartActivity;
import com.example.myapplication.database.CartItem;
import com.example.myapplication.profile.cart.CartManager;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class CommodityDetailActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private TextView commodityName; // 商品名称
    private TextView commodityPrice; // 商品价格
    private TextView commodityDescription; // 商品描述
    private ImageView commodityImage; // 商品图片
    private TextView commoditySeller;
    private Button chatButton; //想要，跳转到聊天界面
    private Button editButton;
    private Commodity commodity;
    private Button saveButton;
    private Button addCartButton,addHobbyButton;
    private ImageButton cartButton;
    private CartManager cartManager = CartManager.getInstance();
    private InputNumberView quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_detail);

        commodityName = findViewById(R.id.commodity_name);
        commodityPrice = findViewById(R.id.commodity_price);
        commodityDescription = findViewById(R.id.commodity_description);
        commodityImage = findViewById(R.id.commodity_image);
        commoditySeller = findViewById(R.id.commodity_seller); // 绑定卖家信息
        chatButton = findViewById(R.id.button_want); // 绑定“想要”按钮
        editButton = findViewById(R.id.button_edit_commodity); //
        addCartButton = findViewById(R.id.button_add_to_cart);
        saveButton = findViewById(R.id.button_save); //
        addHobbyButton = findViewById(R.id.button_add_to_hobby);
        quantity = findViewById(R.id.commodity_num);

        // 重定向到自己的购物车
        cartButton = findViewById(R.id.button_cart);
        cartButton.setOnClickListener(v-> {
            Intent intent = new Intent(CommodityDetailActivity.this, CartActivity.class);
            startActivity(intent);
        });

        //从 Intent 中获取商品 ID
        Intent intent = getIntent();
        long commodityId = intent.getLongExtra("commodity_id", -1);
        commodity = LitePal.find(Commodity.class, commodityId);

        // 确保 ID 有效并执行后续逻辑
        if (commodityId != -1) {
            // 根据 ID 加载商品详情
            loadCommodityDetails(commodityId);
        } else {
            Toast.makeText(this, "商品 ID 无效", Toast.LENGTH_SHORT).show();
        }

        //检测当前是否是卖家，是就显示修改按钮,不显示聊天按钮
        if (isCurrentUserSeller(commodity)) {
            editButton.setVisibility(View.VISIBLE);
            chatButton.setVisibility(View.GONE);
            addCartButton.setVisibility(View.GONE);
            quantity.setVisibility(View.GONE);
            editButton.setOnClickListener(v -> enableEditing());
        }
        //保存按钮
        saveButton.setOnClickListener(v -> saveChanges());

        // 返回按钮
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    protected void onResume() {
        super.onResume();
        // 在这里执行你希望在Activity重新回到前台后执行的逻辑
        Intent intent = getIntent();
        long commodityId = intent.getLongExtra("commodity_id", -1);
        loadCommodityDetails(commodityId);
    }

    @SuppressLint("SetTextI18n")
    private void loadCommodityDetails(long commodityId) {
        // 使用商品 ID 获取商品详情
        Commodity commodity = LitePal.find(Commodity.class, commodityId);
        // 确保商品不为空后设置视图
        if (commodity != null) {
            commodityName.setText(commodity.getCommodityName());
            commodityPrice.setText("¥ " + commodity.getPrice());
            commodityDescription.setText(commodity.getDescription());
            commoditySeller.setText("卖家: " + commodity.getSellerName()); // 显示卖家信息

            // 加载商品图片
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        REQUEST_CODE);
//            } else {
//                loadImage();
//            }
            String imageBase64 = commodity.getImageUrl();
            if (imageBase64 != null || !imageBase64.isEmpty()) {
                byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                commodityImage.setImageBitmap(decodedByte);
            } else {
                Glide.with(this)
                    .load(R.drawable.logo)
                    .placeholder(R.drawable.wangpeng) // 占位图
                    .error(R.drawable.logo) // 错误图
                    .into(commodityImage);
            }


            chatButton.setOnClickListener(v -> {
                long user2 = DBFunction.findUserByName(commodity.getSellerName()).getId();
                Intent intent = new Intent(CommodityDetailActivity.this, ChatMsgView.class);
                intent.putExtra("commodity_id", commodityId);
                intent.putExtra("chat_id", user2); // 传递聊天 ID
                intent.putExtra("chat_name", commodity.getSellerName());
                startActivity(intent);
            });

            // 加入商品数量
            quantity.setMaxNum(50);
            quantity.setOnAmountChangeListener(new InputNumberView.OnAmountChangeListener() {
                @Override
                public void onAmountChange(View view, int amount) {
                    if (amount > 50) {
                        Toast.makeText(getApplicationContext(), "未找到该商品", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // 已添加
            TextView already = findViewById(R.id.commodity_already_num);
            already.setText("已添加：" + cartManager.getQuantity(commodity.getId()));

            // 加入购物车
            addCartButton.setOnClickListener(v -> {
                CartItem cartItem = new CartItem(commodity.getCommodityName()
                        , commodity.getId()
                        , commodity.getPrice()
                        , quantity.getCurrentNum());
                cartManager.addItemToCart(cartItem);
                Tools.toastMessageShort(CommodityDetailActivity.this, "加入购物车成功!");
                already.setText("已添加：" + cartManager.getQuantity(cartItem));
            });



            addHobbyButton.setOnClickListener(v -> {
                Hobby hobby = new Hobby();
                hobby.setCommodityId(commodity.getId());
                hobby.setTitle(commodity.getCommodityName());
                hobby.setId(commodity.getId());
                hobby.setUserName(MainActivity.getCurrentUsername());
                hobby.save();
                User user1 = DBFunction.findUserByName(MainActivity.getCurrentUsername());
                user1.addHobby(hobby);
                user1.save();
                String s = "no";
                if (!user1.getHobbies().isEmpty()) {
                    s = "yes";
                    //Tools.toastMessageShort(CommodityDetailActivity.this, DBFunction.findUserByName(MainActivity.getCurrentUsername()).getHobbies().get(0).getTitle());
                }
                Tools.toastMessageShort(CommodityDetailActivity.this, s);
            });
        } else {
            Toast.makeText(this, "未找到该商品", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isCurrentUserSeller(Commodity commodity) {
        // 检查当前用户是否为卖家
        return Objects.equals(commodity.getSellerName(), MainActivity.getCurrentUsername()); // 替换为实际逻辑
    }

    private void enableEditing() {
        // 只有卖家能编辑
        if (isCurrentUserSeller(commodity)) {
            commodityPrice.setEnabled(true);
            commodityDescription.setEnabled(true);
            saveButton.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "无法编辑商品信息", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveChanges() {
        String description = commodityDescription.getText().toString();
        String priceString = commodityPrice.getText().toString();
        float price;

        // 检查价格输入是否为空
        if (priceString.isEmpty()) {
            Toast.makeText(this, "价格不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 移除非数字和小数点字符
        priceString = priceString.replaceAll("[^\\d.]", "");

        try {
            price = Float.parseFloat(priceString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效的价格", Toast.LENGTH_SHORT).show();
            return;
        }

        // 更新商品对象
        commodity.setDescription(description);
        commodity.setPrice(price);
        commodity.save(); // 保存更新到数据库

        Toast.makeText(this, "商品信息已更新", Toast.LENGTH_SHORT).show();

        // 禁用编辑模式
        disableEditing();

        //关闭当前页面
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updated", true); // 或其他需要返回的数据
        setResult(RESULT_OK, resultIntent);
        finish(); // 关闭当前活动
    }


    private void disableEditing() {
        commodityPrice.setEnabled(false);
        commodityDescription.setEnabled(false);
        saveButton.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限已授予，可以执行相应的操作
                loadImage();
            } else {
                // 权限被拒绝，处理相应的逻辑（例如提示用户）
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // 用户已勾选"不再询问"，你可以引导用户去设置界面手动打开权限
                    Toast.makeText(this, "Permission permanently denied, please enable it in settings", Toast.LENGTH_LONG).show();
                } else {
                    // 用户拒绝权限，显示解释并请求权限
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE);
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void loadImage() {
        Uri imgUri = null;
        if (commodity.getImageUrl() != null) {
            imgUri = Uri.parse(commodity.getImageUrl());
        }
        // InputStream inputStream = getContentResolver().openInputStream(commodity.getImageUrl());
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//            }
        // getApplicationContext().getContentResolver().takePersistableUriPermission(imgUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

//            Glide.with(this)
//                    .load(imgUri == null ? R.drawable.logo: imgUri)
//                    .placeholder(R.drawable.wangpeng) // 占位图
//                    .error(R.drawable.logo) // 错误图
//                    .into(commodityImage);
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(imgUri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            // 使用文件路径加载图片
            Glide.with(this)
                    .load(filePath)
                    .placeholder(R.drawable.wangpeng) // 占位图
                    .error(R.drawable.logo) // 错误图
                    .into(commodityImage);
        }

        Log.w("from addComodity", commodity.getImageUrl());
    }
}
