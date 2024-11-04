package com.example.myapplication.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class PrivacyPolicyActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy);

        Button agree_button = findViewById(R.id.agree_button);
        agree_button.setOnClickListener(v -> {
            Intent intent = new Intent(PrivacyPolicyActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        Button exit_button = findViewById(R.id.exit_button);
        exit_button.setOnClickListener(v -> {
            Intent intent = new Intent(PrivacyPolicyActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
