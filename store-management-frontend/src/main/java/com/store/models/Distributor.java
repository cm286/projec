package com.store.models;

import java.io.Serializable;

/**
 * Lớp đại diện cho Đại lý phụ (Distributor)
 */
public class Distributor implements Serializable {
    private String id;
    private String code;
    private String brandName;
    private String address;
    private String phoneNumber;

    // Constructor mặc định
    public Distributor() {
    }

    // Constructor đầy đủ
    public Distributor(String id, String code, String brandName, String address, String phoneNumber) {
        this.id = id;
        this.code = code;
        this.brandName = brandName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Constructor cho tìm kiếm (không cần id)
    public Distributor(String code, String brandName, String address, String phoneNumber) {
        this.code = code;
        this.brandName = brandName;
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

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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
        return "Distributor{" +
                "code='" + code + '\'' +
                ", brandName='" + brandName + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
