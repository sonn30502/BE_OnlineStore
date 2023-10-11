package com.example.online_farm.DTO.Products;

public class Message {
    private String message;
    private ProductDTO data;

    public Message(String message, ProductDTO data) {
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

    public ProductDTO getData() {
        return data;
    }

    public void setData(ProductDTO data) {
        this.data = data;
    }
}
