package com.store.ui;

import com.store.models.*;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog tạo đại lý phụ mới
 */
public class CreateDistributorDialog extends JDialog {
    private JTextField codeField;
    private JTextField brandNameField;
    private JTextField addressField;
    private JTextField phoneField;
    private Distributor createdDistributor;

    public CreateDistributorDialog(JFrame owner) {
        super(owner, "Thêm đại lý phụ mới", true);
        initComponents();
        setSize(400, 250);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        mainPanel.add(new JLabel("Mã đại lý:"));
        codeField = new JTextField();
        mainPanel.add(codeField);

        mainPanel.add(new JLabel("Tên thương hiệu:"));
        brandNameField = new JTextField();
        mainPanel.add(brandNameField);

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

        saveButton.addActionListener(e -> saveDistributor());
        cancelButton.addActionListener(e -> dispose());

        add(mainPanel);
    }

    private void saveDistributor() {
        String code = codeField.getText().trim();
        String brandName = brandNameField.getText().trim();
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();

        if (code.isEmpty() || brandName.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            createdDistributor = com.store.services.APIClient.createDistributor(code, brandName, address, phone);
            JOptionPane.showMessageDialog(this, "Thêm đại lý phụ thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Distributor getCreatedDistributor() {
        return createdDistributor;
    }
}
