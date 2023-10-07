package com.example.online_farm.Entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int Id;
    @Column(name="price")
    private int price;
    @Column(name="price_before_discount")
    private  int priceBeforeDiscount;
    @Column(name="quantity")
    private int quantity;
    @Column(name="rating")
    private int rating;
    @Column(name="sold")
    private int sold;
    @Column(name="view")
    private int view;
    @Column(name="title")
    private String title;
    @Column(name="description")
    private String description;
    @Column(name="category_id")
    private int categoryId;
    @Column(name="image")
    private String image;
    @Column(name="created_at")
    private Date createdAt;
    @Column(name="updated_at")
    private Date updatedAt;
    @Column(name="discount")
    private int discount;
    @OneToMany(mappedBy = "product", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Images> images;

    public Product() {
    }

    public Product(int id, int price, int priceBeforeDiscount, int quantity, int rating, int sold, int view, String title, String description, int categoryId, String image, Date createdAt, Date updatedAt, int discount, List<Images> images) {
        Id = id;
        this.price = price;
        this.priceBeforeDiscount = priceBeforeDiscount;
        this.quantity = quantity;
        this.rating = rating;
        this.sold = sold;
        this.view = view;
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.discount = discount;
        this.images = images;
    }

    public Product(int id) {
        Id = id;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPriceBeforeDiscount() {
        return priceBeforeDiscount;
    }

    public void setPriceBeforeDiscount(int priceBeforeDiscount) {
        this.priceBeforeDiscount = priceBeforeDiscount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }
    public void addImages(Images tempImages){
        if(images == null){
            images = new ArrayList<>();
        }
        images.add(tempImages);
        tempImages.setProduct(this);
    }
}
