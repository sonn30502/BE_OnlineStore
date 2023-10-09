package com.example.online_farm.DTO;

import com.example.online_farm.Entity.Role;

import java.util.Date;
import java.util.List;

public class UserAllDTO {
    private int Id;
    private List<String> roles;
    private String email;
    private String name;
    private String address;
    private String phone;
    private Date createdAt;
    private Date updateAt;
    private Date date_of_birth;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

//    public List<String> getRoleList() {
//        return roleList;
//    }
//
//    public void setRoleList(List<String> roleList) {
//        this.roleList = roleList;
//    }


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

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
}
