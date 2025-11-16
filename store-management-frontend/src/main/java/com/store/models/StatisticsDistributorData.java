package com.store.models;

import java.io.Serializable;

/**
 * Lớp đại diện cho dữ liệu thống kê đại lý phụ
 */
public class StatisticsDistributorData implements Serializable {
    private String distributorId;
    private String distributorCode;
    private String distributorName;
    private double totalRevenue;

    // Constructor mặc định
    public StatisticsDistributorData() {
    }

    // Constructor đầy đủ
    public StatisticsDistributorData(String distributorId, String distributorCode, String distributorName, double totalRevenue) {
        this.distributorId = distributorId;
        this.distributorCode = distributorCode;
        this.distributorName = distributorName;
        this.totalRevenue = totalRevenue;
    }

    // Getters and Setters
    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getDistributorCode() {
        return distributorCode;
    }

    public void setDistributorCode(String distributorCode) {
        this.distributorCode = distributorCode;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    @Override
    public String toString() {
        return "StatisticsDistributorData{" +
                "distributorCode='" + distributorCode + '\'' +
                ", distributorName='" + distributorName + '\'' +
                ", totalRevenue=" + totalRevenue +
                '}';
    }
}
