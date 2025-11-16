package com.store.ui;

import com.store.models.*;
import com.store.services.APIClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;

/**
 * Panel Xuất hàng
 */
public class ExportPanel extends JPanel {
    private Distributor selectedDistributor;
    private ExportReceipt currentReceipt;
    private JTable exportTable;
    private DistributorSelectionPanel distributorPanel;
    private ItemSearchPanel itemPanel;
    private JSpinner quantitySpinner;
    private JSpinner priceSpinner;
    private JLabel totalLabel;
    private JButton addItemButton;
    private JButton removeItemButton;
    private JButton submitButton;

    public ExportPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Phần trên: chọn đại lý phụ
        distributorPanel = new DistributorSelectionPanel(new DistributorSelectionPanel.DistributorSelectionListener() {
            @Override
            public void onDistributorSelected(Distributor distributor) {
                selectedDistributor = distributor;
                currentReceipt = new ExportReceipt(
                    "", 
                    "PXH-" + System.currentTimeMillis(), 
                    LocalDateTime.now(), 
                    distributor
                );
                itemPanel.setEnabled(true);
                JOptionPane.showMessageDialog(ExportPanel.this, 
                    "Đã chọn đại lý phụ: " + distributor.getBrandName(), 
                    "Thông tin", 
                    JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void onAddNewDistributor() {
                CreateDistributorDialog dialog = new CreateDistributorDialog((JFrame) SwingUtilities.getWindowAncestor(ExportPanel.this));
                dialog.setVisible(true);
                Distributor newDistributor = dialog.getCreatedDistributor();
                if (newDistributor != null) {
                    selectedDistributor = newDistributor;
                    currentReceipt = new ExportReceipt(
                        "", 
                        "PXH-" + System.currentTimeMillis(), 
                        LocalDateTime.now(), 
                        newDistributor
                    );
                    itemPanel.setEnabled(true);
                }
            }
        });

        // Phần giữa: chọn mặt hàng
        itemPanel = new ItemSearchPanel(new ItemSearchPanel.ItemSelectionListener() {
            @Override
            public void onItemSelected(Item item) {
                showAddItemDialog(item);
            }

            @Override
            public void onAddNewItem() {
                CreateItemDialog dialog = new CreateItemDialog((JFrame) SwingUtilities.getWindowAncestor(ExportPanel.this));
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

        // Bảng danh sách hàng xuất
        String[] columns = {"Mã", "Tên", "SL", "Đơn giá", "Tổng tiền", "Xóa"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        exportTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(exportTable);

        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        totalLabel = new JLabel("Tổng tiền: 0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(totalLabel);

        removeItemButton = new JButton("Xóa dòng");
        removeItemButton.setEnabled(false);
        removeItemButton.addActionListener(e -> removeSelectedItem());
        buttonPanel.add(removeItemButton);

        submitButton = new JButton("Nộp phiếu xuất");
        submitButton.setEnabled(false);
        submitButton.addActionListener(e -> submitExport());
        buttonPanel.add(submitButton);

        // Sắp xếp layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(distributorPanel, BorderLayout.NORTH);
        topPanel.add(itemPanel, BorderLayout.CENTER);
        topPanel.add(inputPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        exportTable.getSelectionModel().addListSelectionListener(e -> {
            removeItemButton.setEnabled(exportTable.getSelectedRow() != -1);
        });
    }

    private void showAddItemDialog(Item item) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Thêm mặt hàng", true);
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Mặt hàng: " + item.getName()));
        panel.add(new JLabel("(SL trong kho: " + item.getQuantity() + ")"));

        panel.add(new JLabel("Số lượng xuất:"));
        JSpinner qtySpinner = new JSpinner(new SpinnerNumberModel(1L, 1L, item.getQuantity(), 1L));
        panel.add(qtySpinner);

        panel.add(new JLabel("Đơn giá:"));
        JSpinner priceSpinnerDialog = new JSpinner(new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, 1000.0));
        panel.add(priceSpinnerDialog);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Hủy");

        okBtn.addActionListener(e -> {
            long qty = ((Number) qtySpinner.getValue()).longValue();
            if (qty > item.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Số lượng xuất không được vượt quá " + item.getQuantity(), "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
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
        // Được gọi từ nút "Thêm mặt hàng"
        long qty = ((Number) quantitySpinner.getValue()).longValue();
        double price = ((Number) priceSpinner.getValue()).doubleValue();
    }

    private void addItemToReceipt(Item item, long quantity, double unitPrice) {
        if (currentReceipt == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đại lý phụ trước", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ExportReceiptItem receiptItem = new ExportReceiptItem(item.getId(), item.getCode(), item.getName(), quantity, unitPrice);
        currentReceipt.addItem(receiptItem);

        updateExportTable();
        updateTotal();
        submitButton.setEnabled(true);
    }

    private void removeSelectedItem() {
        int selectedRow = exportTable.getSelectedRow();
        if (selectedRow != -1) {
            currentReceipt.removeItem(selectedRow);
            updateExportTable();
            updateTotal();
        }
    }

    private void updateExportTable() {
        DefaultTableModel model = (DefaultTableModel) exportTable.getModel();
        model.setRowCount(0);

        if (currentReceipt != null) {
            for (ExportReceiptItem item : currentReceipt.getItems()) {
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

    private void submitExport() {
        if (currentReceipt == null || currentReceipt.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phiếu xuất phải có ít nhất một mặt hàng", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ExportReceipt saved = APIClient.createExportReceipt(currentReceipt);
            printExportReceipt(currentReceipt);
            JOptionPane.showMessageDialog(this, "Nộp phiếu xuất thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);

            // Reset form
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void printExportReceipt(ExportReceipt receipt) {
        StringBuilder sb = new StringBuilder();
        sb.append("========== PHIẾU XUẤT HÀNG ==========\n");
        sb.append("Số phiếu: ").append(receipt.getReceiptNumber()).append("\n");
        sb.append("Ngày xuất: ").append(receipt.getExportDate()).append("\n\n");
        sb.append("Đại lý phụ: ").append(receipt.getDistributor().getBrandName()).append("\n");
        sb.append("Địa chỉ: ").append(receipt.getDistributor().getAddress()).append("\n");
        sb.append("Số điện thoại: ").append(receipt.getDistributor().getPhoneNumber()).append("\n\n");
        sb.append("Chi tiết mặt hàng:\n");
        sb.append("Mã\tTên\t\tSL\tĐơn giá\tTổng tiền\n");

        for (ExportReceiptItem item : receipt.getItems()) {
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
        JDialog printDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "In phiếu xuất", true);
        printDialog.add(scrollPane);
        printDialog.setSize(600, 400);
        printDialog.setLocationRelativeTo(this);
        printDialog.setVisible(true);
    }

    private void clearForm() {
        selectedDistributor = null;
        currentReceipt = null;
        exportTable.setModel(new DefaultTableModel(new String[]{"Mã", "Tên", "SL", "Đơn giá", "Tổng tiền", "Xóa"}, 0));
        totalLabel.setText("Tổng tiền: 0");
        submitButton.setEnabled(false);
        itemPanel.setEnabled(false);
    }
}
