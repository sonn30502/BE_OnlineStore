package com.example.online_farm.DTO;

import java.util.Date;
import java.util.List;

public class UserRegister {
    private String access_token;
    public String expires = "30P";
    private UserDataRegister user;

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public UserRegister() {
    }

    public UserDataRegister getUser() {
        return user;
    }

    public void setUser(UserDataRegister user) {
        this.user = user;
    }

//    public String getExpires() {
//        return expires;
//    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

}
