package com.example.myapplication.square;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.chat.ChatListActivity;
import com.example.myapplication.chat.ChatMsgView;
import com.example.myapplication.database.Commodity;
import com.example.myapplication.database.DBFunction;
import com.example.myapplication.profile.ProfileActivity;
import com.example.myapplication.profile.cart.CartActivity;

import org.litepal.LitePal;

import java.util.Objects;

public class CommodityDetailActivity extends AppCompatActivity {
    private TextView commodityName; // 商品名称
    private TextView commodityPrice; // 商品价格
    private TextView commodityDescription; // 商品描述
    private ImageView commodityImage; // 商品图片
    private TextView commoditySeller;
    private Button chatButton; //想要，跳转到聊天界面
    private Button editButton;
    private Commodity commodity;
    private Button saveButton;
    private ImageButton cartButton;

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
        saveButton = findViewById(R.id.button_save); //

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
            cartButton.setVisibility(View.GONE);
            editButton.setOnClickListener(v -> enableEditing());
        }
        //保存按钮
        saveButton.setOnClickListener(v -> saveChanges());
    }

    private void loadCommodityDetails(long commodityId) {
        // 使用商品 ID 获取商品详情
        Commodity commodity = LitePal.find(Commodity.class, commodityId);
        // 确保商品不为空后设置视图
        if (commodity != null) {
            commodityName.setText(commodity.getCommodityName());
            commodityPrice.setText("¥ " + commodity.getPrice());
            commodityDescription.setText(commodity.getDescription());
            commoditySeller.setText("卖家: " + commodity.getSellerName()); // 显示卖家信息
            // 使用 Glide 加载商品图片
//            Glide.with(this)
//                    .load(commodity.getImageUrl())
//                    .placeholder(R.drawable.placeholder) // 占位图
//                    .error(R.drawable.error_image) // 错误图
//                    .into(commodityImage);
            chatButton.setOnClickListener(v -> {
                long user2 = DBFunction.findUserByName(commodity.getSellerName()).getId();
                Intent intent = new Intent(CommodityDetailActivity.this, ChatMsgView.class);
                intent.putExtra("commodity_id", commodityId);
                intent.putExtra("chat_id", user2); // 传递聊天 ID
                intent.putExtra("chat_name", commodity.getSellerName());
                startActivity(intent);
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

}
