package com.chechu.onthego;

import org.json.JSONException;
import org.json.JSONObject;

class ItemConsumable {
    private int id;
    private String name;
    private double price;

    public ItemConsumable(JSONObject object) throws JSONException {
        this.id = object.getInt("id");
        this.name = object.getString("nombre");
        this.price = object.getDouble("precio");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
