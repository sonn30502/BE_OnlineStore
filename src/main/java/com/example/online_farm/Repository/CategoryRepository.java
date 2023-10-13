package com.example.online_farm.Repository;

import com.example.online_farm.Entity.Category;
import com.example.online_farm.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findAll();
}
