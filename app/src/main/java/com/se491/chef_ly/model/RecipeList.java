package com.se491.chef_ly.model;

import java.util.ArrayList;

public class RecipeList {
    private ArrayList<Recipe> recipes;

    public RecipeList(){
        recipes = new ArrayList<>();
    }

    public ArrayList<Recipe> getRecipes(){
        return recipes;
    }

}
