package com.example.myapplication.profile;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AddressAdapter addressAdapter;
    private List<Address> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        recyclerView = findViewById(R.id.recycler_view_address);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 初始化地址列表
        addressList = new ArrayList<>();
        // TODO: 从数据库或API加载地址数据

        // 设置适配器
        addressAdapter = new AddressAdapter(addressList, this);
        recyclerView.setAdapter(addressAdapter);

        // 添加新地址按钮
        Button addAddressButton = findViewById(R.id.button_add_address);
        addAddressButton.setOnClickListener(v -> {
            // TODO: 打开添加地址的Activity或者弹出一个Dialog
        });
    }
}
