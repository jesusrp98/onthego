package com.chechu.onthego;

import android.graphics.drawable.Drawable;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemConsumableAction extends ItemConsumable {
    private long quantity;

    public ItemConsumableAction(Drawable consumiblePhoto, int consumibleId, String consumibleName, double consumiblePrice) {
        super(consumiblePhoto, consumibleId, consumibleName, consumiblePrice);
        this.quantity = 0;
    }

    public ItemConsumableAction(JSONObject object) throws JSONException {
        super(object.getInt("id_producto"), object.getString("nombre"));
        this.quantity = object.getLong("cantidad");
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
