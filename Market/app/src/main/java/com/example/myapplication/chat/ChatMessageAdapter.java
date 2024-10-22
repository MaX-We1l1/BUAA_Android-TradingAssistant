package com.example.myapplication.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.myapplication.R;
import com.example.myapplication.database.ChatMessage;
import com.example.myapplication.database.DBFunction;
import com.example.myapplication.database.User;

import org.litepal.LitePal;

import java.util.List;

public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {
    private final Context context;
    private final List<ChatMessage> messages;

    public ChatMessageAdapter(Context context, List<ChatMessage> megs) {
        super(context, 0, megs);
        this.context = context;
        this.messages = megs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 获取当前消息
        ChatMessage chatMessage = getItem(position);

        // 如果没有复用的视图，则创建一个新的视图
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.message_item, parent, false);
        }

        // 获取视图中的控件
        TextView messageTextView = convertView.findViewById(R.id.message_text);
        TextView senderTextView = convertView.findViewById(R.id.sender_name);
        TextView timestampTextView = convertView.findViewById(R.id.timestamp);

        // 设置消息内容
        assert chatMessage != null;
        String sendName = LitePal.find(User.class, chatMessage.getSenderId()).getUsername();
        messageTextView.setText(chatMessage.getMessageContent());
        senderTextView.setText(String.valueOf(sendName));
        timestampTextView.setText(formatTimestamp(chatMessage.getTimestamp()));

        return convertView;
    }

    // 格式化时间戳为可读格式
    private String formatTimestamp(long timestamp) {
        // 可以使用 SimpleDateFormat 来格式化时间
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
        return sdf.format(new java.util.Date(timestamp));
    }
}
