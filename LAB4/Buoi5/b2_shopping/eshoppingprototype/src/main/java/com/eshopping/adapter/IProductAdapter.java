package com.eshopping.adapter;

public interface IProductAdapter {
    String reserveStock(String sku, int quantity);
    boolean deductStock(String reserveToken);
}