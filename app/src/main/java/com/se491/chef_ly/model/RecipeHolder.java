package com.se491.chef_ly.model;

import android.content.res.Resources;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a dummy class to simulate making a call to the server
 * to get detailedRecipes.
 * TODO - When the back is running this class and all references
 *        to it should be removed
 */

public class RecipeHolder {
    List<RecipeDetail> detailedRecipes = new ArrayList<>();
    List<Recipe> recipes = new ArrayList<>();
    Resources resources;

    public RecipeHolder(Resources r){
        resources = r;
        createRecipes();

    }

    public List<RecipeDetail> getDetailedRecipes(){
        return detailedRecipes;
    }
    public List<Recipe> getRecipes(){ return recipes; }

    public RecipeDetail getRecipe(String name){

        for(RecipeDetail r : detailedRecipes){
            if(r.getName().equals(name)){
                return r;
            }
        }
        return null;
    }
    private void createRecipes(){

        List<Ingredient> ingred = new ArrayList<>();
        List<String> direc = new ArrayList<>();

        Ingredient pb = new Ingredient(new IngredientItem("Peanut Butter", "tablespoon"), 2);
        Ingredient Jelly = new Ingredient(new IngredientItem("Strawberry Jelly", "tablespoon"), 2);
        Ingredient bread = new Ingredient(new IngredientItem("Rye Bread", "slice"), 2);

        ingred.add(pb);
        ingred.add(Jelly);
        ingred.add(bread);
        direc.add("Spread Peanut Butter on 1 Slice of bread");
        direc.add("Spread Jelly on the other slice of bread");
        direc.add("Place pieces of bread together ");

        //Bitmap image = BitmapFactory.decodeResource(resources, R.drawable.pbj);
        Uri pbjUri = Uri.parse("android.resource://com.se491.chef_ly/drawable/pbj");
        Recipe pbj2 = new Recipe(1,"Peanut Butter and Jelly", "David", pbjUri, 5, 3, new String[]{"snack","lunch","quick","easy"}, "Easy" );
        RecipeDetail pbj = new RecipeDetail(1,"Peanut Butter and Jelly", "David",
                "A classic recipe for a classic dish: the Peanut Butter and Jelly Sandwich.",1,3,
                new String[]{"snack","lunch","quick","easy"},pbjUri, ingred,direc );
        recipes.add(pbj2);
        detailedRecipes.add(pbj);
        List<Ingredient> ingred2 = new ArrayList<>();
        List<String> direc2 = new ArrayList<>();

        Ingredient dough = new Ingredient(new IngredientItem("Pizza Dough", "unk"), 1);
        Ingredient sauce = new Ingredient(new IngredientItem("Pizza Sauce", "oz"), 6);
        Ingredient cheese = new Ingredient(new IngredientItem("Shredded Cheese", "oz"), 18);
        Ingredient pepperoni = new Ingredient(new IngredientItem("Pepperoni slices", "slice"), 50);
        ingred2.add(dough);
        ingred2.add(sauce);
        ingred2.add(cheese);
        ingred2.add(pepperoni);
        direc2.add("Roll out dough");
        direc2.add("Prebake dough @375 for 10 min");
        direc2.add("Add sauce, cheese, and pepperoni");
        direc2.add("Bake @ 375 for 15 min");
       // Bitmap image2 = BitmapFactory.decodeResource(resources, R.drawable.pizza);
        Uri pizzaUri = Uri.parse("android.resource://com.se491.chef_ly/drawable/pizza");
        Recipe pizza2 = new Recipe(2,"Pizza","Abraham Lincoln", pizzaUri,5, 30, new String[]{"quick","easy"}, "Easy");
        RecipeDetail pizza = new RecipeDetail(2,"Pizza","Abraham Lincoln", "A quick and easy pizza recipe", 3, 30,
                new String[]{"quick","easy"},pizzaUri, ingred2,direc2  );
        recipes.add(pizza2);
        detailedRecipes.add(pizza);

        List<Ingredient> ingred3 = new ArrayList<>();
        List<String> direc3 = new ArrayList<>();
        Ingredient a = new Ingredient(new IngredientItem("A","lb"), 1);
        Ingredient b = new Ingredient(new IngredientItem("B","oz"), 12);
        Ingredient c = new Ingredient(new IngredientItem("C","tsp"), 3);
        Ingredient d = new Ingredient(new IngredientItem("D","cup"), 1.5);
        ingred3.add(a);
        ingred3.add(b);
        ingred3.add(c);
        ingred3.add(d);

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
        String author = "George Washington";
        String description = "Its delicious trust me";
        String[] categories = new String[] {"delicious", "President approved", "You'll try it and you WILL like it!"};
        Recipe r2 = new Recipe(3,"Scrambled Eggs",author, uri, 4, 10, categories, "Easy");
        RecipeDetail r = new RecipeDetail(3,"Scrambled Eggs",author,description,2,10,categories,uri, ingred3,direc3  );
        recipes.add(r2);
        detailedRecipes.add(r);
        r2 = new Recipe(4,"Spaghetti",author, uri, 5, 45, categories, "Moderate");
        r = new RecipeDetail(4,"Spaghetti",author,description,5,45,categories,uri, ingred3,direc3 );
        recipes.add(r2);
        detailedRecipes.add(r);
        r2 = new Recipe(5,"Stuffed Peppers",author, uri, 5, 60, categories, "Moderate");
        r = new RecipeDetail(5,"Stuffed Peppers",author,description,4,60,categories,uri, ingred3,direc3);
        recipes.add(r2);
        detailedRecipes.add(r);
        r2 = new Recipe(6,"Meatloaf",author, uri, 2.6, 60, categories, "Moderate");
        r = new RecipeDetail(6,"Meatloaf",author,description,4,60,categories,uri, ingred3,direc3);
        recipes.add(r2);
        detailedRecipes.add(r);
        r2 = new Recipe(7,"Chicken Alfredo",author, uri, 4.2, 60, categories, "Easy");
        r = new RecipeDetail(7,"Chicken Alfredo",author,description,6,60,categories,uri, ingred3,direc3);
        recipes.add(r2);
        detailedRecipes.add(r);
        r2 = new Recipe(8,"Taco Salad",author, uri, 4.9, 30, categories, "Easy");
        r = new RecipeDetail(8,"Taco Salad",author,description,4,30,categories,uri, ingred3,direc3 );
        recipes.add(r2);
        detailedRecipes.add(r);
        r2 = new Recipe(9,"Mediterranean Chicken and Pasta",author, uri, 4.1, 45, categories, "Hard");
        r = new RecipeDetail(9,"Mediterranean Chicken and Pasta",author,description,2,45,categories,uri, ingred3,direc3 );
        recipes.add(r2);
        detailedRecipes.add(r);
        r2 = new Recipe(10,"Cranberry-Apple Pork Roast",author, uri, 3.8, 60, categories, "Moderate");
        r = new RecipeDetail(10,"Cranberry-Apple Pork Roast",author,description,4,60,categories,uri, ingred3,direc3);
        recipes.add(r2);
        detailedRecipes.add(r);




    }

}
