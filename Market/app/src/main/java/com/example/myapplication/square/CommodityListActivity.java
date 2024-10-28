package com.example.myapplication.square;

import android.os.Bundle;
import android.util.Log;

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
        //TODO 每次都会多添加两个,添加得放在其他地方
//        commodityList = new ArrayList<>();
//        DBFunction.addCommodity("商品1", "卖家1","2024-10-21",
//                Type.BOOK, 100.0F, "学生自用，几乎全新，可小刀，屠龙刀勿扰");
//        Commodity commodity1 = LitePal.find(Commodity.class, 1);
//        commodityList.add(commodity1);
//
//        DBFunction.addCommodity("商品2", "卖家2","2024-10-21",
//                Type.BAG, 1000.0F, "重生之我在bh卖包包，十年前我被仇家算害，家财散尽，如今重生归来，当我凑够十万就会前去复仇，欢迎买我的包包资助我完成我的复仇大计");
//        Commodity commodity2 = LitePal.find(Commodity.class, 2);
//        commodityList.add(commodity2);

        // 添加更多商品...
//        int count = LitePal.count(Commodity.class);
//        Log.d("CommodityCount", "商品数量: " + count);
        List<Commodity> allCommodities = LitePal.findAll(Commodity.class);
        commodityList.addAll(allCommodities);

        commodityAdapter = new CommodityAdapter(this, commodityList);
        recyclerView.setAdapter(commodityAdapter);
    }
}

