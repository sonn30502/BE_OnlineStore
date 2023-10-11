package com.example.online_farm.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.online_farm.DTO.Products.DataProductLimit;
import com.example.online_farm.DTO.Pagination;
import com.example.online_farm.DTO.Products.ProductDTO;
import com.example.online_farm.DTO.Products.ProductsLimit;
import com.example.online_farm.Entity.Images;
import com.example.online_farm.Entity.Product;
import com.example.online_farm.Repository.ImagesRepository;
import com.example.online_farm.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final ImagesRepository imagesRepository;

    @Autowired
    private Cloudinary cloudinary;

    public ProductService(ProductRepository productRepository, ImagesRepository imagesRepository) {
        this.productRepository = productRepository;
        this.imagesRepository = imagesRepository;
    }
    public boolean hasImages(int productId) {
        // Kiểm tra xem sản phẩm có liên quan đến hình ảnh không
        return imagesRepository.existsByProductId(productId);
    }
    public void deleteById(int id){
        productRepository.deleteById(id);
    }

    public Product getProductById(int id){
        return productRepository.findById(id).orElse(null);
    }


    private ProductDTO convertToProduct(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.set_id(String.valueOf(product.getId()));
        productDTO.setPrice(product.getPrice());
        productDTO.setRating(product.getRating());
        productDTO.setPrice_before_discount(product.getPriceBeforeDiscount());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setSold(product.getSold());
        productDTO.setView(product.getView());
        productDTO.setName(product.getTitle());
        productDTO.setDescription(product.getDescription());
        productDTO.setCategory(String.valueOf(product.getCategoryId()));
        productDTO.setImage(product.getImage());
        productDTO.setCreatedAt(product.getCreatedAt());
        productDTO.setUpdatedAt(product.getUpdatedAt());
        productDTO.setImages(product.getImages().stream()
                .map(Images::getImageUrl)
                .collect(Collectors.toList()));


        return productDTO;
    }
    public ProductsLimit getAllProducts(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductDTO> productDTOs = productPage.getContent().stream()
                .map(this::convertToProduct)
                .collect(Collectors.toList());

        Pagination pagination = new Pagination();
        pagination.setPage(page);
        pagination.setLimit(limit);
        pagination.setPage_size(productPage.getTotalPages());

        DataProductLimit data = new DataProductLimit();
        data.setProducts(productDTOs);
        data.setPagination(pagination);

        ProductsLimit apiResponse = new ProductsLimit();
        apiResponse.setMessage("Lấy các sản phẩm thành công");
        apiResponse.setData(data);

        return apiResponse;
    }

    public Product uploadProductImage(Product product, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = uploadResult.get("secure_url").toString();
            product.setImage(imageUrl);
            return productRepository.save(product);
        }
        return product;
    }

    public ProductsLimit searchProductByTitle(String name,int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit); // Lưu ý: Trừ đi 1 từ page để đáp ứng với trang 0-indexed
        Page<Product> productPage = productRepository.findByTitleContaining(name, pageable);

        List<ProductDTO> productDTOs = productPage.getContent().stream()
                .map(this::convertToProduct)
                .collect(Collectors.toList());

        Pagination pagination = new Pagination();
        pagination.setPage(page);
        pagination.setLimit(limit);
        pagination.setPage_size(productPage.getTotalPages());

        DataProductLimit data = new DataProductLimit();
        data.setProducts(productDTOs);
        data.setPagination(pagination);

        ProductsLimit apiResponse = new ProductsLimit();
        apiResponse.setMessage("Lấy các sản phẩm thành công");
        apiResponse.setData(data);

        return apiResponse;
    }

}
