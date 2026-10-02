package com.eshopping.adapter;

public class MoMoPaymentAdapter implements IPaymentAdapter {
    @Override
    public String createPaymentUrl(String orderId, double amount) {
        return "https://test-payment.momo.vn/pay?orderId=" + orderId + "&amount=" + (long)amount;
    }
}