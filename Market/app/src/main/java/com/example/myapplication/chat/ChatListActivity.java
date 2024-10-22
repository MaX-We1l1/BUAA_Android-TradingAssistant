package com.example.myapplication.chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.example.myapplication.R;
import com.example.myapplication.profile.ProfileActivity;
import com.example.myapplication.square.CommodityListActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatListActivity extends AppCompatActivity {

    private ListView chatListView;
    private FloatingSearchView mSearchView;
//    private EditText searchBox;
//    private ImageView searchButton, menuButton;
    private ChatAdapter chatAdapter; // 你的聊天适配器
    private ArrayList<ChatItem> chatList; // 聊天列表数据



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);

        chatListView = findViewById(R.id.chat_list_view);
        mSearchView = findViewById(R.id.floating_search_view);
        //searchBox = findViewById(R.id.search_box);
        // TODO: 搜索功能以及菜单功能仍未完善
//        searchButton = findViewById(R.id.search_button);
//        menuButton = findViewById(R.id.menu_button);

        // 初始化聊天列表
        chatList = new ArrayList<>();
        // TODO: 添加聊天数据到 chatList
        chatList.add(new ChatItem("1", "张三", "你好，有空吗？"));
        chatList.add(new ChatItem("2", "李四", "关于交易的事..."));


        chatAdapter = new ChatAdapter(this, chatList);
        chatListView.setAdapter(chatAdapter);

        // 点击事件
//        searchButton.setOnClickListener(v -> {
//            // TODO: 实现搜索功能
//            String query = searchBox.getText().toString();
//            // 示例：过滤聊天列表
//            Toast.makeText(this, "搜索功能待实现: " + query, Toast.LENGTH_SHORT).show();
//        });

//        menuButton.setOnClickListener(v -> {
//            // TODO: 实现菜单功能
//            Toast.makeText(this, "菜单功能待实现", Toast.LENGTH_SHORT).show();
//        });

        // 监听搜索框的查询变化
        mSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {
            // 提供建议项
            provideSuggestions(newQuery);
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSearchAction(String currentQuery) {
                // 执行搜索操作
                saveSearchHistory(currentQuery);
                performSearch(currentQuery);
            }

            @Override
            public void onSuggestionClicked(SearchSuggestion suggestion) {
                // 处理建议项点击事件
                mSearchView.setSearchText(suggestion.getBody());
            }
        });

        // 点击聊天列表项跳转到具体聊天界面
        chatListView.setOnItemClickListener((parent, view, position, id) -> {
            // TODO: 跳转到聊天界面
            Intent intent = new Intent(ChatListActivity.this, ChatDetailActivity.class);
            intent.putExtra("chat_id", chatList.get(position).getId()); // 传递聊天 ID
            startActivity(intent);
        });

        // 这里可以设置其他初始化逻辑，比如加载数据等
        Button messagesButton = findViewById(R.id.button_messages);
        messagesButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChatListActivity.this, ChatListActivity.class);
            startActivity(intent);
        });

        Button userButton = findViewById(R.id.button_profile);
        userButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChatListActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        Button squareButton = findViewById(R.id.button_square);
        squareButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChatListActivity.this, CommodityListActivity.class);
            startActivity(intent);
        });

    }

    private void provideSuggestions(String query) {
        SharedPreferences preferences = getSharedPreferences("search_history", MODE_PRIVATE);
        Set<String> historySet = preferences.getStringSet("history", new HashSet<>());

        List<ContactSearchSuggestion> suggestions = new ArrayList<>();
        for (String history : historySet) {
            if (history.toLowerCase().contains(query.toLowerCase())) {
                suggestions.add(new ContactSearchSuggestion(history));
            }
        }

        // 将建议项设置到搜索框
        mSearchView.swapSuggestions(suggestions);
    }


    @SuppressLint("MutatingSharedPrefs")
    private void saveSearchHistory(String query) {
        SharedPreferences preferences = getSharedPreferences("search_history", MODE_PRIVATE);
        Set<String> historySet = preferences.getStringSet("history", new HashSet<>());
        if (!query.isEmpty()) {
            historySet.add(query);
        }
        preferences.edit().putStringSet("history", historySet).apply();
    }


    private void performSearch(String query) {
        // 实现搜索逻辑，比如过滤聊天列表
        Toast.makeText(this, "搜索: " + query, Toast.LENGTH_SHORT).show();
    }


}
