package com.example.myapplication.square;

import static java.util.Locale.filter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.chat.ChatListActivity;
import com.example.myapplication.commodity.AddCommodityActivity;
import com.example.myapplication.database.Commodity;
import com.example.myapplication.database.DBFunction;
import com.example.myapplication.database.Type;
import com.example.myapplication.home.HomepageActivity;
import com.example.myapplication.profile.ProfileActivity;


import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class CommodityListActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1; // 定义请求码
    private RecyclerView recyclerView;
    private CommodityAdapter commodityAdapter;
    private List<Commodity> commodityList;
    private List<Commodity> commodityListClone;//用于克隆一份商品

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 初始化商品列表
        commodityList = LitePal.findAll(Commodity.class); // 从数据库加载商品
        commodityAdapter = new CommodityAdapter(this, commodityList, REQUEST_CODE); // 创建适配器

        commodityListClone = new ArrayList<>();
        commodityListClone = LitePal.findAll(Commodity.class);

        recyclerView.setAdapter(commodityAdapter); // 设置适配器

        // 设置搜索框
        EditText searchEditText = findViewById(R.id.editTextSearch);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.d("Search", "Text changed: " + s.toString());
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Button messagesButton = findViewById(R.id.button_messages);
        messagesButton.setOnClickListener(v -> {
            Intent intent = new Intent(CommodityListActivity.this, ChatListActivity.class);
            startActivity(intent);
        });

        Button userButton = findViewById(R.id.button_profile);
        userButton.setOnClickListener(v -> {
            Intent intent = new Intent(CommodityListActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        Button sellButton = findViewById(R.id.button_sell);
        sellButton.setOnClickListener(v -> {
            Intent intent = new Intent(CommodityListActivity.this, AddCommodityActivity.class);
            startActivity(intent);
        });

        Button squareButton = findViewById(R.id.button_square);
        squareButton.setOnClickListener(v -> {
            Intent intent = new Intent(CommodityListActivity.this, CommodityListActivity.class);
            startActivity(intent);
        });

        Button homeButton = findViewById(R.id.button_home);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(CommodityListActivity.this, HomepageActivity.class);
            startActivity(intent);
        });
    }

    // 过滤商品列表
    private void filter(String text) {
        List<Commodity> filteredList = new ArrayList<>();
        if (text.isEmpty()) {
            filteredList.addAll(commodityListClone); // 如果输入为空，返回全部商品
        } else {
            for (Commodity item : commodityListClone) {
                if (item.getCommodityName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        //Log.d("Filter", "Filtered List Size1: " + commodityList.size());
        //Log.d("Filter", "Filtered List Size: " + filteredList.size());
        commodityAdapter.updateList(filteredList); // 更新适配器
        //Log.d("Filter", "Filtered List Size2: " + commodityList.size());
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

