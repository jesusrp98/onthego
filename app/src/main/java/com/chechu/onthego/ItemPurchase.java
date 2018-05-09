package com.chechu.onthego;

public class ItemPurchase {
     private int number;
    private String date;
    private String state;
    private String id;

    public ItemPurchase(int number, String date, String state, String id) {
        this.number = number;
        this.date = date;
        this.state = state;
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}
