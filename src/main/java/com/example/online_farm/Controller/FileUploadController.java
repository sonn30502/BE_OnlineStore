package com.example.online_farm.Controller;

import com.example.online_farm.DTO.ImageDTO;
import com.example.online_farm.Entity.Images;
import com.example.online_farm.Entity.User;
import com.example.online_farm.Repository.ImagesRepository;
import com.example.online_farm.Service.IStorageService;
import com.example.online_farm.Service.UserSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileUploadController {
    @Autowired
    private IStorageService storageService;
    @Autowired
    private UserSevice userSevice;
    @Autowired
    ImagesRepository imagesRepository;
    @PostMapping("/upload/{productId}")
    public ResponseEntity<Object> uploadFile(@PathVariable int productId, @RequestParam("file") MultipartFile file) {
        try {
            String generatedFileName = storageService.storeFile(file);
            // Access other fields from the Images object
            Images imageUpLoad = new Images();
            imageUpLoad.setProductId(productId);
            imageUpLoad.setImageUrl(generatedFileName);
            // Do whatever you need to do with productId and imageUrl
            imagesRepository.save(imageUpLoad);
            // Upload the file and get the generated file name
            // Return a response indicating success
            return ResponseEntity.status(HttpStatus.OK).body("upload file thành công");
        } catch (Exception exception) {
            // Return a response indicating an error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi");
        }
    }

    @PostMapping("/upload/{userId}")
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
}
