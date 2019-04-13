package com.example.test1706;

public class ChatMessage {
    private String messageUser_NguoiNhan;
    private String messageText;
    private String messageUser;
    private long messageTime;
    private String userUID;

    public ChatMessage() {
    }


    public ChatMessage(String messageText, String messageUser, long messageTime, String messageUser_NguoiNhan, String userUID) {
        this.messageUser_NguoiNhan = messageUser_NguoiNhan;
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTime = messageTime;
        this.userUID = userUID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getMessageUser_NguoiNhan() {
        return messageUser_NguoiNhan;
    }

    public void setMessageUser_NguoiNhan(String messageUser_NguoiNhan) {
        this.messageUser_NguoiNhan = messageUser_NguoiNhan;
    }


    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
