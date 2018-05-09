package com.chechu.onthego;

import java.util.ArrayList;

class ItemPurchase {
    private String id;
    private String date;
    private long client;
    private ArrayList<ItemConsumableAction> items;
    private double amount;

    public ItemPurchase(String id, String date, long client, ArrayList<ItemConsumableAction> items, double amount) {
        this.id = id;
        this.date = date;
        this.client = client;
        this.items = items;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }


    public String getDate() {
        return date;
    }

    public long getClient() {
        return client;
    }

    public ArrayList<ItemConsumableAction> getItems() {
        return items;
    }

    public double getAmount() {
        return amount;
    }
}
