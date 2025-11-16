package com.store.ui;

import com.store.models.*;
import com.store.services.APIClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;

/**
 * Panel Nhập hàng
 */
public class ImportPanel extends JPanel {
    private Supplier selectedSupplier;
    private ImportReceipt currentReceipt;
    private JTable importTable;
    private SupplierSelectionPanel supplierPanel;
    private ItemSearchPanel itemPanel;
    private JSpinner quantitySpinner;
    private JSpinner priceSpinner;
    private JLabel totalLabel;
    private JButton addItemButton;
    private JButton removeItemButton;
    private JButton submitButton;

    public ImportPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Phần trên: chọn nhà cung cấp
        supplierPanel = new SupplierSelectionPanel(new SupplierSelectionPanel.SupplierSelectionListener() {
            @Override
            public void onSupplierSelected(Supplier supplier) {
                selectedSupplier = supplier;
                currentReceipt = new ImportReceipt(
                    "", 
                    "PNH-" + System.currentTimeMillis(), 
                    LocalDateTime.now(), 
                    supplier
                );
                itemPanel.setEnabled(true);
                JOptionPane.showMessageDialog(ImportPanel.this, 
                    "Đã chọn nhà cung cấp: " + supplier.getName(), 
                    "Thông tin", 
                    JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void onAddNewSupplier() {
                CreateSupplierDialog dialog = new CreateSupplierDialog((JFrame) SwingUtilities.getWindowAncestor(ImportPanel.this));
                dialog.setVisible(true);
                Supplier newSupplier = dialog.getCreatedSupplier();
                if (newSupplier != null) {
                    selectedSupplier = newSupplier;
                    currentReceipt = new ImportReceipt(
                        "", 
                        "PNH-" + System.currentTimeMillis(), 
                        LocalDateTime.now(), 
                        newSupplier
                    );
                    itemPanel.setEnabled(true);
                }
            }
        });

        // Phần giữa: chọn mặt hàng
        itemPanel = new ItemSearchPanel(new ItemSearchPanel.ItemSelectionListener() {
            @Override
            public void onItemSelected(Item item) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ImportPanel.this);
                showAddItemDialog(item);
            }

            @Override
            public void onAddNewItem() {
                CreateItemDialog dialog = new CreateItemDialog((JFrame) SwingUtilities.getWindowAncestor(ImportPanel.this));
                dialog.setVisible(true);
                Item newItem = dialog.getCreatedItem();
                if (newItem != null) {
                    showAddItemDialog(newItem);
                }
            }
        });
        itemPanel.setEnabled(false);

        // Panel nhập số lượng và giá
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Nhập số lượng và giá"));

        inputPanel.add(new JLabel("Số lượng:"));
        SpinnerNumberModel quantityModel = new SpinnerNumberModel(1L, 1L, 1000000L, 1L);
        quantitySpinner = new JSpinner(quantityModel);
        inputPanel.add(quantitySpinner);

