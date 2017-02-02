package com.se491.chef_ly.model;

import android.content.res.Resources;
import android.net.Uri;

import com.se491.chef_ly.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a dummy class to simulate making a call to the server
 * to get recipes.
 * TODO - When the back is running this class and all references
 *        to it should be removed
 */

public class RecipeHolder {
    List<Recipe> recipes = new ArrayList<>();
    Resources resources;

    public RecipeHolder(Resources r){
        resources = r;
        createRecipes();

    }

    public List<Recipe> getRecipes(){
        return recipes;
    }
    public Recipe getRecipe(String name){

        for(Recipe r : recipes){
            if(r.getName().equals(name)){
                return r;
            }
        }
        return null;
    }
    private void createRecipes(){

        List<String> ingred = new ArrayList<>();
        List<String> direc = new ArrayList<>();

        ingred.add("Peanut Butter");
        ingred.add("Jelly");
        ingred.add("2 Slices Bread");
        direc.add("Spread Peanut Butter on 1 Slice of bread");
        direc.add("Spread Jelly on the other slice of bread");
        direc.add("Place pieces of bread together ");

        //Bitmap image = BitmapFactory.decodeResource(resources, R.drawable.pbj);
        Uri pbjUri = Uri.parse("android.resource://com.se491.chef_ly/drawable/pbj");
        Recipe pbj = new Recipe("Peanut Butter and Jelly", ingred,direc, pbjUri );

        recipes.add(pbj);
        List<String> ingred2 = new ArrayList<>();
        List<String> direc2 = new ArrayList<>();
        ingred2.add("Pizza Dough");
        ingred2.add("Sauce");
        ingred2.add("Cheese");
        ingred2.add("Pepperoni");
        direc2.add("Roll out dough");
        direc2.add("Prebake dough @375 for 10 min");
        direc2.add("Add sauce, cheese, and pepperoni");
        direc2.add("Bake @ 375 for 15 min");
       // Bitmap image2 = BitmapFactory.decodeResource(resources, R.drawable.pizza);
        Uri pizzaUri = Uri.parse("android.resource://com.se491.chef_ly/drawable/pizza");
        Recipe pizza = new Recipe("Pizza", ingred2,direc2, pizzaUri );
        recipes.add(pizza);

        List<String> ingred3 = new ArrayList<>();
        List<String> direc3 = new ArrayList<>();
        ingred3.add("1");
        ingred3.add("2");
        ingred3.add("3");
        ingred3.add("4");
        ingred3.add("5");
        direc3.add("1");
        direc3.add("2");
        direc3.add("3");
        direc3.add("4");
        direc3.add("5");
        direc3.add("Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah..." +
                "Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah..." +
                "Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah..." +
                "Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah..." +
                "Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah..." +
                "Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah..." +
                "Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah..." +
                "Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah..." +
                "Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah..." +
                "Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...Blah...END");
        //Bitmap image3 = BitmapFactory.decodeResource(resources, R.drawable.noimageavailable);
        Uri uri = Uri.parse("android.resource://com.se491.chef_ly/drawable/noimageavailable");
        Recipe r = new Recipe("Scrambled Eggs", ingred3,direc3,uri  );
        recipes.add(r);
        r = new Recipe("Spaghetti", ingred3,direc3, uri );
        recipes.add(r);
        r = new Recipe("Stuffed Peppers", ingred3,direc3, uri );
        recipes.add(r);
        r = new Recipe("Meatloaf", ingred3,direc3, uri );
        recipes.add(r);
        r = new Recipe("Chicken Alfredo", ingred3,direc3, uri );
        recipes.add(r);
        r = new Recipe("Taco Salad", ingred3,direc3, uri );
        recipes.add(r);
        r = new Recipe("Mediterranean Chicken and Pasta", ingred3,direc3, uri );
        recipes.add(r);
        r = new Recipe("Cranberry-Apple Pork Roast", ingred3,direc3, uri );
        recipes.add(r);




    }

}
