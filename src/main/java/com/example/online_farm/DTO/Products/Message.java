package com.example.online_farm.DTO.Products;

public class Message {
    private String message;
    private DataProductDetailDto data;

    public Message(String message, DataProductDetailDto data) {
        this.message = message;
        this.data = data;
    }

    public Message(String message) {
        this.message = message;
    }

    public Message() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataProductDetailDto getData() {
        return data;
    }

    public void setData(DataProductDetailDto data) {
        this.data = data;
    }
}
