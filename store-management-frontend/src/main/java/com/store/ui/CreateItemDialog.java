package com.store.ui;

import com.store.models.*;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog tạo mặt hàng mới
 */
public class CreateItemDialog extends JDialog {
    private JTextField codeField;
    private JTextField nameField;
    private JTextField descriptionField;
    private Item createdItem;

    public CreateItemDialog(JFrame owner) {
        super(owner, "Thêm mặt hàng mới", true);
        initComponents();
        setSize(400, 200);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        mainPanel.add(new JLabel("Mã mặt hàng:"));
        codeField = new JTextField();
        mainPanel.add(codeField);

        mainPanel.add(new JLabel("Tên mặt hàng:"));
        nameField = new JTextField();
        mainPanel.add(nameField);

        mainPanel.add(new JLabel("Mô tả:"));
        descriptionField = new JTextField();
        mainPanel.add(descriptionField);

        mainPanel.add(new JLabel(""));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton saveButton = new JButton("Lưu");
        JButton cancelButton = new JButton("Hủy");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel);

        saveButton.addActionListener(e -> saveItem());
        cancelButton.addActionListener(e -> dispose());

        add(mainPanel);
    }

    private void saveItem() {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String description = descriptionField.getText().trim();

        if (code.isEmpty() || name.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            createdItem = com.store.services.APIClient.createItem(code, name, description);
            JOptionPane.showMessageDialog(this, "Thêm mặt hàng thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Item getCreatedItem() {
        return createdItem;
    }
}
