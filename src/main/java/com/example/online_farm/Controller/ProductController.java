package com.example.online_farm.Controller;

import com.example.online_farm.DTO.ProductsLimit;
import com.example.online_farm.Entity.Product;
import com.example.online_farm.Service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    @CrossOrigin
    public ProductsLimit getProducts(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "30") int limit) {
        return productService.getAllProducts(page, limit);
    }

//     lấy sản phẩm theo id
    @GetMapping("/products/{id}")
    @CrossOrigin
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        Product product = this.productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping("/products/{id}")
//    public Product getProductById(@PathVariable int id) {
//        return productService.getProductById(id);
//    }

    // xóa sản phẩm theo id
    @DeleteMapping("/delete/product/{id}")
    @CrossOrigin
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        // Kiểm tra xem sản phẩm có liên quan đến hình ảnh không
        boolean hasImages = productService.hasImages(id);

        if (!hasImages) {
            // Nếu không có hình ảnh liên quan, thực hiện xóa sản phẩm
            productService.deleteById(id);
            return ResponseEntity.ok("Xóa sản phẩm thành công.");
        } else {
            // Nếu có hình ảnh liên quan, trả về thông báo lỗi
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể xóa sản phẩm vì có hình ảnh liên quan.");
        }
    }

}
