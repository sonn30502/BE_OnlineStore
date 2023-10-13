package com.example.online_farm.DTO.Cart;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PurchaseResponse {
    private String message;
    private PurchaseDataDto data;

    public PurchaseResponse() {
    }
    public PurchaseResponse(String message, PurchaseDataDto data) {
        this.message = message;
        this.data = data;
    }
}
