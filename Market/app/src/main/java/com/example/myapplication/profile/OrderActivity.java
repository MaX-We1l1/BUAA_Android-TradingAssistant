package com.example.myapplication.profile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recyclerViewOrders = findViewById(R.id.recyclerView_orders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        // 初始化订单数据
        orderList = new ArrayList<>();
        orderList.add(new Order("订单 1", "待发货", 123.00, R.drawable.ic_order_placeholder,1));
        orderList.add(new Order("订单 2", "已完成", 456.00, R.drawable.ic_order_placeholder,1));
        orderList.add(new Order("订单 3", "已取消", 789.00, R.drawable.ic_order_placeholder,1));
        orderList.add(new Order("订单 4", "待发货", 234.00, R.drawable.ic_order_placeholder,1));

        // 设置适配器
        orderAdapter = new OrderAdapter(this, orderList);
        recyclerViewOrders.setAdapter(orderAdapter);
    }
}
