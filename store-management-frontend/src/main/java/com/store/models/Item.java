package com.store.models;

import java.io.Serializable;

/**
 * Lớp đại diện cho Mặt hàng (Item)
 */
public class Item implements Serializable {
    private String id;
    private String code;
    private String name;
    private String description;
    private long quantity; // Số lượng trong kho
    private double price;  // Giá hiện tại

    // Constructor mặc định
    public Item() {
    }

    // Constructor đầy đủ
    public Item(String id, String code, String name, String description, long quantity, double price) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    // Constructor cho tìm kiếm (không cần id)
    public Item(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.quantity = 0;
        this.price = 0;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
