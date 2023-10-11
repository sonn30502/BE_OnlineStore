package com.example.online_farm.DTO;

public class ImageMessageDTO {
    private String message;
    private String data;

    public ImageMessageDTO() {
    }

    public ImageMessageDTO(String message) {
        this.message = message;
    }


    public ImageMessageDTO(String message, String data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
