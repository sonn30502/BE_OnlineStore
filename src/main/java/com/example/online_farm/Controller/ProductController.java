package com.example.online_farm.Controller;

import com.example.online_farm.DTO.Products.Message;
import com.example.online_farm.DTO.Products.ProductDTO;
import com.example.online_farm.DTO.Products.ProductsLimit;
import com.example.online_farm.Entity.Images;
import com.example.online_farm.Entity.Product;
import com.example.online_farm.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

//     lấy sản phẩm theo id
    @GetMapping("/products/{id}")
    @CrossOrigin
    public ResponseEntity<Message> getProductById(@PathVariable int id) {
        Product product = productService.getProductById(id);
        if (product != null) {
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
//            List<ProductDTO> products = new ArrayList<>();
//            products.add(productDTO);
//            data.setProducts(products);

            Message response = new Message();
            response.setMessage("Lấy sản phẩm thành công");
            response.setData(productDTO);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

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

    @GetMapping("/products")
    @CrossOrigin
    public ProductsLimit searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "createdAt") String sort_by) {
        if (name != null) {
            return productService.getProductsByField("name", name, page, limit, sort_by);
        } else if (category != null) {
            try {
                return productService.getProductsByField("category", String.valueOf(category), page, limit, sort_by);
            } catch (IllegalArgumentException e) {
                // Xử lý lỗi khi categoryId không tồn tại
                return handleNotFoundError(e.getMessage());
            }
        } else {
            // Trường hợp không có tham số title và categoryId, lấy tất cả sản phẩm
//            return productService.getAllProducts(page, limit);
            return productService.getProductsByField("createdAt", "", page, limit, sort_by);
        }

    }

    private ProductsLimit handleNotFoundError(String errorMessage) {
        ProductsLimit apiResponse = new ProductsLimit();
        apiResponse.setMessage("Lỗi: " + errorMessage);
        // Thêm thông báo lỗi phù hợp vào response
        return apiResponse;
    }
}
