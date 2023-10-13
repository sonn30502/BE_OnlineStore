package com.example.online_farm.Controller;

import com.cloudinary.api.ApiResponse;
import com.example.online_farm.DTO.Cart.PurchaseDataDto;
import com.example.online_farm.DTO.Cart.PurchaseRequest;
import com.example.online_farm.DTO.Cart.PurchaseResponse;
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

import java.util.Date;

@RestController
@RequestMapping("/api")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private UserSevice userSevice;
    @Autowired
    private ProductService productService;

//    @PostMapping("purchases/add-to-cart")
//    public ResponseEntity<PurchaseResponse> addToCart(@RequestBody PurchaseRequest request) {
//        // Lấy thông tin người dùng hiện tại (cần thêm phần xác thực người dùng)
//        User currentUser = userSevice.getCurrentUser();
//
//        // Lấy thông tin sản phẩm dựa trên request.getProduct_id()
//        Product product = productService.getProductById(Integer.parseInt(request.getProduct_id()));
//
//        // Thêm sản phẩm vào giỏ hàng và lấy thông tin đơn hàng
//        Purchase purchase = purchaseService.addToCart(currentUser, product, request.getBuy_count());
//
//        // Tạo đối tượng PurchaseDataDto cho phản hồi
//        PurchaseDataDto data = purchaseService.convertToPurchase(purchase);
//
//        // Tạo phản hồi
//        PurchaseResponse response = new PurchaseResponse();
//        response.setMessage("Thêm sản phẩm vào giỏ hàng thành công");
//        response.setData(data);
//
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }

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
}
