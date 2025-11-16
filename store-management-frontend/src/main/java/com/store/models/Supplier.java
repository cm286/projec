package com.store.models;

import java.io.Serializable;

/**
 * Lớp đại diện cho Nhà cung cấp (Supplier)
 */
public class Supplier implements Serializable {
    private String id;
    private String code;
    private String name;
    private String address;
    private String phoneNumber;

    // Constructor mặc định
    public Supplier() {
    }

    // Constructor đầy đủ
    public Supplier(String id, String code, String name, String address, String phoneNumber) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Constructor cho tìm kiếm (không cần id)
    public Supplier(String code, String name, String address, String phoneNumber) {
        this.code = code;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
