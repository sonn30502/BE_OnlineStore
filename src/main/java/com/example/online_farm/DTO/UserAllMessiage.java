package com.example.online_farm.DTO;

import java.util.List;

public class UserAllMessiage {
    private String masseage;
    private List<UserAllDTO> data;

    public String getMasseage() {
        return masseage;
    }

    public void setMasseage(String masseage) {
        this.masseage = masseage;
    }

    public List<UserAllDTO> getUserAllDTOS() {
        return data;
    }

    public void setUserAllDTOS(List<UserAllDTO> userAllDTOS) {
        this.data = userAllDTOS;
    }
}
