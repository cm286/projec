package com.store.ui;

import com.store.models.*;
import com.store.services.APIClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel tìm kiếm và chọn nhà cung cấp
 */
public class SupplierSelectionPanel extends JPanel {
    private JTextField searchField;
    private JButton searchButton;
    private JTable supplierTable;
    private JButton selectButton;
    private JButton addNewButton;
    private Supplier selectedSupplier;
    private SupplierSelectionListener listener;

    public interface SupplierSelectionListener {
        void onSupplierSelected(Supplier supplier);
        void onAddNewSupplier();
    }

    public SupplierSelectionPanel(SupplierSelectionListener listener) {
        this.listener = listener;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Tìm kiếm Nhà cung cấp"));

        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel label = new JLabel("Tên nhà cung cấp:");
        searchField = new JTextField(20);
        searchButton = new JButton("Tìm kiếm");
        addNewButton = new JButton("Thêm mới");

        searchPanel.add(label);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(addNewButton);

        add(searchPanel, BorderLayout.NORTH);

        // Bảng danh sách nhà cung cấp
        String[] columns = {"Mã", "Tên", "Địa chỉ", "Số điện thoại"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        supplierTable = new JTable(tableModel);
        supplierTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(supplierTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel nút chọn
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectButton = new JButton("Chọn");
        selectButton.setEnabled(false);
        buttonPanel.add(selectButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Event listeners
        searchButton.addActionListener(e -> performSearch());
        selectButton.addActionListener(e -> selectSupplier());
        addNewButton.addActionListener(e -> listener.onAddNewSupplier());
        supplierTable.getSelectionModel().addListSelectionListener(e -> {
            selectButton.setEnabled(supplierTable.getSelectedRow() != -1);
        });
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<Supplier> suppliers = APIClient.searchSuppliers(keyword);
            updateTable(suppliers);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selectSupplier() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) supplierTable.getModel();
            String code = model.getValueAt(selectedRow, 0).toString();
            String name = model.getValueAt(selectedRow, 1).toString();
            String address = model.getValueAt(selectedRow, 2).toString();
            String phone = model.getValueAt(selectedRow, 3).toString();

            selectedSupplier = new Supplier(code, name, address, phone);
            listener.onSupplierSelected(selectedSupplier);
        }
    }

    private void updateTable(List<Supplier> suppliers) {
        DefaultTableModel model = (DefaultTableModel) supplierTable.getModel();
        model.setRowCount(0);

        for (Supplier supplier : suppliers) {
            model.addRow(new Object[]{
                supplier.getCode(),
                supplier.getName(),
                supplier.getAddress(),
                supplier.getPhoneNumber()
            });
        }
    }

    public Supplier getSelectedSupplier() {
        return selectedSupplier;
    }
}
