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
import org.springframework.data.domain.Sort;
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

    public Product uploadProductImage(Product product, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = uploadResult.get("secure_url").toString();
            product.setImage(imageUrl);
            return productRepository.save(product);
        }
        return product;
    }

    public Product getProductById(int id){
        return productRepository.findById(id).orElse(null);
    }


    public ProductDTO convertToProduct(Product product) {
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

    public boolean categoryExists(int categoryId) {
        return productRepository.existsByCategoryId(categoryId);
    }

    // tìm kiếm
    public ProductsLimit getProductsByField(String fieldName, String value, int page, int limit, String sort_by) {
        Pageable pageable;
        // Kiểm tra xem fieldName có phải là "categoryId"
        if ("category".equals(fieldName)) {
            // Kiểm tra xem categoryId tồn tại trong cơ sở dữ liệu
            int categoryId = Integer.parseInt(value);
            if (!categoryExists(categoryId)) {
                throw new IllegalArgumentException("Category không tồn tại: " + categoryId);
            }
            pageable = PageRequest.of(page - 1, limit);
        }else if("name".equals(fieldName)){
            // Kiểm tra và thực hiện tìm kiếm theo name
            pageable = PageRequest.of(page - 1, limit);
        }else {
            // Không tìm kiếm theo category hoặc name, sử dụng giá trị sort_by để xác định thứ tự sắp xếp
            switch (sort_by) {
                case "view":
                    pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.desc("view")));
                    break;
                case "sold":
                    pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.desc("sold")));
                    break;
                case "price":
                    pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.asc("price")));
                    break;
                default:
                    // Mặc định sắp xếp theo 'createdAt'
                    pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.desc("createdAt")));
            }
        }
        Page<Product> productPage;
        // Thực hiện tìm kiếm dựa trên fieldName (category hoặc name) hoặc sắp xếp dựa trên sort_by
        if ("name".equals(fieldName)) {
            productPage = productRepository.findByTitleContaining(value, pageable);
        } else if ("category".equals(fieldName)) {
            productPage = productRepository.findByCategoryId(Integer.parseInt(value), pageable);
        } else {
            // Thực hiện truy vấn theo trường sort_by
            productPage = productRepository.findAll(pageable);
        }

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
        apiResponse.setMessage("Lấy sản phẩm thành công");
        apiResponse.setData(data);

        return apiResponse;
    }

}
