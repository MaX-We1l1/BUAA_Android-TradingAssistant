package com.example.myapplication.square;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.chat.ChatListActivity;
import com.example.myapplication.database.Commodity;

import org.litepal.LitePal;

public class CommodityDetailActivity extends AppCompatActivity {
    private TextView commodityName; // 商品名称
    private TextView commodityPrice; // 商品价格
    private TextView commodityDescription; // 商品描述
    private ImageView commodityImage; // 商品图片
    private TextView commoditySeller;
    private Button wantButton; //想要，跳转到聊天界面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_detail);

        commodityName = findViewById(R.id.commodity_name);
        commodityPrice = findViewById(R.id.commodity_price);
        commodityDescription = findViewById(R.id.commodity_description);
        commodityImage = findViewById(R.id.commodity_image);
        commoditySeller = findViewById(R.id.commodity_seller); // 绑定卖家信息
        wantButton = findViewById(R.id.button_want); // 绑定“想要”按钮

        //从 Intent 中获取商品 ID
        Intent intent = getIntent();
        long commodityId = intent.getLongExtra("commodity_id", -1);

        // 确保 ID 有效并执行后续逻辑
        if (commodityId != -1) {
            // 根据 ID 加载商品详情
            loadCommodityDetails(commodityId);
        } else {
            Toast.makeText(this, "商品 ID 无效", Toast.LENGTH_SHORT).show();
        }
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
            wantButton.setOnClickListener(v -> {
                Intent intent = new Intent(CommodityDetailActivity.this, ChatListActivity.class);
                intent.putExtra("commodity_id", commodityId);
                startActivity(intent);
            });
        } else {
            Toast.makeText(this, "未找到该商品", Toast.LENGTH_SHORT).show();
        }
    }
}
