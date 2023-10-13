package com.example.online_farm.Controller;

import com.example.online_farm.DTO.Cart.PurchaseDataDto;
import com.example.online_farm.DTO.Cart.PurchaseRequest;
import com.example.online_farm.DTO.Cart.PurchaseResponse;
import com.example.online_farm.DTO.Cart.PurchaseStatus;
import com.example.online_farm.DTO.Products.ProductDTO;
import com.example.online_farm.Entity.Product;
import com.example.online_farm.Entity.Purchase;
import com.example.online_farm.Entity.User;
import com.example.online_farm.Service.ProductService;
import com.example.online_farm.Service.PurchaseService;
import com.example.online_farm.Service.UserSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private UserSevice userSevice;
    @Autowired
    private ProductService productService;

    @PostMapping("/purchases/add-to-cart")
    @CrossOrigin
    public ResponseEntity<PurchaseResponse> addToCart(@RequestBody PurchaseRequest addToCartRequest) {
        PurchaseDataDto data = null;
        try {
            User user = userSevice.getCurrentUser(); // Đảm bảo rằng bạn có một phương thức để lấy người dùng hiện tại từ xác thực.
            Product product = productService.getProductById(Integer.parseInt(addToCartRequest.getProduct_id()));
            Purchase purchase = purchaseService.addToCart(user, product, addToCartRequest.getBuy_count());

            // Tạo phản hồi dạng cấu trúc bạn đã cung cấp
            data = new PurchaseDataDto();
            data.setBuy_count(purchase.getBuyCount());
            data.setPrice(purchase.getPrice());
            data.setPrice_before_discount(purchase.getPriceBeforeDiscount());
            data.setStatus(purchase.getStatus());
            data.set_id(String.valueOf(purchase.getId()));
            data.setCreatedAt(new Date());
            data.setUpdatedAt(new Date());
            data.setUser(String.valueOf(user.getId())); // Đảm bảo rằng bạn có một phương thức để lấy id của người dùng.

            ProductDTO productDTO = productService.convertToProduct(product);
            data.setProduct(productDTO);

            // Tạo và trả về phản hồi
            PurchaseResponse apiResponse = new PurchaseResponse("Thêm sản phẩm vào giỏ hàng thành công", data);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            // Xử lý lỗi nếu có
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PurchaseResponse("Lỗi: " + e.getMessage(), null));
        }
    }

    @GetMapping("/purchases")
    @CrossOrigin
    public ResponseEntity<List<PurchaseDataDto>> getPurchases(@RequestParam("status") int status) {
        PurchaseStatus purchaseStatus = PurchaseStatus.fromCode(status);
        if (purchaseStatus == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<Purchase> purchases = purchaseService.getPurchasesByStatus(purchaseStatus.getCode());

        if (purchases.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<PurchaseDataDto> purchaseDataList = purchases.stream()
                .map(purchase -> purchaseService.convertToPurchase(purchase))
                .collect(Collectors.toList());

        return ResponseEntity.ok(purchaseDataList);
    }

}
