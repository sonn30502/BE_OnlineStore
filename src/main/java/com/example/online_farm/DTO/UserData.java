package com.example.online_farm.DTO;

import java.util.Date;
import java.util.List;

public class UserData {
    public String expires = "30P";
    private String accessToken;
    private List<String> roles;
    private int _id;

    private String email;
    private String name;
    private String address;
    private String phone;
    private Date createdAt;
    private Date updatedAt;
    private Date birthDay;

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getAccessToken() {
        return accessToken;
    }


    public String getExpires() {
        return expires;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
