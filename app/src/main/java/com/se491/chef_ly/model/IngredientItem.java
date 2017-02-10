package com.se491.chef_ly.model;

import java.util.HashMap;

public class IngredientItem {
    private String name;
    private String uom;
    private HashMap<String, Integer> nutrition;

    public IngredientItem(String name, String uom) {
        this.name = name;
        this.uom = uom;
    }

    public IngredientItem(String name, String uom, HashMap<String, Integer> nutrition) {
        this.name = name;
        this.uom = uom;
        this.nutrition = nutrition;
    }

    public String getName() {
        return name;
    }

    public String getUom() {
        return uom;
    }

    public HashMap<String, Integer> getNutrition() {
        return nutrition;
    }
}
