package com.example.myapplication.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.commodity.AddCommodityActivity;
import com.example.myapplication.R;
import com.example.myapplication.chat.ChatListActivity;
import com.example.myapplication.profile.ProfileActivity;
import com.example.myapplication.square.CommodityListActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Response;

public class HomepageActivity extends AppCompatActivity {
    private static final ObjectMapper mapper = new ObjectMapper();

    private TextView textApiResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        String apiKey = getString(R.string.api_key);
        String apiSecret = getString(R.string.api_secret);

        Log.d("HomepageActivity", "API Key: " + apiKey);
        Log.d("HomepageActivity", "API Secret: " + apiSecret);

        // 确认 API Key 和 Secret 不是空字符串
        if (apiKey.isEmpty() || apiSecret.isEmpty()) {
            Log.e("HomepageActivity", "API Key or Secret is empty!");
            return; // 避免进一步初始化导致崩溃
        }

        ClientV4 client = new ClientV4.Builder(apiKey)
                .enableTokenCache()
                .networkConfig(300, 100, 100, 100, TimeUnit.SECONDS)
                .connectionPool(new okhttp3.ConnectionPool(8, 1, TimeUnit.SECONDS))
                .build();

        EditText editQueryInput = findViewById(R.id.edit_query_input);
        Button buttonCallApi = findViewById(R.id.button_call_api);
        textApiResult = findViewById(R.id.text_api_result);

        buttonCallApi.setOnClickListener(v -> {
            String query = editQueryInput.getText().toString();
            try {
                callZhipuApi(query,client);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                textApiResult.setText("Error processing JSON: " + e.getMessage());
            }
        });

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

        Button sellButton = findViewById(R.id.button_sell);
        sellButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageActivity.this, AddCommodityActivity.class);
            startActivity(intent);
        });

        Button squareButton = findViewById(R.id.button_square);
        squareButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageActivity.this, CommodityListActivity.class);
            startActivity(intent);
        });

        Button homeButton = findViewById(R.id.button_home);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageActivity.this, HomepageActivity.class);
            startActivity(intent);
        });
    }

    private void callZhipuApi(String query,ClientV4 client) throws JsonProcessingException {
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(),query);
        List<ChatMessage> messageList = new ArrayList<>();
        messageList.add(chatMessage);
        String requestId = String.format("BUAA_Android-TradingAssistant" + String.valueOf(System.currentTimeMillis()));

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelCharGLM3)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messageList)
                .requestId(requestId)
                .build();

        new Thread(() -> {
            try {
                ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);

                // Update UI on the main thread
                new Handler(Looper.getMainLooper()).post(() -> {
                    try {
                        textApiResult.setText("model output: " + mapper.writeValueAsString(invokeModelApiResp));
                    } catch (JsonProcessingException e) {
                        textApiResult.setText("Error: " + e.getMessage());
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                // Update UI on the main thread in case of error
                new Handler(Looper.getMainLooper()).post(() ->
                        textApiResult.setText("API call failed: " + e.getMessage())
                );
            }
        }).start();
    }
}
