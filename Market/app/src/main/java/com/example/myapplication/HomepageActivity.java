package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.chat.ChatListActivity;
import com.example.myapplication.profile.ProfileActivity;

public class HomepageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // 这里可以设置其他初始化逻辑，比如加载数据等
        Button messagesButton = findViewById(R.id.button_messages);
        messagesButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageActivity.this, ChatListActivity.class);
            startActivity(intent);
        });

        Button userButton = findViewById(R.id.button_profile);
        userButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

    }
}
