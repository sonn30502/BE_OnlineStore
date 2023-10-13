package com.example.online_farm.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.online_farm.Config.UserInfoUserDetails;
import com.example.online_farm.DTO.AuthRequest;
import com.example.online_farm.Entity.Role;
import com.example.online_farm.Entity.User;
import com.example.online_farm.Repository.RoleRepository;
import com.example.online_farm.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserSevice {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Cloudinary cloudinary;

    public User addUser(AuthRequest userInfo) {
        User user = new User();
        user.setEmail(userInfo.getEmail());
        user.setPassWord(passwordEncoder.encode(userInfo.getPassword()));
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        Role roleUser = roleRepository.findByName("USER");

        if (roleUser == null) {
            // Nếu "ROLE_USER" chưa tồn tại, bạn có thể tạo nó
            roleUser = new Role("USER");
            roleRepository.save(roleUser);
        }

        List<Role> roles = new ArrayList<>();
        roles.add(roleUser);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public User uploadProfileImage(User user, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = uploadResult.get("secure_url").toString();
            user.setAvatar(imageUrl);
            return userRepository.save(user);
        }
        return user;
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }


    // lay user hien tai
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName(); // Lấy tên người dùng (thường là email)
            return userRepository.findByEmail(email).orElse(null);
        }
        return null;
    }
}

