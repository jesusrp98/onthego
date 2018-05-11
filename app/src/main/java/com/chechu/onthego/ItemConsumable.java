package com.chechu.onthego;

import android.graphics.drawable.Drawable;
import org.json.JSONException;
import org.json.JSONObject;

class ItemConsumable {
    private Drawable consumiblePhoto;
    private int consumibleId;
    private String consumibleName;
    private double consumiblePrice;

    public ItemConsumable(Drawable consumiblePhoto, int consumibleId, String consumibleName, double consumiblePrice) {
        this.consumiblePhoto = consumiblePhoto;
        this.consumibleId = consumibleId;
        this.consumibleName = consumibleName;
        this.consumiblePrice = consumiblePrice;
    }

    public ItemConsumable(int consumibleId, String consumibleName) {
        this.consumibleId = consumibleId;
        this.consumibleName = consumibleName;
    }

    public ItemConsumable(JSONObject object) throws JSONException {
        this.consumibleId = object.getInt("id");
        this.consumibleName = object.getString("nombre");
        this.consumiblePrice = object.getDouble("precio");
    }

    public int getConsumibleId() {
        return consumibleId;
    }

    public Drawable getConsumiblePhoto() {
        return consumiblePhoto;
    }

    public String getConsumibleName() {
        return consumibleName;
    }

    public double getConsumiblePrice() {
        return consumiblePrice;
    }
}
