package com.example.myapplication.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.square.CommodityListActivity;

public class SettingsActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ImageButton personalInfoButton = findViewById(R.id.personal_info_button);
        personalInfoButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, PersonalInfoActivity.class);
            startActivity(intent);
        });

        ImageButton settingsButton = findViewById(R.id.developer_news_button);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, DeveloperNewsActivity.class);
            startActivity(intent);
        });

        ImageButton privacyPolicyButton = findViewById(R.id.privacy_policy_button);
        privacyPolicyButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, PrivacyPolicyActivity.class);
            startActivity(intent);
        });

        RelativeLayout backButton = findViewById(R.id.relative_back);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        RelativeLayout exitLoginButton = findViewById(R.id.relative_exit);
        exitLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        RelativeLayout userInfoListButton = findViewById(R.id.relative_user_info);
        userInfoListButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, UserInfoListActivity.class);
            startActivity(intent);
        });

    }
}
