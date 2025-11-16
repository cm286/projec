package com.store.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp đại diện cho Phiếu xuất
 */
public class ExportReceipt implements Serializable {
    private String id;
    private String receiptNumber;
    private LocalDateTime exportDate;
    private Distributor distributor;
    private List<ExportReceiptItem> items;
    private double totalAmount;

    // Constructor mặc định
    public ExportReceipt() {
        this.items = new ArrayList<>();
        this.totalAmount = 0;
    }

    // Constructor
    public ExportReceipt(String id, String receiptNumber, LocalDateTime exportDate, Distributor distributor) {
        this.id = id;
        this.receiptNumber = receiptNumber;
        this.exportDate = exportDate;
        this.distributor = distributor;
        this.items = new ArrayList<>();
        this.totalAmount = 0;
    }

    // Thêm mặt hàng vào phiếu xuất
    public void addItem(ExportReceiptItem item) {
        this.items.add(item);
        calculateTotalAmount();
    }

    // Xóa mặt hàng khỏi phiếu xuất
    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            this.items.remove(index);
            calculateTotalAmount();
        }
    }

    // Tính tổng tiền tự động
    public void calculateTotalAmount() {
        this.totalAmount = 0;
        for (ExportReceiptItem item : items) {
            this.totalAmount += item.getTotalAmount();
        }
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public LocalDateTime getExportDate() {
        return exportDate;
    }

    public void setExportDate(LocalDateTime exportDate) {
        this.exportDate = exportDate;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    public List<ExportReceiptItem> getItems() {
        return items;
    }

    public void setItems(List<ExportReceiptItem> items) {
        this.items = items;
        calculateTotalAmount();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public int getItemCount() {
        return items.size();
    }

    @Override
    public String toString() {
        return "ExportReceipt{" +
                "receiptNumber='" + receiptNumber + '\'' +
                ", exportDate=" + exportDate +
                ", distributor=" + distributor +
                ", itemCount=" + items.size() +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
