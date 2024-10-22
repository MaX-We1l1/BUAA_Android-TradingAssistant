package com.example.myapplication.database;

import org.litepal.crud.LitePalSupport;

public class ChatMessage extends LitePalSupport {
    private long id; // 消息唯一标识
    private String chatId; // 关联的聊天 ID
    private long senderId; // 发送者 ID
    private long receiverId; // 接收者 ID
    private String messageContent; // 消息内容
    private long timestamp; // 发送时间戳
    private boolean isRead; // 是否已读

    public ChatMessage() {
    }

    public String getChatId() {
        return chatId;
    }

    public long getId() {
        return id;
    }

    public boolean isRead() {
        return isRead;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
