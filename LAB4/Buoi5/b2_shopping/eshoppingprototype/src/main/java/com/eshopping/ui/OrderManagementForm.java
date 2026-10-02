package com.eshopping.ui;

import com.eshopping.adapter.ExternalPIMAdapter;
import com.eshopping.adapter.SendGridEmailAdapter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class OrderManagementForm extends JFrame {
    private JTable tableOrders;
    private DefaultTableModel tableModel;

    public OrderManagementForm() {
        initUI();
    }

    private void initUI() {
        setTitle("e-SHOPPING - Quản lý Đơn hàng (Admin / Nhân viên) - UC07");
        setSize(650, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        String[] columns = {"Mã Đơn Hàng", "Khách Hàng", "Tổng Tiền (VND)", "Trạng Thái Đơn", "Thanh Toán"};
        tableModel = new DefaultTableModel(columns, 0);
        tableOrders = new JTable(tableModel);

        tableModel.addRow(new Object[]{"ORD_1700000001", "Nguyễn Khánh Duy", "25,000,000", "StockReserved", "Paid"});

        add(new JScrollPane(tableOrders), BorderLayout.CENTER);

        JPanel panelAction = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnComplete = new JButton("XÁC NHẬN HOÀN TẤT GIAO HÀNG (Trừ kho PIM)");
        btnComplete.setBackground(new Color(40, 167, 69));
        btnComplete.setForeground(Color.WHITE);
        panelAction.add(btnComplete);
        add(panelAction, BorderLayout.SOUTH);

        btnComplete.addActionListener(e -> {
            ExternalPIMAdapter pim = new ExternalPIMAdapter();
            SendGridEmailAdapter email = new SendGridEmailAdapter();

            pim.deductStock("TOK_RESERVE_123");
            email.sendEmail("khanhduy@gmail.com", "ĐƠN HÀNG ĐÃ GIAO THÀNH CÔNG", "Cảm ơn bạn đã mua sắm tại e-SHOPPING!");

            tableModel.setValueAt("Completed", 0, 3);
            JOptionPane.showMessageDialog(this, "Đã trừ kho PIM và hoàn tất đơn hàng!");
        });
    }
}