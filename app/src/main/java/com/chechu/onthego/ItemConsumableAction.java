package com.chechu.onthego;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemConsumableAction extends ItemConsumable {
    private int quantity;
    private int stock;

    public ItemConsumableAction(JSONObject object) throws JSONException {
        super(object);
        this.quantity = 1;
        this.stock = object.getInt("cantidad");
    }

    public long getQuantity() {
        return quantity;
    }

    public int getStock() {
        return stock;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
