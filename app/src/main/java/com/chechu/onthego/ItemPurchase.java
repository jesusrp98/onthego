package com.chechu.onthego;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

class ItemPurchase {
    private int cardinal;
    private String id;
    private String date;
    private ArrayList<ItemConsumableAction> items;
    private double amount;

    public ItemPurchase(int cardinal, JSONObject object) throws JSONException {
        this.cardinal = cardinal;
        this.id = object.getString("id_pago");
        this.date = object.getString("fecha");
        this.amount = object.getDouble("precio_total");
        this.items = new ArrayList<>();

        final JSONArray array = object.getJSONArray("productos");
        for (int i = 0; i < array.length(); ++i)
            items.add(new ItemConsumableAction(array.getJSONObject(i)));
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

    public String getItemList() {
        String aux = "";
        for (ItemConsumableAction item : items)
            aux += " " + item.getConsumibleName() + ": " + item.getQuantity() + " unidades.\n";
        return aux;
    }

    public double getAmount() {
        return amount;
    }
}
