package com.store.models;

import java.io.Serializable;

/**
 * Lớp đại diện cho từng mặt hàng trong phiếu xuất
 */
public class ExportReceiptItem implements Serializable {
    private String itemId;
    private String itemCode;
    private String itemName;
    private long quantity;
    private double unitPrice;
    private double totalAmount;

    // Constructor mặc định
    public ExportReceiptItem() {
        this.totalAmount = 0;
    }

    // Constructor
    public ExportReceiptItem(String itemId, String itemCode, String itemName, long quantity, double unitPrice) {
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        calculateTotalAmount();
    }

    // Tính tổng tiền tự động
    public void calculateTotalAmount() {
        this.totalAmount = quantity * unitPrice;
    }

    // Getters and Setters
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
        calculateTotalAmount();
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotalAmount();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    @Override
    public String toString() {
        return "ExportReceiptItem{" +
                "itemCode='" + itemCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
