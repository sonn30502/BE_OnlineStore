package com.example.online_farm.Controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.online_farm.DTO.ImageMessageDTO;
import com.example.online_farm.Entity.Images;
import com.example.online_farm.Entity.Product;
import com.example.online_farm.Entity.User;
import com.example.online_farm.Repository.ImagesRepository;
import com.example.online_farm.Service.IStorageService;
import com.example.online_farm.Service.ProductService;
import com.example.online_farm.Service.UserSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FileUploadController {
    @Autowired
    private IStorageService storageService;
    @Autowired
    private UserSevice userSevice;
    @Autowired
    private ProductService productService;
    @Autowired
    private ImagesRepository imagesRepository;
    @Autowired
    private Cloudinary cloudinary;

    @PostMapping("/uploadsp/{productId}")
    @CrossOrigin
    public ResponseEntity<Object> uploadFile(@PathVariable int productId, @RequestParam("file") MultipartFile file) {
        ImageMessageDTO responseDTO = new ImageMessageDTO();
        try {
            // Tải lên tệp lên Cloudinary
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = uploadResult.get("secure_url").toString();

            // Lưu đường dẫn ảnh vào cơ sở dữ liệu hoặc làm bất kỳ việc bạn cần
            Images imageUpLoad = new Images();
            imageUpLoad.setProductId(productId);
            imageUpLoad.setImageUrl(imageUrl);
            imagesRepository.save(imageUpLoad);

            // Thiết lập thông báo thành công và trả về đường dẫn ảnh
            responseDTO.setMessage("Upload sản phẩm thành công");
            responseDTO.setData(imageUrl); // Trả về đường dẫn ảnh đã tải lên
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (Exception exception) {
            // Xử lý lỗi và thiết lập thông báo lỗi
            responseDTO.setMessage("Lỗi");
            responseDTO.setData(exception.getMessage()); // Trả về thông tin lỗi cụ thể
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }


    @PostMapping("/uploaduser/{userId}")
    @CrossOrigin
    public ResponseEntity<User> uploadProfileImage(
            @PathVariable int userId,
            @RequestParam("file") MultipartFile file
    ) {
        User user = userSevice.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            User updatedUser = userSevice.uploadProfileImage(user, file);
            return ResponseEntity.ok(updatedUser);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/profile/{userId}")
    @CrossOrigin
    public ResponseEntity<?> getUserProfile(@PathVariable int userId) {
        User user = userSevice.getUserById(userId);
        if (user == null) {
            // Tạo thông báo lỗi
            String errorMessage = String.format("User with ID %d not found", userId);

            // Trả về một ResponseEntity với mã lỗi HTTP 404 và một đối tượng JSON chứa thông báo lỗi.
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\": \"" + errorMessage + "\"}");
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/uploadpro/{proId}")
    @CrossOrigin
    public ResponseEntity<Product> uploadProductImage(
            @PathVariable int proId,
            @RequestParam("file") MultipartFile file
    ) {
        Product product = productService.getProductById(proId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            Product updatedProduct = productService.uploadProductImage(product, file);
            return ResponseEntity.ok(updatedProduct);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
