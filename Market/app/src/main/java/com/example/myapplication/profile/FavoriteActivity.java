package com.example.myapplication.profile;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.profile.FavoriteItem;
import com.example.myapplication.profile.FavoriteAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private List<FavoriteItem> favoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite); // 绑定布局文件

        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.recycler_favorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // 设置布局管理器为线性布局

        // 模拟数据（实际项目中，从数据库或API获取数据） TODO
        favoriteList = new ArrayList<>();
        favoriteList.add(new FavoriteItem(R.drawable.ic_favorite_placeholder, "收藏标题1", "这是收藏的描述1", "2024-10-17"));
        favoriteList.add(new FavoriteItem(R.drawable.ic_favorite_placeholder, "收藏标题2", "这是收藏的描述2", "2024-10-16"));
        favoriteList.add(new FavoriteItem(R.drawable.ic_favorite_placeholder, "收藏标题3", "这是收藏的描述3", "2024-10-15"));

        // 初始化适配器并绑定数据
        favoriteAdapter = new FavoriteAdapter(this, favoriteList);
        recyclerView.setAdapter(favoriteAdapter); // 设置适配器
    }
}
