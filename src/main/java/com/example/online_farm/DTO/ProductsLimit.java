package com.example.online_farm.DTO;

public class ProductsLimit {
    private String message;
    private DataProductLimit data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataProductLimit getData() {
        return data;
    }

    public void setData(DataProductLimit data) {
        this.data = data;
    }
}