        inputPanel.add(new JLabel("Đơn giá:"));
        SpinnerNumberModel priceModel = new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, 1000.0);
        priceSpinner = new JSpinner(priceModel);
        inputPanel.add(priceSpinner);

        addItemButton = new JButton("Thêm mặt hàng");
        addItemButton.setEnabled(false);
        addItemButton.addActionListener(e -> addItemToReceipt());
        inputPanel.add(addItemButton);

        // Bảng danh sách hàng nhập
        String[] columns = {"Mã", "Tên", "SL", "Đơn giá", "Tổng tiền", "Xóa"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        importTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(importTable);

        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        totalLabel = new JLabel("Tổng tiền: 0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(totalLabel);

        removeItemButton = new JButton("Xóa dòng");
        removeItemButton.setEnabled(false);
        removeItemButton.addActionListener(e -> removeSelectedItem());
        buttonPanel.add(removeItemButton);

        submitButton = new JButton("Nộp phiếu nhập");
        submitButton.setEnabled(false);
        submitButton.addActionListener(e -> submitImport());
        buttonPanel.add(submitButton);

        // Sắp xếp layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(supplierPanel, BorderLayout.NORTH);
        topPanel.add(itemPanel, BorderLayout.CENTER);
        topPanel.add(inputPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        importTable.getSelectionModel().addListSelectionListener(e -> {
            removeItemButton.setEnabled(importTable.getSelectedRow() != -1);
        });
    }

    private void showAddItemDialog(Item item) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Thêm mặt hàng", true);
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Mặt hàng: " + item.getName()));
        panel.add(new JLabel(""));

        panel.add(new JLabel("Số lượng:"));
        JSpinner qtySpinner = new JSpinner(new SpinnerNumberModel(1L, 1L, 1000000L, 1L));
        panel.add(qtySpinner);

        panel.add(new JLabel("Đơn giá:"));
        JSpinner priceSpinnerDialog = new JSpinner(new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, 1000.0));
        panel.add(priceSpinnerDialog);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Hủy");

        okBtn.addActionListener(e -> {
            long qty = ((Number) qtySpinner.getValue()).longValue();
            double price = ((Number) priceSpinnerDialog.getValue()).doubleValue();
            addItemToReceipt(item, qty, price);
            dialog.dispose();
        });
        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(okBtn);
        buttonPanel.add(cancelBtn);

        panel.add(buttonPanel);
        dialog.add(panel);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void addItemToReceipt() {
        // Được gọi từ nút "Thêm mặt hàng" nhưng cần có item được chọn
        long qty = ((Number) quantitySpinner.getValue()).longValue();
        double price = ((Number) priceSpinner.getValue()).doubleValue();
    }

    private void addItemToReceipt(Item item, long quantity, double unitPrice) {
        if (currentReceipt == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp trước", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ImportReceiptItem receiptItem = new ImportReceiptItem(item.getId(), item.getCode(), item.getName(), quantity, unitPrice);
        currentReceipt.addItem(receiptItem);

        updateImportTable();
        updateTotal();
        submitButton.setEnabled(true);
    }

    private void removeSelectedItem() {
        int selectedRow = importTable.getSelectedRow();
        if (selectedRow != -1) {
            currentReceipt.removeItem(selectedRow);
            updateImportTable();
            updateTotal();
        }
    }

    private void updateImportTable() {
        DefaultTableModel model = (DefaultTableModel) importTable.getModel();
        model.setRowCount(0);

        if (currentReceipt != null) {
            for (ImportReceiptItem item : currentReceipt.getItems()) {
                model.addRow(new Object[]{
                    item.getItemCode(),
                    item.getItemName(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getTotalAmount(),
                    "Xóa"
                });
            }
        }
    }

    private void updateTotal() {
        if (currentReceipt != null) {
            totalLabel.setText(String.format("Tổng tiền: %.2f VNĐ", currentReceipt.getTotalAmount()));
        }
    }

    private void submitImport() {
        if (currentReceipt == null || currentReceipt.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phiếu nhập phải có ít nhất một mặt hàng", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ImportReceipt saved = APIClient.createImportReceipt(currentReceipt);
            printImportReceipt(currentReceipt);
            JOptionPane.showMessageDialog(this, "Nộp phiếu nhập thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);

            // Reset form
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void printImportReceipt(ImportReceipt receipt) {
        StringBuilder sb = new StringBuilder();
        sb.append("========== PHIẾU NHẬP HÀNG ==========\n");
        sb.append("Số phiếu: ").append(receipt.getReceiptNumber()).append("\n");
        sb.append("Ngày nhập: ").append(receipt.getImportDate()).append("\n\n");
        sb.append("Nhà cung cấp: ").append(receipt.getSupplier().getName()).append("\n");
        sb.append("Địa chỉ: ").append(receipt.getSupplier().getAddress()).append("\n");
        sb.append("Số điện thoại: ").append(receipt.getSupplier().getPhoneNumber()).append("\n\n");
        sb.append("Chi tiết mặt hàng:\n");
        sb.append("Mã\tTên\t\tSL\tĐơn giá\tTổng tiền\n");

        for (ImportReceiptItem item : receipt.getItems()) {
            sb.append(item.getItemCode()).append("\t");
            sb.append(item.getItemName()).append("\t");
            sb.append(item.getQuantity()).append("\t");
            sb.append(item.getUnitPrice()).append("\t");
            sb.append(item.getTotalAmount()).append("\n");
        }

        sb.append("\n====================================\n");
        sb.append(String.format("TỔNG TIỀN: %.2f VNĐ", receipt.getTotalAmount())).append("\n");
        sb.append("====================================\n");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JDialog printDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "In phiếu nhập", true);
        printDialog.add(scrollPane);
        printDialog.setSize(600, 400);
        printDialog.setLocationRelativeTo(this);
        printDialog.setVisible(true);
    }

    private void clearForm() {
        selectedSupplier = null;
        currentReceipt = null;
        importTable.setModel(new DefaultTableModel(new String[]{"Mã", "Tên", "SL", "Đơn giá", "Tổng tiền", "Xóa"}, 0));
        totalLabel.setText("Tổng tiền: 0");
        submitButton.setEnabled(false);
        itemPanel.setEnabled(false);
    }
}
