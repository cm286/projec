package com.store.ui;

import com.store.models.*;
import com.store.services.APIClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Panel Thống kê mặt hàng
 */
public class StatisticsItemPanel extends JPanel {
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private JButton searchButton;
    private JTable statisticsTable;

    public StatisticsItemPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel tìm kiếm theo thời gian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Chọn khoảng thời gian"));

        searchPanel.add(new JLabel("Từ ngày:"));
        startDateSpinner = new JSpinner(new SpinnerDateModel());
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd"));
        searchPanel.add(startDateSpinner);

        searchPanel.add(new JLabel("Đến ngày:"));
        endDateSpinner = new JSpinner(new SpinnerDateModel());
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd"));
        searchPanel.add(endDateSpinner);

        searchButton = new JButton("Tìm kiếm");
        searchButton.addActionListener(e -> performSearch());
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);

        // Bảng thống kê
        String[] columns = {"Mã mặt hàng", "Tên mặt hàng", "Số lượng đã bán", "Tổng doanh thu"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        statisticsTable = new JTable(tableModel);
        statisticsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        statisticsTable.getSelectionModel().addListSelectionListener(e -> {
            if (statisticsTable.getSelectedRow() != -1) {
                showItemDetails();
            }
        });

        JScrollPane scrollPane = new JScrollPane(statisticsTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void performSearch() {
        java.util.Date startDate = (java.util.Date) startDateSpinner.getValue();
        java.util.Date endDate = (java.util.Date) endDateSpinner.getValue();

        String startDateStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(startDate);
        String endDateStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(endDate);

        try {
            List<StatisticsItemData> dataList = APIClient.getItemStatistics(startDateStr, endDateStr);
            updateTable(dataList);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable(List<StatisticsItemData> dataList) {
        DefaultTableModel model = (DefaultTableModel) statisticsTable.getModel();
        model.setRowCount(0);

        for (StatisticsItemData data : dataList) {
            model.addRow(new Object[]{
                data.getItemCode(),
                data.getItemName(),
                data.getQuantitySold(),
                String.format("%.2f VNĐ", data.getTotalRevenue())
            });
        }
    }

    private void showItemDetails() {
        int selectedRow = statisticsTable.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) statisticsTable.getModel();
            String itemCode = model.getValueAt(selectedRow, 0).toString();
            String itemName = model.getValueAt(selectedRow, 1).toString();

            java.util.Date startDate = (java.util.Date) startDateSpinner.getValue();
            java.util.Date endDate = (java.util.Date) endDateSpinner.getValue();
            String startDateStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(startDate);
            String endDateStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(endDate);

            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), 
                "Chi tiết mặt hàng: " + itemName, false);

            JPanel panel = new JPanel(new BorderLayout());

            // Bảng chi tiết phiếu nhập
            String[] columns = {"Số phiếu", "Ngày nhập", "Nhà cung cấp", "Số lượng", "Tổng tiền"};
            DefaultTableModel detailModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            JTable detailTable = new JTable(detailModel);

            try {
                // Lấy chi tiết
                List<ImportReceipt> imports = APIClient.getItemImportDetails(model.getValueAt(selectedRow, 0).toString(), startDateStr, endDateStr);
                for (ImportReceipt receipt : imports) {
                    detailModel.addRow(new Object[]{
                        receipt.getReceiptNumber(),
                        receipt.getImportDate(),
                        receipt.getSupplier().getName(),
                        receipt.getItemCount(),
                        String.format("%.2f VNĐ", receipt.getTotalAmount())
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

            JScrollPane scrollPane = new JScrollPane(detailTable);
            panel.add(scrollPane, BorderLayout.CENTER);

            dialog.add(panel);
            dialog.setSize(700, 400);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        }
    }
}
