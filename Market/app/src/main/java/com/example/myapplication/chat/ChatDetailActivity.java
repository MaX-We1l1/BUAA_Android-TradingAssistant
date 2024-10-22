package com.example.myapplication.chat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.database.ChatMessage;
import com.example.myapplication.database.DBFunction;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Objects;

public class ChatDetailActivity extends AppCompatActivity {

    private ListView messageListView;
    private EditText messageInput;
    private Button sendButton;
    private TextView otherUsername;
    private String currentUsername = MainActivity.getCurrentUsername();
    private ChatMessageAdapter messageAdapter; // 自定义的消息适配器
    private ArrayList<ChatMessage> messageList; // 消息数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        messageListView = findViewById(R.id.message_list_view);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        otherUsername = findViewById(R.id.username_text);

        Intent intent = getIntent();
        long recId = intent.getLongExtra("chat_id", 0);
        String recName = intent.getStringExtra("chat_name");
        otherUsername.setText(recName);

        // 初始化消息列表
        String chatId = currentUsername + "_" + recName;

        // TODO: 加载聊天记录

        messageList = (ArrayList<ChatMessage>) DBFunction.findChatHistoryByChatId(currentUsername, recName);
        messageAdapter = new ChatMessageAdapter(this, messageList);
        messageListView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString();
            if (!messageText.isEmpty()) {
                // TODO: 发送消息并更新列表
                long sendId = Objects.requireNonNull(DBFunction.findUserByName(currentUsername)).getId();
                ChatMessage cm = new ChatMessage();
                cm.setChatId(chatId);
                cm.setRead(false);
                cm.setMessageContent(messageText);
                cm.setTimestamp(System.currentTimeMillis());
                cm.setReceiverId(recId);
                cm.setSenderId(sendId);
                cm.save();
                messageList.add(cm);
                messageAdapter.notifyDataSetChanged();
                messageInput.setText("");
            }
        });

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish(); // 返回到上一个活动
        });


    }
}

