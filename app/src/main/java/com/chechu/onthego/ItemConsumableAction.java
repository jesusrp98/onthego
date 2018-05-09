package com.chechu.onthego;

import android.graphics.drawable.Drawable;

public class ItemConsumableAction extends ItemConsumable {
    private long quantity;

    public ItemConsumableAction(Drawable consumiblePhoto, int consumibleId, String consumibleName, double consumiblePrice) {
        super(consumiblePhoto, consumibleId, consumibleName, consumiblePrice);
        this.quantity = 0;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
