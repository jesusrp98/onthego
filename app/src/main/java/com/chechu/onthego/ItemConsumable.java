package com.chechu.onthego;

import android.graphics.drawable.Drawable;

class ItemConsumable {
    private Drawable consumiblePhoto;
    private long consumibleId;
    private String consumibleName;
    private double consumiblePrice;

    public ItemConsumable(Drawable consumiblePhoto, int consumibleId, String consumibleName, double consumiblePrice) {
        this.consumiblePhoto = consumiblePhoto;
        this.consumibleId = consumibleId;
        this.consumibleName = consumibleName;
        this.consumiblePrice = consumiblePrice;
    }

    public long getConsumibleId() {
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
