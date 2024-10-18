package com.example.myapplication.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {

    private ListView chatListView;
    private EditText searchBox;
    private ImageView searchButton, menuButton;
    private ChatAdapter chatAdapter; // 你的聊天适配器
    private ArrayList<ChatItem> chatList; // 聊天列表数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);

        chatListView = findViewById(R.id.chat_list_view);
        searchBox = findViewById(R.id.search_box);
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

        // 点击聊天列表项跳转到具体聊天界面
        chatListView.setOnItemClickListener((parent, view, position, id) -> {
            // TODO: 跳转到聊天界面
            Intent intent = new Intent(ChatListActivity.this, ChatDetailActivity.class);
            intent.putExtra("chat_id", chatList.get(position).getId()); // 传递聊天 ID
            startActivity(intent);
        });
    }
}
