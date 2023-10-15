package com.example.online_farm.Controller;

import com.example.online_farm.Config.UserInfoUserDetails;
import com.example.online_farm.DTO.*;
import com.example.online_farm.Entity.RefreshToken;
import com.example.online_farm.Entity.Role;
import com.example.online_farm.Entity.User;
import com.example.online_farm.Exception.EmailAlreadyExistsException;
import com.example.online_farm.Repository.RefreshTokenRepository;
import com.example.online_farm.Repository.UserRepository;
import com.example.online_farm.Service.JwtService;
import com.example.online_farm.Service.RefreshTokenService;
import com.example.online_farm.Service.UserSevice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserSevice userSevice;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    @CrossOrigin
    public ResponseEntity<AuthRegister> addUser(@RequestBody @Valid AuthRequest user) {
        try {
            // Kiểm tra xem email đã tồn tại trong hệ thống chưa
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new EmailAlreadyExistsException("Email already exists");
            }

            // Tạo một đối tượng User từ dữ liệu trong AuthRequest
            User newUser = userSevice.addUser(user);

            // Tạo một đối tượng UserDetails
            UserDetails userDetails = new UserInfoUserDetails(newUser);

            // Tạo token cho người dùng mới đăng ký
            String token = jwtService.generateToken(newUser.getEmail());

            // Xây dựng AuthRegister
            AuthRegister authResponse = new AuthRegister();
            authResponse.setMessage("Đăng ký thành công");

            UserRegister userData = new UserRegister();
            userData.setAccess_token("Bearer " + token);

            UserDataRegister userDataRegister = new UserDataRegister();
            // Lấy thời gian hiện tại và chuyển đổi sang java.util.Date
            userDataRegister.setCreatedAt(newUser.getCreatedAt());
            userDataRegister.setUpdatedAt(newUser.getUpdatedAt());
            userDataRegister.setEmail(newUser.getEmail());
            userDataRegister.set_id(String.valueOf(newUser.getId()));

            List<String> roleNames = newUser.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toList());
            userDataRegister.setRoles(roleNames);
            userData.setUser(userDataRegister);

            authResponse.setData(userData);
            return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
        } catch (EmailAlreadyExistsException e) {
            AuthRegister authResponse = new AuthRegister();
            authResponse.setMessage("Email đã tồn tại");
            return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            AuthRegister errorResponse = new AuthRegister();
            errorResponse.setMessage(errorMessage); // Set an appropriate error message here
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/me")
    @CrossOrigin
    public ResponseEntity<UserAllMessiage> getAllUsers() {
        List<User> users = userRepository.findAll();
        // Chuyển đổi từ User sang UserDTO và loại bỏ trường password
        List<UserAllDTO> userDTOs = users.stream()
                .map(user -> {
                    UserAllDTO userDTO = new UserAllDTO();
                    userDTO.set_id(String.valueOf(user.getId()));
                    userDTO.setEmail(user.getEmail());
                    userDTO.setName(user.getFullName());
                    userDTO.setAddress(user.getAddress());
                    userDTO.setPhone(user.getSdt());
                    userDTO.setCreatedAt(user.getCreatedAt());
                    userDTO.setUpdateAt(user.getUpdatedAt());
                    userDTO.setDate_of_birth(user.getDate_of_birth());
                    List<String> roleNames = user.getRoles().stream()
                            .map(Role::getName) // giả sử Role có phương thức getName() để lấy tên của vai trò
                            .collect(Collectors.toList());
                    userDTO.setRoles(roleNames);
                    // Thêm các trường khác mà bạn muốn trả về
                    return userDTO;
                })
                .collect(Collectors.toList());

        UserAllMessiage allMessiage = new UserAllMessiage();
        allMessiage.setMasseage("Lấy nguoi dung thanh cong");
        allMessiage.setUserAllDTOS(userDTOs);
        return new ResponseEntity<>(allMessiage, HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    @CrossOrigin
    public ResponseEntity<User> updateUser(@PathVariable int id, @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser == null) {
            // Trả về lỗi 404 Not Found
            return ResponseEntity.notFound().build();
        }

        // Update the user's information based on the request body
        existingUser.setAddress(userUpdateRequest.getAddress());
        existingUser.setFullName(userUpdateRequest.getName());
        existingUser.setSdt(userUpdateRequest.getPhone());
        existingUser.setAvatar(userUpdateRequest.getAvatar());

        // Chuyển đổi chuỗi ngày tháng thành đối tượng java.util.Date
        // Chuyển đổi chuỗi ngày tháng thành đối tượng java.util.Date
        if (userUpdateRequest.getDate_of_birth() != null && !userUpdateRequest.getDate_of_birth().isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date birthDay = dateFormat.parse(userUpdateRequest.getDate_of_birth());
                existingUser.setDate_of_birth(birthDay);
            } catch (ParseException e) {
                // Xử lý lỗi nếu không thể phân tích cú pháp ngày tháng
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }
        existingUser.setUpdatedAt(new Date());

        // Check if a new password is provided and update it if needed
        if (userUpdateRequest.getNewPassword() != null) {
            if (userUpdateRequest.getNewPassword().equals(userUpdateRequest.getPassword())) {
                String newPasswordHash = passwordEncoder.encode(userUpdateRequest.getNewPassword());
                existingUser.setPassWord(newPasswordHash);
            } else {
                // Handle password mismatch error
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }

        // Save the updated user to the database
        User updatedUser = userRepository.save(existingUser);

        // Trả về phản hồi 200 OK với thông tin người dùng đã được cập nhật
        return ResponseEntity.ok(updatedUser);
    }
    @PostMapping("/login")
    @CrossOrigin
    public ResponseEntity<AuthResponse> authenticateAndGetToken(@RequestBody @Valid AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(authRequest.getEmail());
                Object principal = authentication.getPrincipal();
                UserDetails userDetails = (UserDetails) principal;
                Optional<User> user = userRepository.findByEmail(userDetails.getUsername());

                UserData userData = new UserData();
                userData.setAccess_token("Bearer " + token);

                UserAllDTO userAllDTO = new UserAllDTO();
                userAllDTO.set_id(String.valueOf(user.get().getId()));
                userAllDTO.setRoles(user.get().getRoles().stream().map(Role::getName).collect(Collectors.toList()));
                userAllDTO.setEmail(user.get().getEmail());
                userAllDTO.setName(user.get().getFullName());
                userAllDTO.setDate_of_birth(user.get().getDate_of_birth());
                userAllDTO.setAddress(user.get().getAddress());
                userAllDTO.setPhone(user.get().getSdt());
                userAllDTO.setCreatedAt(user.get().getCreatedAt());
                userAllDTO.setUpdateAt(user.get().getUpdatedAt());

                userData.setUser(userAllDTO);

                // Tạo refreshToken
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getEmail());

                // Tạo và trả về AuthResponse chứa cả refreshToken
                AuthResponse authResponse = new AuthResponse();
                authResponse.setMessage("Đăng nhập thành công");
                authResponse.setData(userData);
                authResponse.setRefreshToken(refreshToken.getToken());

                return ResponseEntity.ok(authResponse);
            } else {
                // Nếu tài khoản hoặc mật khẩu không chính xác, trả về phản hồi lỗi
                AuthResponse errorResponse = new AuthResponse();
                errorResponse.setMessage("Tài khoản hoặc mật khẩu không chính xác");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
        } catch (AuthenticationException e) {
            // Xử lý ngoại lệ xảy ra khi không thể xác thực
            AuthResponse errorResponse = new AuthResponse();
            errorResponse.setMessage("Tài khoản hoặc mật khẩu không chính xác");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
    private boolean isValidRefreshToken(String refreshToken) {
        // Thực hiện kiểm tra tính hợp lệ của Refresh Token tại đây
        // Đảm bảo rằng Refresh Token tồn tại trong cơ sở dữ liệu và hợp lệ

        Optional<RefreshToken> refreshTokenOptional = refreshTokenService.findByToken(refreshToken);
        return refreshTokenOptional.isPresent();
    }
    @PostMapping("/logout")
    @CrossOrigin
    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequest logoutRequest) {
        String refreshToken = logoutRequest.getRefreshToken();

        // Kiểm tra tính hợp lệ của Refresh Token trước khi xóa
        if (isValidRefreshToken(refreshToken)) {
            // Xóa Refresh Token từ cơ sở dữ liệu
            refreshTokenService.deleteRefreshToken(refreshToken);

            return ResponseEntity.ok("Đăng xuất thành công");
        } else {
            return ResponseEntity.badRequest().body("Refresh Token không hợp lệ");
        }
    }

    @GetMapping("/refreshToken/findByToken")
    @CrossOrigin
    public ResponseEntity<RefreshToken> findByToken(@RequestParam String token) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(token);

        if (refreshTokenOptional.isPresent()) {
            return ResponseEntity.ok(refreshTokenOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<String> handleAuthenticationException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tài khoản hoặc mật khẩu không chính xác");
    }

    @PostMapping("/refreshToken")
    @CrossOrigin
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getEmail());
                    return JwtResponse.builder()
                            .access_token(accessToken)
                            .token(refreshTokenRequest.getRefreshToken())
                            .build();
                }).orElseThrow(() -> new RuntimeException(
                        "Refresh token is not in database!"));
    }

}
