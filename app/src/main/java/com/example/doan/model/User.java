package com.example.doan.model;

import java.io.Serializable;

public class User implements Serializable {
    int id;
    String email;
    String pass;
    String username;
    String mobile;

    public User(int id, String email, String pass, String username, String mobile) {
        this.id = id;
        this.email = email;
        this.pass = pass;
        this.username = username;
        this.mobile = mobile;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
