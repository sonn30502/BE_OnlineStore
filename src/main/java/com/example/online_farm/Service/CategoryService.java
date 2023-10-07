package com.example.online_farm.Service;

import com.example.online_farm.DTO.AuthRequest;
import com.example.online_farm.Entity.Category;
import com.example.online_farm.Entity.Role;
import com.example.online_farm.Entity.User;
import com.example.online_farm.Repository.CategoryRepository;
import com.example.online_farm.Repository.RoleRepository;
import com.example.online_farm.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository repository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public User addUser(AuthRequest userInfo) {
        User user = new User();
        user.setEmail(userInfo.getUsername());
        user.setPassWord(passwordEncoder.encode(userInfo.getPassword()));
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        Role roleUser = roleRepository.findByName("ROLE_USER");

        if (roleUser == null) {
            // Nếu "ROLE_USER" chưa tồn tại, bạn có thể tạo nó
            roleUser = new Role("ROLE_USER");
            roleRepository.save(roleUser);
        }

        List<Role> roles = new ArrayList<>();
        roles.add(roleUser);
        user.setRoles(roles);

        return repository.save(user);
    }
}
