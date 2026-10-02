package com.eshopping.ui;

import com.eshopping.adapter.ExternalPIMAdapter;
import com.eshopping.adapter.SendGridEmailAdapter;
import com.eshopping.model.Customer;
import com.eshopping.model.Order;
import com.eshopping.repository.OrderRepositoryImpl;
import com.eshopping.service.OrderService;

import javax.swing.*;
import java.awt.*;

public class CartAndCheckoutForm extends JFrame {
    private JTextField txtName, txtEmail, txtPhone, txtAddress;
    private JComboBox<String> cbProduct;
    private JSpinner spQty;
    private JButton btnPlaceOrder;
    private OrderService orderService;

    public CartAndCheckoutForm() {
        OrderRepositoryImpl orderRepo = new OrderRepositoryImpl();
        this.orderService = new OrderService(orderRepo, new ExternalPIMAdapter(), new SendGridEmailAdapter());
        initUI();
    }

    private void initUI() {
        setTitle("e-SHOPPING - Form Giỏ hàng & Đặt hàng (UC02, UC03)");
        setSize(500, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 2, 8, 8));

        add(new JLabel(" Chọn sản phẩm:"));
        cbProduct = new JComboBox<>(new String[]{"iPhone 15 Pro - 25,000,000 VND", "MacBook Air M3 - 28,000,000 VND"});
        add(cbProduct);

        add(new JLabel(" Số lượng:"));
        spQty = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        add(spQty);

        add(new JLabel(" Họ và Tên:"));
        txtName = new JTextField("Nguyễn Khánh Duy");
        add(txtName);

        add(new JLabel(" Email nhận HD:"));
        txtEmail = new JTextField("khanhduy@gmail.com");
        add(txtEmail);

        add(new JLabel(" Số Điện Thoại:"));
        txtPhone = new JTextField("0909123456");
        add(txtPhone);

        add(new JLabel(" Địa chỉ Giao Hàng:"));
        txtAddress = new JTextField("123 Cao Thắng, Q.3, TP.HCM");
        add(txtAddress);

        btnPlaceOrder = new JButton("XÁC NHẬN ĐẶT HÀNG");
        btnPlaceOrder.setBackground(new Color(40, 167, 69));
        btnPlaceOrder.setForeground(Color.WHITE);
        add(new JLabel());
        add(btnPlaceOrder);

        btnPlaceOrder.addActionListener(e -> executeOrder());
    }

    private void executeOrder() {
        String orderId = "ORD_" + System.currentTimeMillis();
        Customer customer = new Customer("CUST_" + System.currentTimeMillis(), 
            txtName.getText(), txtEmail.getText(), txtPhone.getText(), txtAddress.getText());

        double price = cbProduct.getSelectedIndex() == 0 ? 25000000 : 28000000;
        int qty = (Integer) spQty.getValue();
        double total = price * qty;

        Order order = new Order(orderId, customer, total);

        boolean success = orderService.placeOrder(order);
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Đặt hàng thành công!\nMã đơn: " + orderId + "\nĐã lưu SQL Server CSDL [EShopDB] thành công.", 
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new PaymentGatewayForm(orderId, total).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Đặt hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CartAndCheckoutForm().setVisible(true));
    }
}