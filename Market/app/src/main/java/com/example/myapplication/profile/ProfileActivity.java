package com.example.myapplication.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.HomepageActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.chat.ChatListActivity;
import com.example.myapplication.profile.FavoriteActivity;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView usernameTextView;

    // 假设 currentUser 是当前登录的用户名
    private final String currentUser = MainActivity.getCurrentUsername(); // 这个可以根据实际情况进行获取

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //初始化控件
        profileImageView = findViewById(R.id.profile_image);
        usernameTextView = findViewById(R.id.username);

        // 设置用户名
        usernameTextView.setText(currentUser);

        // 根据用户名设置头像
        setProfileImage(currentUser);

        ImageButton orderButton = findViewById(R.id.order_button);
        orderButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, OrderActivity.class);
            startActivity(intent);
        });

        ImageButton favoriteButton = findViewById(R.id.favorite_button);
        favoriteButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, FavoriteActivity.class);
            startActivity(intent);
        });

        ImageButton addressButton = findViewById(R.id.address_button);
        addressButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AddressActivity.class);
            startActivity(intent);
        });


        // 这里可以设置其他初始化逻辑，比如加载数据等
        Button messagesButton = findViewById(R.id.button_messages);
        messagesButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ChatListActivity.class);
            startActivity(intent);
        });

        Button userButton = findViewById(R.id.button_profile);
        userButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    /**
     * 根据用户名动态设置头像
     * @param username 当前用户的用户名
     */
    private void setProfileImage(String username) {
        int avatarResId;

        // 根据用户名来选择头像资源
        switch (username) {
            default:
                avatarResId = R.drawable.default_avatar; // 如果用户名没有匹配的头像，使用默认头像
                break;
        }

        // 设置头像
        profileImageView.setImageResource(avatarResId);
    }

    //TODO 根据用户名选择地址



}
