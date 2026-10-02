package com.eshopping.adapter;

public interface IPaymentAdapter {
    String createPaymentUrl(String orderId, double amount);
}