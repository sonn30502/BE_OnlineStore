package com.example.online_farm.Controller;

import com.example.online_farm.Config.UserInfoUserDetails;
import com.example.online_farm.DTO.*;
import com.example.online_farm.Entity.Category;
import com.example.online_farm.Entity.RefreshToken;
import com.example.online_farm.Entity.Role;
import com.example.online_farm.Entity.User;
import com.example.online_farm.Exception.EmailAlreadyExistsException;
import com.example.online_farm.Repository.UserRepository;
import com.example.online_farm.Service.CategoryService;
import com.example.online_farm.Service.JwtService;
import com.example.online_farm.Service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService service;
    @Autowired
    private UserRepository repository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AuthRegister> addUser(@RequestBody @Valid  AuthRequest user) {
        try {
            if (repository.existsByEmail(user.getUsername())) {
                throw new EmailAlreadyExistsException("Email already exists");
            }
            User newUser =  service.addUser(user);

            // Call the addUser method of your service to save the new user


// Lưu newUser vào CSDL bằng service.addUser(newUser) hoặc bất kỳ phương thức nào bạn sử dụng để lưu người dùng mới.

            // Tạo một đối tượng UserDetails
            UserDetails userDetails = new UserInfoUserDetails(newUser);

            // Tạo token cho người dùng mới đăng ký
            String token = jwtService.generateToken(newUser.getEmail());

            // Xây dựng AuthRegister
            AuthRegister authResponse = new AuthRegister();
            authResponse.setMessage("Đăng ký thành công");

            UserRegister userData = new UserRegister();
            userData.setAccessToken("Bearer " + token);
            UserDataRegister userDataRegister = new UserDataRegister();
            // Lấy thời gian hiện tại và chuyển đổi sang java.util.Date
            userDataRegister.setCreatedAt(newUser.getCreatedAt());
            userDataRegister.setUpdatedAt(newUser.getUpdatedAt());
            userDataRegister.setEmail(newUser.getEmail());
            userDataRegister.set_id(newUser.getId());

            List<String> roleNames = newUser.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toList());
            userDataRegister.setRoles(roleNames);
            userData.setUserDataRegister(userDataRegister);

            authResponse.setUserRegister(userData);
            return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
        } catch (EmailAlreadyExistsException e) {
            AuthRegister authResponse = new AuthRegister();
            authResponse.setMessage("Email already exists");
            return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            AuthRegister errorResponse = new AuthRegister();
            errorResponse.setMessage(errorMessage); // Set an appropriate error message here
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }






    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }
    @GetMapping("/categories")
    public CategoryResponse getAll(){
        List<Category> categories = service.getAll();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> {
                    CategoryDTO categoryDTO = new CategoryDTO();
                    categoryDTO.set_id(String.valueOf(category.getId()));
                    categoryDTO.setName(category.getName());
                    return categoryDTO;
                })
                .collect(Collectors.toList());

        CategoryResponse response = new CategoryResponse();
        response.setMessage("Lấy categories thành công");
        response.setData(categoryDTOs);

        return response;
    }



    @PostMapping("/authenticate")
    public AuthResponse authenticateAndGetToken(@RequestBody @Valid AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(authRequest.getUsername());
                Object principal = authentication.getPrincipal();
                UserDetails userDetails = (UserDetails) principal;
                Optional<User> user = repository.findByEmail(userDetails.getUsername());

                UserData userData = new UserData();
                userData.setAccessToken("Bearer " + token);
                userData.setName(user.get().getFullName());
                userData.setPhone(user.get().getSdt());
                userData.setAddress(user.get().getAddress());
                userData.setEmail(user.get().getEmail());
                userData.setBirthDay(user.get().getBirthDay());
                userData.setCreatedAt(user.get().getCreatedAt());
                userData.setUpdatedAt(user.get().getUpdatedAt());
                userData.set_id(user.get().getId());
//                userData.getCreatedAt();
//                userData.getAddress();
//                userData.getPhone();
//                userData.getUpdatedAt();
                List<String> roleNames = user.get().getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList());
                userData.setRoles(roleNames);

                // Tạo refreshToken
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());

                // Tạo và trả về AuthResponse chứa cả refreshToken
                AuthResponse authResponse = new AuthResponse();
                authResponse.setMessage("Đăng nhập thành công");
                authResponse.setData(userData);
                authResponse.setRefreshToken(refreshToken.getToken());

                return authResponse;
            } else {
                throw new UsernameNotFoundException("Tài khoản hoặc mật khẩu không chính xác");
            }
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Tài khoản hoặc mật khẩu không chính xác", e);
        }
    }
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<String> handleAuthenticationException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tài khoản hoặc mật khẩu không chính xác");
    }


    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getEmail());
                    return JwtResponse.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.getToken())
                            .build();
                }).orElseThrow(() -> new RuntimeException(
                        "Refresh token is not in database!"));
    }
}
