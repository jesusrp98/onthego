package com.chechu.onthego;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

class ItemPurchase {
    private String id;
    private String date;
    private ArrayList<ItemConsumableAction> items;
    private double totalPrice;

    public ItemPurchase() {
        items = new ArrayList<>();
    }

    public ItemPurchase(JSONObject object) throws JSONException {
        this.id = object.getString("id_pago");
        this.date = object.getString("fecha");
        this.totalPrice = object.getDouble("precio_total");
        this.items = new ArrayList<>();

        final JSONArray array = object.getJSONArray("productos");
        for (int i = 0; i < array.length(); ++i)
            items.add(new ItemConsumableAction(array.getJSONObject(i)));
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<ItemConsumableAction> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void addItem(ItemConsumableAction item, int quantity) {
        items.add(item);
        items.get(items.size() - 1).setQuantity(quantity);
        totalPrice += item.getPrice() * quantity;
    }

    public void removeItem(int i) {
        totalPrice -= items.get(i).getPrice() * items.get(i).getQuantity();
        items.remove(i);
    }

    public void editItem(int i, long quantity) {
        totalPrice -= items.get(i).getPrice() * items.get(i).getQuantity();
        items.get(i).setQuantity(quantity);
        totalPrice += items.get(i).getPrice() * items.get(i).getQuantity();
    }

    public String getItemList() {
        String aux = "";
        for (ItemConsumableAction item : items)
            aux += " " + item.getName() + ": " + item.getQuantity() + " unidades.\n";
        return aux;
    }
}
