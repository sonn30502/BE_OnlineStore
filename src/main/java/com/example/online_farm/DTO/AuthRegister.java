package com.example.online_farm.DTO;

//import jakarta.validation.constraints.Size;

public class AuthRegister {

    private String message;
    private UserRegister data;

    public AuthRegister() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserRegister getData() {
        return data;
    }

    public void setData(UserRegister data) {
        this.data = data;
    }

}
