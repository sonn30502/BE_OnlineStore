package com.example.online_farm.Entity;

import jakarta.persistence.*;

@Entity
@Table(name="cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int Id;
    @Column(name="quantity")
    private int quantity;
    @Column(name="cart_id")
    private int cartId;
    @Column(name="product_id")
    private int productId;

    public CartItem() {
    }

    public CartItem(int id, int quantity, int cardId, int productId) {
        Id = id;
        this.quantity = quantity;
        this.cartId = cardId;
        this.productId = productId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCardId() {
        return cartId;
    }

    public void setCardId(int cardId) {
        this.cartId = cardId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
