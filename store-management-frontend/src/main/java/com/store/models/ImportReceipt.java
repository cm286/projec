package com.store.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp đại diện cho Phiếu nhập
 */
public class ImportReceipt implements Serializable {
    private String id;
    private String receiptNumber;
    private LocalDateTime importDate;
    private Supplier supplier;
    private List<ImportReceiptItem> items;
    private double totalAmount;

    // Constructor mặc định
    public ImportReceipt() {
        this.items = new ArrayList<>();
        this.totalAmount = 0;
    }

    // Constructor
    public ImportReceipt(String id, String receiptNumber, LocalDateTime importDate, Supplier supplier) {
        this.id = id;
        this.receiptNumber = receiptNumber;
        this.importDate = importDate;
        this.supplier = supplier;
        this.items = new ArrayList<>();
        this.totalAmount = 0;
    }

    // Thêm mặt hàng vào phiếu nhập
    public void addItem(ImportReceiptItem item) {
        this.items.add(item);
        calculateTotalAmount();
    }

    // Xóa mặt hàng khỏi phiếu nhập
    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            this.items.remove(index);
            calculateTotalAmount();
        }
    }

    // Tính tổng tiền tự động
    public void calculateTotalAmount() {
        this.totalAmount = 0;
        for (ImportReceiptItem item : items) {
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

    public LocalDateTime getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDateTime importDate) {
        this.importDate = importDate;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public List<ImportReceiptItem> getItems() {
        return items;
    }

    public void setItems(List<ImportReceiptItem> items) {
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
        return "ImportReceipt{" +
                "receiptNumber='" + receiptNumber + '\'' +
                ", importDate=" + importDate +
                ", supplier=" + supplier +
                ", itemCount=" + items.size() +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
