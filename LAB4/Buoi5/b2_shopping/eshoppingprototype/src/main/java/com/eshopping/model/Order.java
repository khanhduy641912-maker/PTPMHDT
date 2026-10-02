package com.eshopping.model;

public class Order {
    private String orderId;
    private Customer customer;
    private double totalAmount;
    private String orderStatus;
    private String paymentStatus;
    private String reserveToken;

    public Order(String orderId, Customer customer, double totalAmount) {
        this.orderId = orderId;
        this.customer = customer;
        this.totalAmount = totalAmount;
        this.orderStatus = "Created";
        this.paymentStatus = "Pending";
    }

    public String getOrderId() { return orderId; }
    public Customer getCustomer() { return customer; }
    public double getTotalAmount() { return totalAmount; }
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getReserveToken() { return reserveToken; }
    public void setReserveToken(String reserveToken) { this.reserveToken = reserveToken; }
}