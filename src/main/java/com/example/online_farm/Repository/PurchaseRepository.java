package com.example.online_farm.Repository;

import com.example.online_farm.DTO.Cart.PurchaseStatus;
import com.example.online_farm.Entity.Product;
import com.example.online_farm.Entity.Purchase;
import com.example.online_farm.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    Purchase findByUserAndProductAndStatus(User user, Product product, int status);
    List<Purchase> findByStatus(int status);
}
