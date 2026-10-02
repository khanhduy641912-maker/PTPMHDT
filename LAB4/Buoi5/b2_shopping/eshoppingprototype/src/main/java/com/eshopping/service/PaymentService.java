package com.eshopping.service;

import com.eshopping.adapter.IEmailAdapter;
import com.eshopping.adapter.IPaymentAdapter;
import com.eshopping.repository.IOrderRepository;

public class PaymentService {
    private final IOrderRepository orderRepository;
    private final IPaymentAdapter paymentAdapter;
    private final IEmailAdapter emailAdapter;

    public PaymentService(IOrderRepository orderRepository, IPaymentAdapter paymentAdapter, IEmailAdapter emailAdapter) {
        this.orderRepository = orderRepository;
        this.paymentAdapter = paymentAdapter;
        this.emailAdapter = emailAdapter;
    }

    public String initiatePayment(String orderId, double amount) {
        return paymentAdapter.createPaymentUrl(orderId, amount);
    }

    public boolean handleIPNCallback(String orderId, String status) {
        if ("SUCCESS".equals(status)) {
            orderRepository.updateStatus(orderId, "StockReserved", "Paid");
            emailAdapter.sendEmail("khachhang@gmail.com", "HÓA ĐƠN THANH TOÁN #" + orderId, "Đã thanh toán thành công!");
            return true;
        } else {
            orderRepository.updateStatus(orderId, "StockReserved", "Failed");
            return false;
        }
    }
}