package com.store.ui;

import com.store.models.*;
import com.store.services.APIClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel tìm kiếm và chọn đại lý phụ
 */
public class DistributorSelectionPanel extends JPanel {
    private JTextField searchField;
    private JButton searchButton;
    private JTable distributorTable;
    private JButton selectButton;
    private JButton addNewButton;
    private Distributor selectedDistributor;
    private DistributorSelectionListener listener;

    public interface DistributorSelectionListener {
        void onDistributorSelected(Distributor distributor);
        void onAddNewDistributor();
    }

    public DistributorSelectionPanel(DistributorSelectionListener listener) {
        this.listener = listener;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Tìm kiếm Đại lý phụ"));

        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel label = new JLabel("Tên đại lý:");
        searchField = new JTextField(20);
        searchButton = new JButton("Tìm kiếm");
        addNewButton = new JButton("Thêm mới");

        searchPanel.add(label);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(addNewButton);

        add(searchPanel, BorderLayout.NORTH);

        // Bảng danh sách đại lý phụ
        String[] columns = {"Mã", "Tên thương hiệu", "Địa chỉ", "Số điện thoại"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        distributorTable = new JTable(tableModel);
        distributorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(distributorTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel nút chọn
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectButton = new JButton("Chọn");
        selectButton.setEnabled(false);
        buttonPanel.add(selectButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Event listeners
        searchButton.addActionListener(e -> performSearch());
        selectButton.addActionListener(e -> selectDistributor());
        addNewButton.addActionListener(e -> listener.onAddNewDistributor());
        distributorTable.getSelectionModel().addListSelectionListener(e -> {
            selectButton.setEnabled(distributorTable.getSelectedRow() != -1);
        });
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<Distributor> distributors = APIClient.searchDistributors(keyword);
            updateTable(distributors);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selectDistributor() {
        int selectedRow = distributorTable.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) distributorTable.getModel();
            String code = model.getValueAt(selectedRow, 0).toString();
            String brandName = model.getValueAt(selectedRow, 1).toString();
            String address = model.getValueAt(selectedRow, 2).toString();
            String phone = model.getValueAt(selectedRow, 3).toString();

            selectedDistributor = new Distributor(code, brandName, address, phone);
            listener.onDistributorSelected(selectedDistributor);
        }
    }

    private void updateTable(List<Distributor> distributors) {
        DefaultTableModel model = (DefaultTableModel) distributorTable.getModel();
        model.setRowCount(0);

        for (Distributor distributor : distributors) {
            model.addRow(new Object[]{
                distributor.getCode(),
                distributor.getBrandName(),
                distributor.getAddress(),
                distributor.getPhoneNumber()
            });
        }
    }

    public Distributor getSelectedDistributor() {
        return selectedDistributor;
    }
}
