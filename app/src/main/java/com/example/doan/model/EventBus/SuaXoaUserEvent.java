package com.example.doan.model.EventBus;

import com.example.doan.model.User;

public class SuaXoaUserEvent {
    User user;

    public SuaXoaUserEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
