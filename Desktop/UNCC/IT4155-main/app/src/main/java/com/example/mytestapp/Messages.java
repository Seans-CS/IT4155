package com.example.mytestapp;

public class Messages {

    private String messageId;
    private String senderId;
    private String receiverId;
    private String messageText;
    private long timestamp;

    public Messages() {
        // Default constructor required for Firebase
    }

    // Adjust the constructor to accept senderId, receiverId, messageText, and timestamp
    public Messages(String messageId, String senderId, String receiverId, String messageText, long timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    // Getter and setter methods
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
