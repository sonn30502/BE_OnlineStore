package com.example.online_farm.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class UserUpdateRequest {
    @Size(max = 160, message = "Không được quá 160 ký tự")
    private String name;

    @Size(max = 20, message = "Không được quá 20 ký tự")
    private String phone;

    @Size(max = 160, message = "Không được quá 160 ký tự")
    private String address;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private String birthDay;

    @Size(max = 1000, message = "Không được quá 1000 ký tự")
    private String avatar;

    @Size(min = 6, max = 160, message = "Mật khẩu phải từ 6 đến 160 ký tự")
    private String password;

    @Size(min = 6, max = 160, message = "Mật khẩu mới phải từ 6 đến 160 ký tự")
    private String newPassword;

    private Date updateAt;

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
    // Getters and setters for all fields

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

