package com.example.online_farm.Entity;

import jakarta.persistence.*;

@Entity
@Table(name="images")
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="image_id")
    private int Id;
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @ManyToOne(targetEntity = Product.class, fetch = FetchType.EAGER)
    private Product product;
    @Column(name = "imageURL")
    private String imageUrl;
    @Column(name = "product_id")
    private int productId;
    public Images() {
    }

    public Images(int id, int productId, String imageUrl) {
        Id = id;
        this.productId = productId;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
