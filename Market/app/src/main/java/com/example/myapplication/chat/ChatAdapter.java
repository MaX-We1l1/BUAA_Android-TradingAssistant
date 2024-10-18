package com.example.myapplication.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<ChatItem> {

    public ChatAdapter(Context context, List<ChatItem> chats) {
        super(context, 0, chats);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatItem chat = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_chat, parent, false);
        }

        TextView chatName = convertView.findViewById(R.id.chat_name);
        TextView chatPreview = convertView.findViewById(R.id.chat_preview);

        chatName.setText(chat.getName());
        chatPreview.setText(chat.getLastMessage());

        return convertView;
    }
}