package com.eshopping.repository;

import com.eshopping.config.DBConnection;
import com.eshopping.model.Customer;
import com.eshopping.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderRepositoryImpl implements IOrderRepository {

    @Override
    public boolean saveCustomer(Customer customer) {
        String sql = "INSERT INTO Customers (CustomerID, FullName, Email, Phone, ShippingAddress) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getCustomerId());
            stmt.setString(2, customer.getFullName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhone());
            stmt.setString(5, customer.getShippingAddress());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveOrder(Order order) {
        saveCustomer(order.getCustomer());
        String sql = "INSERT INTO Orders (OrderID, CustomerID, TotalAmount, OrderStatus, PaymentStatus, ReserveToken) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, order.getOrderId());
            stmt.setString(2, order.getCustomer().getCustomerId());
            stmt.setDouble(3, order.getTotalAmount());
            stmt.setString(4, order.getOrderStatus());
            stmt.setString(5, order.getPaymentStatus());
            stmt.setString(6, order.getReserveToken());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStatus(String orderId, String orderStatus, String paymentStatus) {
        String sql = "UPDATE Orders SET OrderStatus = ?, PaymentStatus = ? WHERE OrderID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderStatus);
            stmt.setString(2, paymentStatus);
            stmt.setString(3, orderId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}