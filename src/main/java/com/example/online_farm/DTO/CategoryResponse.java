package com.example.online_farm.DTO;

import java.util.List;

public class CategoryResponse {
    private String message;
    private List<CategoryDTO> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CategoryDTO> getData() {
        return data;
    }

    public void setData(List<CategoryDTO> data) {
        this.data = data;
    }
}
