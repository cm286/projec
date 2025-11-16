package com.store.models;

import java.io.Serializable;

/**
 * Lớp đại diện cho dữ liệu thống kê mặt hàng
 */
public class StatisticsItemData implements Serializable {
    private String itemId;
    private String itemCode;
    private String itemName;
    private long quantitySold;
    private double totalRevenue;

    // Constructor mặc định
    public StatisticsItemData() {
    }

    // Constructor đầy đủ
    public StatisticsItemData(String itemId, String itemCode, String itemName, long quantitySold, double totalRevenue) {
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.quantitySold = quantitySold;
        this.totalRevenue = totalRevenue;
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

    public long getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(long quantitySold) {
        this.quantitySold = quantitySold;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    @Override
    public String toString() {
        return "StatisticsItemData{" +
                "itemCode='" + itemCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", quantitySold=" + quantitySold +
                ", totalRevenue=" + totalRevenue +
                '}';
    }
}
