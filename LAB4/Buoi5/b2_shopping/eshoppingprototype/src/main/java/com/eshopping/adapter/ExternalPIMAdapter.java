package com.eshopping.adapter;

public class ExternalPIMAdapter implements IProductAdapter {
    @Override
    public String reserveStock(String sku, int quantity) {
        return "TOK_RESERVE_" + System.currentTimeMillis();
    }

    @Override
    public boolean deductStock(String reserveToken) {
        return true;
    }
}