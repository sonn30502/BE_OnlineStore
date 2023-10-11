package com.example.online_farm.DTO.Products;

import com.example.online_farm.DTO.Pagination;

import java.util.List;

public class DataProductLimit {
    private List<ProductDTO> products;
    private Pagination pagination;

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
