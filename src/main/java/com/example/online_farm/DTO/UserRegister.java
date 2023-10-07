package com.example.online_farm.DTO;

import java.util.Date;
import java.util.List;

public class UserRegister {
    private String accessToken;
    public String expires = "30P";

    UserDataRegister userDataRegister;

    public UserDataRegister getUserDataRegister() {
        return userDataRegister;
    }

    public void setUserDataRegister(UserDataRegister userDataRegister) {
        this.userDataRegister = userDataRegister;
    }

    public String getExpires() {
        return expires;
    }



    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
