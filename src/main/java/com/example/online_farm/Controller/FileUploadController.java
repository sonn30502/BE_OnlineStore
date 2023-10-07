package com.example.online_farm.Controller;

import com.example.online_farm.DTO.ImageDTO;
import com.example.online_farm.Entity.Images;
import com.example.online_farm.Repository.ImagesRepository;
import com.example.online_farm.Service.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {
    @Autowired
    private IStorageService storageService;
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
}
