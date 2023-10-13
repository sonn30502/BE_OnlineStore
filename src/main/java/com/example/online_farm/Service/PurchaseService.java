package com.example.online_farm.Service;

import com.example.online_farm.DTO.Cart.PurchaseDataDto;
import com.example.online_farm.DTO.Cart.PurchaseStatus;
import com.example.online_farm.DTO.Products.ProductDTO;
import com.example.online_farm.Entity.*;
import com.example.online_farm.Repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PurchaseService {
    @Autowired
    private ProductService productService;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private UserSevice userSevice;

//    public PurchaseDataDto convertToPurchase(Purchase purchase) {
//        PurchaseDataDto purchaseDataDto = new PurchaseDataDto();
//        purchaseDataDto.setBuy_count(purchase.getBuyCount());
//        purchaseDataDto.setPrice(purchase.getPrice());
//        purchaseDataDto.setPrice_before_discount(purchase.getPriceBeforeDiscount());
//        purchaseDataDto.setStatus(purchase.getStatus());
//        purchaseDataDto.set_id(String.valueOf(purchase.getId()));
//        purchaseDataDto.setCreatedAt(purchase.getCreatedAt());
//        purchaseDataDto.setUpdatedAt(purchase.getUpdatedAt());
////        purchaseDataDto.setUser(String.valueOf(purchase.getUser().getId()));
//        purchaseDataDto.setProduct(createProductDTO(purchase.getProduct()));
//        return purchaseDataDto;
//    }
//    private ProductDTO createProductDTO(Product product) {
//        ProductDTO productDTO = new ProductDTO();
//        productDTO.set_id(String.valueOf(product.getId()));
//        productDTO.setImages(product.getImages().stream()
//                .map(Images::getImageUrl)
//                .collect(Collectors.toList()));
//        productDTO.setPrice(product.getPrice());
//        productDTO.setRating(product.getRating());
//        productDTO.setPrice_before_discount(product.getPriceBeforeDiscount());
//        productDTO.setQuantity(product.getQuantity());
//        productDTO.setSold(product.getSold());
//        productDTO.setView(product.getView());
//        productDTO.setName(product.getTitle());
//        productDTO.setDescription(product.getDescription());
//        productDTO.setCategory(String.valueOf(product.getCategoryId()));
//        productDTO.setImage(product.getImage());
//        productDTO.setCreatedAt(product.getCreatedAt());
//        productDTO.setUpdatedAt(product.getUpdatedAt());
//        return productDTO;
//    }


    // Tạo mua hàng hoặc cập nhật giỏ hàng nếu sản phẩm đã tồn tại
    public Purchase addToCart(User user, Product product, int buyCount) {
        Purchase existingPurchase = purchaseRepository.findByUserAndProductAndStatus(user, product, PurchaseStatus.IN_CART.getCode());
        if (existingPurchase != null) {
            existingPurchase.setBuyCount(existingPurchase.getBuyCount() + buyCount);
            return purchaseRepository.save(existingPurchase);
        } else {
            Purchase newPurchase = new Purchase();
            newPurchase.setUser(user);
            newPurchase.setProduct(product);
            newPurchase.setBuyCount(buyCount);
            newPurchase.setPrice(product.getPrice());
            newPurchase.setPriceBeforeDiscount(product.getPriceBeforeDiscount());
            newPurchase.setStatus(PurchaseStatus.IN_CART.getCode());
            newPurchase.setCreatedAt(new Date());
            newPurchase.setUpdatedAt(new Date());
            return purchaseRepository.save(newPurchase);
        }
    }
    public List<Purchase> getPurchasesByStatus(int status) {
        return purchaseRepository.findByStatus(status);
    }
    public PurchaseDataDto convertToPurchase(Purchase purchase) {

        PurchaseDataDto purchaseDataDto = new PurchaseDataDto();
        User user = userSevice.getCurrentUser(); // Đảm bảo rằng bạn có một phương thức để lấy người dùng hiện tại từ xác thực.
        purchaseDataDto.setBuy_count(purchase.getBuyCount());
        purchaseDataDto.setPrice(purchase.getPrice());
        purchaseDataDto.setPrice_before_discount(purchase.getPriceBeforeDiscount());
        purchaseDataDto.setStatus(purchase.getStatus());
        purchaseDataDto.set_id(String.valueOf(purchase.getId()));
        purchaseDataDto.setCreatedAt(purchase.getCreatedAt());
        purchaseDataDto.setUpdatedAt(purchase.getUpdatedAt());
        purchaseDataDto.setUser(String.valueOf(user.getId()));

        // Chuyển đổi thông tin về sản phẩm
        ProductDTO productDTO = productService.convertToProduct(purchase.getProduct());
        purchaseDataDto.setProduct(productDTO);

        return purchaseDataDto;
    }

}
