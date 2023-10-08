package com.example.online_farm.Service;

import com.example.online_farm.DTO.DataProductLimit;
import com.example.online_farm.DTO.Pagination;
import com.example.online_farm.DTO.ProductDTO;
import com.example.online_farm.DTO.ProductsLimit;
import com.example.online_farm.Entity.Images;
import com.example.online_farm.Entity.Product;
import com.example.online_farm.Repository.ImagesRepository;
import com.example.online_farm.Repository.ProductRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private ImagesRepository imagesRepository;

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

//    // Chuyển đổi ProductDTO thành Product
//    public Product convertFromProductDto(ProductDTO dto){
//        Product product = new Product();
//        product.setId(dto.get_id());
//
////        List<Images> images = new ArrayList<>();
////        for (String imageUrl : dto.getImages()) {
////            Images image = new Images();
////            image.setImageUrl(imageUrl);
////            // Thiết lập product cho image
////            image.setProduct(product);
////            // Thêm image vào danh sách images
////            images.add(image);
////        }
////        product.setImages(images);
//
//        product.setPrice(dto.getPrice());
//        product.setRating(dto.getRating());
//        product.setPriceBeforeDiscount(dto.getPrice_before_discount());
//        product.setQuantity(dto.getQuantity());
//        product.setSold(dto.getSold());
//        product.setView(dto.getView());
//        product.setTitle(dto.getName());
//        product.setDescription(dto.getDescription());
//        product.setCategoryId(dto.getCategory());
//        product.setImage(dto.getImage());
////        try {
////            MultipartFile imageFile = dto.getImageFile();
////            if (imageFile != null && !imageFile.isEmpty()) {
////                String imagePath = saveImage(imageFile);
////                product.setImage(imagePath);
////            }
////        } catch (IOException e) {
////            // Xử lý lỗi nếu có lỗi khi đọc dữ liệu hình ảnh
////            e.printStackTrace();
////        }
//        product.setCreatedAt(dto.getCreatedAt());
//        product.setUpdatedAt(dto.getUpdatedAt());
//        return product;
//    }
//    public Product addProduct(ProductDTO dto) {
//        Product sp = convertFromProductDto(dto);
//        return productRepository.save(sp);
//    }

//    public Product update(ProductDTO dto) {
//        return productRepository.save(convertFromProductDto(dto));
//    }

    public Product getProductById(int id){
        return productRepository.findById(id).get();
    }

//    private String saveImage(MultipartFile imageFile) throws IOException {
//        // Thực hiện xử lý lưu trữ hình ảnh và trả về đường dẫn lưu trữ
//        // Ví dụ: Lưu tệp tin hình ảnh vào thư mục trên máy chủ và trả về đường dẫn tới tệp tin
//        String fileName = imageFile.getOriginalFilename();
//        String filePath = "/path/to/images/" + fileName;
//        imageFile.transferTo(new File(filePath));
//        return filePath;
//    }

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
//            try {
//                MultipartFile imageFile = productDTO.getImageFile();
//                if (imageFile != null && !imageFile.isEmpty()) {
//                    String imagePath = saveImage(imageFile);
//                    product.setImage(imagePath);
//                }
//            } catch (IOException e) {
//                // Xử lý lỗi nếu có lỗi khi đọc dữ liệu hình ảnh
//                e.printStackTrace();
//            }
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
