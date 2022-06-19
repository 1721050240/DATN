package com.example.doan.model;

public class MessageModel {
    boolean success;
    String message;

    public MessageModel(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public MessageModel() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
