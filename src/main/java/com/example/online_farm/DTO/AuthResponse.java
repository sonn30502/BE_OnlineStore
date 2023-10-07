package com.example.online_farm.DTO;

public class AuthResponse {
    private String message;
    private UserData data;
    private String refreshToken;
    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
