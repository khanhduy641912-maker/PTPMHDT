package com.eshopping.repository;

import com.eshopping.model.Customer;
import com.eshopping.model.Order;

public interface IOrderRepository {
    boolean saveCustomer(Customer customer);
    boolean saveOrder(Order order);
    boolean updateStatus(String orderId, String orderStatus, String paymentStatus);
}