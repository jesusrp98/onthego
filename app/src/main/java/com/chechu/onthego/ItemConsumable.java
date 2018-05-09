package com.chechu.onthego;

import android.graphics.drawable.Drawable;

class ItemConsumable {
    private Drawable photo;
    private String name;
    private String description;

    public ItemConsumable(String name, String description) {
        this.photo = photo;
        this.name = name;
        this.description = description;
    }

    public Drawable getPhoto() {
        return photo;
    }

    public void setPhoto(Drawable photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
