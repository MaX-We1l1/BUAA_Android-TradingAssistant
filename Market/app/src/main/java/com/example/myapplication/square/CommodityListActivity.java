package com.example.myapplication.square;

import static java.util.Locale.filter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.Commodity;
import com.example.myapplication.database.DBFunction;
import com.example.myapplication.database.Type;


import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class CommodityListActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1; // 定义请求码
    private RecyclerView recyclerView;
    private CommodityAdapter commodityAdapter;
    private List<Commodity> commodityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 初始化商品列表
        commodityList = LitePal.findAll(Commodity.class); // 从数据库加载商品
        commodityAdapter = new CommodityAdapter(this, commodityList, REQUEST_CODE); // 创建适配器
        recyclerView.setAdapter(commodityAdapter); // 设置适配器

        // 设置搜索框
        EditText searchEditText = findViewById(R.id.editTextSearch);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // 过滤商品列表
    private void filter(String text) {
        List<Commodity> filteredList = new ArrayList<>();
        for (Commodity item : commodityList) {
            if (item.getCommodityName().toLowerCase().contains(text.toLowerCase())) { // 根据商品名称过滤
                filteredList.add(item);
            }
        }
        commodityAdapter.updateList(filteredList); // 更新适配器
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // 重新加载商品列表
            loadCommodityList(); // 调用加载商品列表的方法
        }
    }

    // 加载商品的方法
    private void loadCommodityList() {
        commodityList.clear();
        commodityList = LitePal.findAll(Commodity.class); // 从数据库加载商品
        commodityAdapter.updateList(commodityList);
    }

}

