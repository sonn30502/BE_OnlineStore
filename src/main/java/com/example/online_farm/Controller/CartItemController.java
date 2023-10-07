package com.example.online_farm.Controller;

import com.example.online_farm.DTO.CardItemId;
import com.example.online_farm.DTO.CardItemIdList;
import com.example.online_farm.Entity.CartItem;
import com.example.online_farm.Repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class CartItemController {
    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartItemController(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @PostMapping("/add-to-cart")
    public ResponseEntity<String> addToCart(@RequestBody CardItemId cartItemRequest) {
        try {
            // Chuyển đổi product_id thành productId và buy_count thành quantity
            CartItem cartItem = new CartItem();
            cartItem.setProductId(cartItemRequest.getProductId());
            cartItem.setQuantity(cartItemRequest.getBuyCount());
            cartItem.setCardId(cartItemRequest.getCartId());

            // Lưu đối tượng vào cơ sở dữ liệu sử dụng JpaRepository
            cartItemRepository.save(cartItem);

            // Trả về thông báo "thêm thành công" và mã trạng thái 200 OK
            return ResponseEntity.status(HttpStatus.OK).body("Thêm thành công");
        } catch (Exception e) {
            // Nếu có lỗi, trả về thông báo lỗi và mã trạng thái 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi thêm");
        }
    }
    @PutMapping("/update-purchase")
    public ResponseEntity<String> updateCartItem(@RequestBody CardItemId cartItemRequest) {
        try {
            // Lấy ra cartItem từ cơ sở dữ liệu bằng productId
            CartItem cartItem = cartItemRepository.findByProductIdAndCartId(
                    cartItemRequest.getProductId(), cartItemRequest.getCartId());

            // Kiểm tra xem cartItem có tồn tại hay không
            if (cartItem == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id không tồn tại");
            }

            // Cập nhật buy_count cho cartItem
            cartItem.setQuantity(cartItemRequest.getBuyCount());

            // Lưu cartItem cập nhật vào cơ sở dữ liệu
            cartItemRepository.save(cartItem);

            // Trả về thông báo "Cập nhật thành công" và mã trạng thái 200 OK
            return ResponseEntity.status(HttpStatus.OK).body("Cập nhật thành công");
        } catch (Exception e) {
            // Nếu có lỗi, trả về thông báo lỗi và mã trạng thái 500 Internal Server Error
            e.printStackTrace(); // In ra lỗi để xem lỗi cụ thể
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi cập nhật");
        }
    }
    @PostMapping("/buy-products")
    public ResponseEntity<String> addMultipleToCart(@RequestBody CardItemIdList requestList) {
        try {
            List<CartItem> cartItems = new ArrayList<>();

            for (CardItemId cartItemRequest : requestList.getCartItems()) {
                boolean check = cartItemRepository.existsByCartIdAndProductId(
                        cartItemRequest.getProductId(), cartItemRequest.getCartId());
                // Kiểm tra xem đã tồn tại bản ghi với cartId và productId tương ứng
                if (check== false) {
                    CartItem cartItem = new CartItem();
                    cartItem.setProductId(cartItemRequest.getProductId());
                    cartItem.setQuantity(cartItemRequest.getBuyCount());
                    cartItem.setCardId(cartItemRequest.getCartId());
                    cartItems.add(cartItem);
                }
                else{
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi thêm vào giỏ hàng");
                }
            }

            // Lưu danh sách đối tượng vào cơ sở dữ liệu sử dụng JpaRepository
            cartItemRepository.saveAll(cartItems);

            // Trả về thông báo "Thêm thành công" và mã trạng thái 200 OK
            return ResponseEntity.status(HttpStatus.OK).body("Thêm thành công");
        } catch (Exception e) {
            // Nếu có lỗi, trả về thông báo lỗi và mã trạng thái 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi thêm vào giỏ hàng");
        }
    }


    @DeleteMapping("/remove-products")
    public ResponseEntity<String> removeMultipleFromCart(@RequestBody CardItemIdList requestList) {
        try {
            boolean allItemsRemoved = false;

            // Lặp qua danh sách đối tượng từ yêu cầu và xóa từng đối tượng khỏi cơ sở dữ liệu
            for (CardItemId cartItemRequest : requestList.getCartItems()) {
                // Tìm kiếm tất cả các đối tượng CartItem trong cơ sở dữ liệu dựa trên productId
                List<CartItem> cartItems = cartItemRepository.findAllByProductId(cartItemRequest.getProductId());

                // Nếu danh sách không rỗng, xóa tất cả các đối tượng khỏi cơ sở dữ liệu
                if (!cartItems.isEmpty()) {
                    cartItemRepository.deleteAll(cartItems);
                } else {
                    allItemsRemoved = true;
                }
            }

            // Trả về thông báo phù hợp và mã trạng thái 200 OK
            if (allItemsRemoved) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("Xóa thành công");
            }
        } catch (Exception e) {
            // Nếu có lỗi, trả về thông báo lỗi và mã trạng thái 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa");
        }
    }



}



