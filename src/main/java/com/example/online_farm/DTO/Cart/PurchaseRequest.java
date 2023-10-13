package com.example.online_farm.DTO.Cart;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PurchaseRequest {
    private String product_id;
    private int buy_count;

    public PurchaseRequest() {
    }

    public PurchaseRequest(String product_id, int buy_count) {
        this.product_id = product_id;
        this.buy_count = buy_count;
    }
}
