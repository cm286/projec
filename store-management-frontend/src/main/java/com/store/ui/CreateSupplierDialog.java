package com.store.ui;

import com.store.models.*;
import com.store.services.APIClient;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog tạo nhà cung cấp mới
 */
public class CreateSupplierDialog extends JDialog {
    private JTextField codeField;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField phoneField;
    private Supplier createdSupplier;

    public CreateSupplierDialog(JFrame owner) {
        super(owner, "Thêm nhà cung cấp mới", true);
        initComponents();
        setSize(400, 250);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        mainPanel.add(new JLabel("Mã nhà cung cấp:"));
        codeField = new JTextField();
        mainPanel.add(codeField);

        mainPanel.add(new JLabel("Tên nhà cung cấp:"));
        nameField = new JTextField();
        mainPanel.add(nameField);

        mainPanel.add(new JLabel("Địa chỉ:"));
        addressField = new JTextField();
        mainPanel.add(addressField);

        mainPanel.add(new JLabel("Số điện thoại:"));
        phoneField = new JTextField();
        mainPanel.add(phoneField);

        mainPanel.add(new JLabel(""));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel);

        saveButton.addActionListener(e -> saveSupplier());
        cancelButton.addActionListener(e -> dispose());

        add(mainPanel);
    }

    private void saveSupplier() {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();

        if (code.isEmpty() || name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            createdSupplier = APIClient.createSupplier(code, name, address, phone);
            JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Supplier getCreatedSupplier() {
        return createdSupplier;
    }
}
