package com.chechu.onthego;

import org.json.JSONException;
import org.json.JSONObject;

class ItemConsumable {
    private int consumibleId;
    private String consumibleName;
    private double consumiblePrice;

    public ItemConsumable(JSONObject object) throws JSONException {
        this.consumibleId = object.getInt("id");
        this.consumibleName = object.getString("nombre");
        this.consumiblePrice = object.getDouble("precio");
    }

    public int getConsumibleId() {
        return consumibleId;
    }

    public String getConsumibleName() {
        return consumibleName;
    }

    public double getConsumiblePrice() {
        return consumiblePrice;
    }
}
