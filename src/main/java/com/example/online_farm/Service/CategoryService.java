package com.example.online_farm.Service;

import com.example.online_farm.DTO.AuthRequest;
import com.example.online_farm.Entity.Category;
import com.example.online_farm.Entity.Role;
import com.example.online_farm.Entity.User;
import com.example.online_farm.Repository.CategoryRepository;
import com.example.online_farm.Repository.RoleRepository;
import com.example.online_farm.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category save(Category d) {
        return categoryRepository.save(d);
    }

    public Category update(Category d) {
        return categoryRepository.save(d);
    }

    public void deleteById(int id) {
        categoryRepository.deleteById(id);
    }



    public Page<Category> getAllForCategoryPageable(int page, int size) {
        return categoryRepository.findAll(PageRequest.of(page, size));
    }

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).get();
    }

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

}
