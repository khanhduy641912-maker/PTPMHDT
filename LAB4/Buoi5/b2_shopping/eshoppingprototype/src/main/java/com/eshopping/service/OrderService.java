package com.eshopping.service;

import com.eshopping.adapter.IEmailAdapter;
import com.eshopping.adapter.IProductAdapter;
import com.eshopping.model.Order;
import com.eshopping.repository.IOrderRepository;

public class OrderService {
    private final IOrderRepository orderRepository;
    private final IProductAdapter productAdapter;
    private final IEmailAdapter emailAdapter;

    public OrderService(IOrderRepository orderRepository, IProductAdapter productAdapter, IEmailAdapter emailAdapter) {
        this.orderRepository = orderRepository;
        this.productAdapter = productAdapter;
        this.emailAdapter = emailAdapter;
    }

    public boolean placeOrder(Order order) {
        String reserveToken = productAdapter.reserveStock("SKU-IP15", 1);
        if (reserveToken == null) return false;

        order.setReserveToken(reserveToken);
        order.setOrderStatus("StockReserved");

        boolean saved = orderRepository.saveOrder(order);
        if (!saved) return false;

        emailAdapter.sendEmail(order.getCustomer().getEmail(), 
            "XÁC NHẬN ĐƠN HÀNG #" + order.getOrderId(), 
            "Cảm ơn bạn đã đặt hàng!");

        return true;
    }
}