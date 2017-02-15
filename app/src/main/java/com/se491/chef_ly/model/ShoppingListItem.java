package com.se491.chef_ly.model;


public class ShoppingListItem {
    private String name;
    private int qty;
    private String unitOfMeasure;
    private boolean purchased;

    public ShoppingListItem(String name, int qty, String unitOfMeasure, boolean purchased) {
        this.name = name;
        this.qty = qty;
        this.unitOfMeasure = unitOfMeasure;
        this.purchased = purchased;
    }

    public String getName() {
        return name;
    }

    public void setPurchased(boolean purchased){
        this.purchased = purchased;
    }
    public int getQty() {
        return qty;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public boolean isPurchased() {
        return purchased;
    }
}
