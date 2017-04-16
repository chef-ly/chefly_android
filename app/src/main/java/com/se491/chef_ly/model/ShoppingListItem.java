package com.se491.chef_ly.model;


import java.util.Objects;

public class ShoppingListItem {

    private final int id;
    private final String name;
    private boolean purchased;


    public ShoppingListItem(String name , boolean purchased) {
        this.id = -9999;
        this.name = name;
        this.purchased = purchased;
    }
    public ShoppingListItem(int id, String name , boolean purchased) {
        this.id = id;
        this.name = name;
        this.purchased = purchased;
    }

    public String getName() {
        return name;
    }
    public int getId(){
        return id;
    }

    public void setPurchased(boolean purchased){
        this.purchased = purchased;
    }

    public boolean isPurchased() {
        return purchased;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingListItem that = (ShoppingListItem) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
