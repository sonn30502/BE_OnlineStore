package com.example.online_farm.DTO.Cart;

import com.example.online_farm.DTO.Products.ProductDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Date;

@Getter
@Setter
@Service
public class PurchaseDataDto {
    private int buy_count;
    private double price;
    private double price_before_discount;
    private int status;
    private String _id;
    private String user;
    private ProductDTO product;
    private Date createdAt;
    private Date updatedAt;

    public PurchaseDataDto() {
    }
    public PurchaseDataDto(int buy_count, double price, double price_before_discount, int status, String _id, String user, ProductDTO product, Date createdAt, Date updatedAt) {
        this.buy_count = buy_count;
        this.price = price;
        this.price_before_discount = price_before_discount;
        this.status = status;
        this._id = _id;
        this.user = user;
        this.product = product;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
