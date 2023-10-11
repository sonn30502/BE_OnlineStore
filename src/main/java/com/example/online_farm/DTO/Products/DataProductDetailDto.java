package com.example.online_farm.DTO.Products;

import java.util.List;

public class DataProductDetailDto {
    private List<ProductDTO> products;

    public DataProductDetailDto(List<ProductDTO> products) {
        this.products = products;
    }

    public DataProductDetailDto() {
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}
