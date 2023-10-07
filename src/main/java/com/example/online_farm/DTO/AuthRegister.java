package com.example.online_farm.DTO;

import jakarta.validation.constraints.Size;

public class AuthRegister {

    private String message;
    private UserRegister userRegister;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserRegister getUserRegister() {
        return userRegister;
    }

    public void setUserRegister(UserRegister userRegister) {
        this.userRegister = userRegister;
    }
}
