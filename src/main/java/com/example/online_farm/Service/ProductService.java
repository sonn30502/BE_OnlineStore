package com.example.online_farm.Service;

import com.example.online_farm.DTO.DataProductLimit;
import com.example.online_farm.DTO.Pagination;
import com.example.online_farm.DTO.ProductDTO;
import com.example.online_farm.DTO.ProductsLimit;
import com.example.online_farm.Entity.Images;
import com.example.online_farm.Entity.Product;
import com.example.online_farm.Repository.ProductRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductsLimit getAllProducts(int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page -1 , limit); // Trang đầu tiên có page = 1
        Page<Product> productPage = productRepository.findAll(pageRequest);

        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.set_id(product.getId());
            productDTO.setPrice(product.getPrice());
            productDTO.setRating(product.getRating());
            productDTO.setPrice_before_discount(product.getPriceBeforeDiscount());
            productDTO.setQuantity(product.getQuantity());
            productDTO.setSold(product.getSold());
            productDTO.setView(product.getView());
            productDTO.setName(product.getTitle());
            productDTO.setDescription(product.getDescription());
            productDTO.setCategory(product.getCategoryId());
            productDTO.setImage(product.getImage());
            productDTO.setCreatedAt(product.getCreatedAt());
            productDTO.setUpdatedAt(product.getUpdatedAt());

            // Xử lý danh sách hình ảnh
            List<String> imageUrls = new ArrayList<>();
            for (Images image : product.getImages()) {
                imageUrls.add(image.getImageUrl());
            }
            productDTO.setImages(imageUrls);

            productDTOs.add(productDTO);
        }

        // Tạo đối tượng pagination
        Pagination pagination = new Pagination();
        pagination.setPage(page);
        pagination.setLimit(limit);
        pagination.setPage_size(products.size());

        // Tạo đối tượng data
        DataProductLimit data = new DataProductLimit();
        data.setProducts(productDTOs);
        data.setPagination(pagination);

        // Tạo đối tượng ApiResponse và đặt message
        ProductsLimit apiResponse = new ProductsLimit();
        apiResponse.setMessage("Lấy các sản phẩm thành công");
        apiResponse.setData(data);

        return apiResponse;
    }
}
