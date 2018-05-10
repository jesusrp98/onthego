package com.chechu.onthego;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

class ItemPurchase {
    private int cardinal;
    private String id;
    private String date;
    private long client;
    private ArrayList<ItemConsumableAction> items;
    private double amount;

    public ItemPurchase(int cardinal, JSONObject object) throws JSONException {
        this.cardinal = cardinal;
        this.id = object.getString("id");
        this.date = object.getString("fecha");
        this.client = object.getLong("id_cliente");
        this.amount = object.getDouble("precio_total");
        this.items = new ArrayList<>();

        final JSONArray array = object.getJSONArray("productos");
        for (int i = 0; i < array.length(); ++i) {
            items.add(new ItemConsumableAction(array.getJSONObject(i)));
        }
    }

    public int getCardinal() {
        return cardinal;
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
