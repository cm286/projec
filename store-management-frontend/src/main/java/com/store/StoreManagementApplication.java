package com.store;

import com.store.ui.ImportPanel;
import com.store.ui.ExportPanel;
import com.store.ui.StatisticsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Lớp chính của ứng dụng
 */
public class StoreManagementApplication extends JFrame {
    private JTabbedPane mainTabbedPane;
    private ImportPanel importPanel;
    private ExportPanel exportPanel;
    private StatisticsPanel statisticsPanel;

    public StoreManagementApplication() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Hệ thống Quản lý Cửa hàng");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo menu bar
        createMenuBar();

        // Tạo tabbed pane chính
        mainTabbedPane = new JTabbedPane();

        // Tab Nhập hàng
        importPanel = new ImportPanel();
        mainTabbedPane.addTab("Nhập hàng", importPanel);

        // Tab Xuất hàng
        exportPanel = new ExportPanel();
        mainTabbedPane.addTab("Xuất hàng", exportPanel);

        // Tab Thống kê
        statisticsPanel = new StatisticsPanel();
        mainTabbedPane.addTab("Thống kê", statisticsPanel);

        add(mainTabbedPane, BorderLayout.CENTER);

        // Status bar
        JLabel statusLabel = new JLabel("Sẵn sàng");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu File
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Thoát");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        // Menu Quản lý
        JMenu manageMenu = new JMenu("Quản lý");
        JMenuItem importItem = new JMenuItem("Nhập hàng");
        importItem.addActionListener(e -> mainTabbedPane.setSelectedIndex(0));
        manageMenu.add(importItem);

        JMenuItem exportItem = new JMenuItem("Xuất hàng");
        exportItem.addActionListener(e -> mainTabbedPane.setSelectedIndex(1));
        manageMenu.add(exportItem);

        // Menu Thống kê
        JMenu statisticsMenu = new JMenu("Thống kê");
        JMenuItem statsItem = new JMenuItem("Xem thống kê");
        statsItem.addActionListener(e -> mainTabbedPane.setSelectedIndex(2));
        statisticsMenu.add(statsItem);

        // Menu Trợ giúp
        JMenu helpMenu = new JMenu("Trợ giúp");
        JMenuItem aboutItem = new JMenuItem("Về chương trình");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(manageMenu);
        menuBar.add(statisticsMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "Hệ thống Quản lý Cửa hàng v1.0\n\n" +
            "Chức năng:\n" +
            "- Nhập hàng từ nhà cung cấp\n" +
            "- Xuất hàng cho đại lý phụ\n" +
            "- Thống kê doanh thu theo mặt hàng và đại lý\n\n" +
            "© 2025 Store Management System",
            "Về chương trình",
            JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StoreManagementApplication app = new StoreManagementApplication();
            app.setVisible(true);
        });
    }
}
