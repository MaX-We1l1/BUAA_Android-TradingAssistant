package com.example.myapplication.chat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;

public class ChatDetailActivity extends AppCompatActivity {

    private ListView messageListView;
    private EditText messageInput;
    private Button sendButton;
    private ChatAdapter messageAdapter; // 自定义的消息适配器
    private ArrayList<ChatItem> messageList; // 消息数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        messageListView = findViewById(R.id.message_list_view);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);

        // 初始化消息列表
        messageList = new ArrayList<>();
        // TODO: 加载聊天记录

        messageAdapter = new ChatAdapter(this, messageList);
        messageListView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString();
            if (!messageText.isEmpty()) {
                // TODO: 发送消息并更新列表
                messageList.add(new ChatItem("0", "我", messageText)); // 示例
                messageAdapter.notifyDataSetChanged();
                messageInput.setText("");
            }
        });
    }
}

