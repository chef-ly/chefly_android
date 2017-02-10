package com.se491.chef_ly.model;

public class Ingredient {
    private IngredientItem item;
    private int qty;

    public Ingredient(IngredientItem item, int qty) {
        this.item = item;
        this.qty = qty;
    }

    public IngredientItem getItem() {
        return item;
    }

    public int getQty() {
        return qty;
    }
}
