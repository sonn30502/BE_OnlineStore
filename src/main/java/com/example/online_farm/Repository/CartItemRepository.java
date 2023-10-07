package com.example.online_farm.Repository;

import com.example.online_farm.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
   CartItem findByProductId(int productId);
   CartItem findByProductIdAndCartId(int productId, int cartId);
   List<CartItem> findAllByProductId(int productId);
   boolean existsByCartIdAndProductId(int productId, int cartId);
}
