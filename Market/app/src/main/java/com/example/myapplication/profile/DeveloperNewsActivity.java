package com.example.myapplication.profile;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;

public class DeveloperNewsActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.developer_news);

        Log.d("DeveloperNewsActivity" , "Succeed");

        String someData = getIntent().getStringExtra("some_key");
        if (someData != null) {
            Log.d("DeveloperNewsActivity", "Received data: " + someData);
        } else {
            Log.d("DeveloperNewsActivity", "No data received");
        }
    }
}
