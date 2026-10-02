package com.eshopping.model;

public class Customer {
    private String customerId;
    private String fullName;
    private String email;
    private String phone;
    private String shippingAddress;

    public Customer(String customerId, String fullName, String email, String phone, String shippingAddress) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.shippingAddress = shippingAddress;
    }

    public String getCustomerId() { return customerId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getShippingAddress() { return shippingAddress; }
}