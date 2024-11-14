package com.example.myapplication.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.commodity.AddCommodityActivity;
import com.example.myapplication.database.DBFunction;
import com.example.myapplication.home.HomepageActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.chat.ChatListActivity;
import com.example.myapplication.profile.address.AddressActivity;
import com.example.myapplication.profile.cart.CartActivity;
import com.example.myapplication.square.CommodityListActivity;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView usernameTextView;
    private TextView userMoney;

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
        userMoney = findViewById(R.id.user_money);
        userMoney.setText("账户余额： " + DBFunction.findUserByName(MainActivity.getCurrentUsername()).getMoney());

        // 根据用户名设置头像
        setProfileImage(currentUser);

        ImageButton cartButton = findViewById(R.id.button_cart);
        cartButton.setOnClickListener(v-> {
            Intent intent = new Intent(ProfileActivity.this, CartActivity.class);
            startActivity(intent);
        });

        RelativeLayout orderButton = findViewById(R.id.my_order_view);
        orderButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, OrderActivity.class);
            startActivity(intent);
        });

        RelativeLayout favoriteButton = findViewById(R.id.my_favorite_view);
        favoriteButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, FavoriteActivity.class);
            startActivity(intent);
        });

        RelativeLayout addressButton = findViewById(R.id.my_address_view);
        addressButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AddressActivity.class);
            startActivity(intent);
        });

        RelativeLayout settingsButton = findViewById(R.id.my_settings_view);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
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

        Button squareButton = findViewById(R.id.button_square);
        squareButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, CommodityListActivity.class);
            startActivity(intent);
        });

        Button sellButton= findViewById(R.id.button_sell);
        sellButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AddCommodityActivity.class);
            startActivity(intent);
        });

        Button homeButton = findViewById(R.id.button_home);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HomepageActivity.class);
            startActivity(intent);
        });
    }

    protected void onResume() {

        super.onResume();
        userMoney.setText("账户余额： " + DBFunction.findUserByName(MainActivity.getCurrentUsername()).getMoney());
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
