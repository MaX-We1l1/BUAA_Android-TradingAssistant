package com.example.myapplication.square;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.database.Commodity;
import com.example.myapplication.database.DBFunction;
import com.example.myapplication.database.OrderTable;
import com.example.myapplication.database.User;
import com.example.myapplication.profile.address.Address;

import org.litepal.LitePal;

import java.util.ArrayList;

public class BuyCommodityActivity extends AppCompatActivity {

    private TextView commodityName;
    private TextView commodityPrice;
    private Spinner addressSpinner;
    private Button incrementButton;
    private Button decrementButton;
    private TextView quantityText;
    private Button buyButton;
    private TextView exitButton;

    private Commodity commodity;
    private int quantity = 1; // 默认数量为 1
    private String chosenAddress; // 选定的地址
    private ArrayList<String> addressList; // 地址列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_commodity);

        // 初始化视图
        commodityName = findViewById(R.id.commodity_name);
        commodityPrice = findViewById(R.id.commodity_price);
        addressSpinner = findViewById(R.id.address_spinner);
        incrementButton = findViewById(R.id.increment_button);
        decrementButton = findViewById(R.id.decrement_button);
        quantityText = findViewById(R.id.quantity_text);
        buyButton = findViewById(R.id.buy_button);
        exitButton = findViewById(R.id.exit_text);

        exitButton.setOnClickListener(v -> {
            finish();
        });

        // 从 Intent 中获取商品 ID
        Intent intent = getIntent();
        long commodityId = intent.getLongExtra("commodity_id", -1);
        commodity = LitePal.find(Commodity.class, commodityId);

        if (commodity != null) {
            // 设置商品信息
            commodityName.setText(commodity.getCommodityName());
            commodityPrice.setText(String.format("¥%.2f", commodity.getPrice()));
        } else {
            Toast.makeText(this, "商品信息加载失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 初始化地址列表
        addressList = DBFunction.getAddress(MainActivity.getCurrentUsername());
        if (addressList.isEmpty()) {
            addressList.add("请添加地址");
        }

        ArrayList<String> strings = new ArrayList<>();
        for (String address : addressList) {
            Address address1 = Address.parseAddressFromString(address);
            strings.add(address1.getName() + " " + address1.getDetail() + " " + address1.getPhoneNumber());
        }

        // 设置地址选择器
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addressSpinner.setAdapter(adapter);

        addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                chosenAddress = addressList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                chosenAddress = null;
            }
        });

        // 数量增加按钮
        incrementButton.setOnClickListener(v -> {
            quantity++;
            updateQuantityDisplay();
        });

        // 数量减少按钮
        decrementButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityDisplay();
            }
        });

        // 购买逻辑
        buyButton.setOnClickListener(v -> {
            if (chosenAddress == null || chosenAddress.equals("请添加地址")) {
                Toast.makeText(this, "请选择有效的收货地址", Toast.LENGTH_SHORT).show();
                return;
            }

            User buyer = DBFunction.findUserByName(MainActivity.getCurrentUsername());
            if (buyer.getMoney() >= commodity.getPrice() * quantity) {
                buyer.buy(commodity.getPrice() * quantity);
                User seller = DBFunction.findUserByName(commodity.getSellerName());
                seller.sell(commodity.getPrice() * quantity);
                commodity.setBuyerName(MainActivity.getCurrentUsername());
                buyer.save();
                seller.save();
                OrderTable order = DBFunction.addBuyOrder(commodity.getId(), buyer.getId(), commodity.getCommodityName(),
                        commodity.getPrice(), commodity.getImageUrl());
                DBFunction.sendNotification2Seller(commodity.getCommodityName(), order.getId(), seller.getId(), buyer.getId(), "待发货");
                Toast.makeText(this, "购买成功！", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "余额不足", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 更新显示的数量
    private void updateQuantityDisplay() {
        quantityText.setText(String.valueOf(quantity));
    }
}
