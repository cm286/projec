package com.store.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Panel tổng hợp cho thống kê
 */
public class StatisticsPanel extends JPanel {
    private StatisticsItemPanel itemPanel;
    private StatisticsDistributorPanel distributorPanel;
    private JTabbedPane tabbedPane;

    public StatisticsPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();

        // Tab thống kê mặt hàng
        itemPanel = new StatisticsItemPanel();
        tabbedPane.addTab("Thống kê mặt hàng", itemPanel);

        // Tab thống kê đại lý phụ
        distributorPanel = new StatisticsDistributorPanel();
        tabbedPane.addTab("Thống kê đại lý phụ", distributorPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }
}
