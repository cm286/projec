package com.store.ui;

import com.store.models.*;
import com.store.services.APIClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel tìm kiếm và chọn mặt hàng để nhập
 */
public class ItemSearchPanel extends JPanel {
    private JTextField searchField;
    private JButton searchButton;
    private JTable itemTable;
    private JButton selectButton;
    private JButton addNewButton;
    private ItemSelectionListener listener;

    public interface ItemSelectionListener {
        void onItemSelected(Item item);
        void onAddNewItem();
    }

    public ItemSearchPanel(ItemSelectionListener listener) {
        this.listener = listener;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Tìm kiếm Mặt hàng"));

        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel label = new JLabel("Tên mặt hàng:");
        searchField = new JTextField(20);
        searchButton = new JButton("Tìm kiếm");
        addNewButton = new JButton("Thêm mới");

        searchPanel.add(label);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(addNewButton);

        add(searchPanel, BorderLayout.NORTH);

        // Bảng danh sách mặt hàng
        String[] columns = {"Mã", "Tên", "Mô tả", "SL trong kho"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        itemTable = new JTable(tableModel);
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(itemTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel nút chọn
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectButton = new JButton("Chọn");
        selectButton.setEnabled(false);
        buttonPanel.add(selectButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Event listeners
        searchButton.addActionListener(e -> performSearch());
        selectButton.addActionListener(e -> selectItem());
        addNewButton.addActionListener(e -> listener.onAddNewItem());
        itemTable.getSelectionModel().addListSelectionListener(e -> {
            selectButton.setEnabled(itemTable.getSelectedRow() != -1);
        });
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<Item> items = APIClient.searchItems(keyword);
            updateTable(items);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selectItem() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
            String code = model.getValueAt(selectedRow, 0).toString();
            String name = model.getValueAt(selectedRow, 1).toString();
            String description = model.getValueAt(selectedRow, 2).toString();

            Item item = new Item(code, name, description);
            listener.onItemSelected(item);
        }
    }

    private void updateTable(List<Item> items) {
        DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
        model.setRowCount(0);

        for (Item item : items) {
            model.addRow(new Object[]{
                item.getCode(),
                item.getName(),
                item.getDescription(),
                item.getQuantity()
            });
        }
    }
}
