package com.eshopping.ui;

import com.eshopping.adapter.MoMoPaymentAdapter;
import com.eshopping.adapter.SendGridEmailAdapter;
import com.eshopping.repository.OrderRepositoryImpl;
import com.eshopping.service.PaymentService;

import javax.swing.*;
import java.awt.*;

public class PaymentGatewayForm extends JFrame {
    private String orderId;
    private double amount;
    private PaymentService paymentService;

    public PaymentGatewayForm(String orderId, double amount) {
        this.orderId = orderId;
        this.amount = amount;

        OrderRepositoryImpl orderRepo = new OrderRepositoryImpl();
        this.paymentService = new PaymentService(orderRepo, new MoMoPaymentAdapter(), new SendGridEmailAdapter());

        initUI();
    }

    private void initUI() {
        setTitle("CỔNG THANH TOÁN ONLINE (MOMO / VNPAY) - UC05");
        setSize(450, 260);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelInfo = new JPanel(new GridLayout(3, 1, 5, 5));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelInfo.add(new JLabel("MÃ ĐƠN HÀNG: " + orderId));
        panelInfo.add(new JLabel("SỐ TIỀN: " + String.format("%,.0f", amount) + " VND"));
        panelInfo.add(new JLabel("Trạng thái: Chờ xác thực OTP / Thẻ"));

        add(panelInfo, BorderLayout.CENTER);

        JPanel panelButtons = new JPanel(new FlowLayout());
        JButton btnPaySuccess = new JButton("Thanh toán THÀNH CÔNG");
        btnPaySuccess.setBackground(new Color(0, 123, 255));
        btnPaySuccess.setForeground(Color.WHITE);

        JButton btnPayFail = new JButton("Thanh toán THẤT BẠI");
        btnPayFail.setBackground(new Color(220, 53, 69));
        btnPayFail.setForeground(Color.WHITE);

        panelButtons.add(btnPaySuccess);
        panelButtons.add(btnPayFail);
        add(panelButtons, BorderLayout.SOUTH);

        btnPaySuccess.addActionListener(e -> handleCallback(true));
        btnPayFail.addActionListener(e -> handleCallback(false));
    }

    private void handleCallback(boolean isSuccess) {
        if (isSuccess) {
            paymentService.handleIPNCallback(orderId, "SUCCESS");
            JOptionPane.showMessageDialog(this, 
                "Cổng thanh toán đã gửi Callback IPN!\nĐơn hàng " + orderId + " -> ĐÃ THANH TOÁN trong CSDL [EShopDB].", 
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new OrderManagementForm().setVisible(true);
        } else {
            paymentService.handleIPNCallback(orderId, "FAILED");
            JOptionPane.showMessageDialog(this, "Thanh toán thất bại!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            this.dispose();
        }
    }
}