package com.se491.chef_ly.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.se491.chef_ly.model.RecipeDetail;

import java.lang.reflect.Type;

public class RecipeDetailSerializer implements JsonSerializer<RecipeDetail> {

    public RecipeDetailSerializer(){
        super();
    }
    @Override
    public JsonElement serialize(RecipeDetail src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObj = new JsonObject();
        //jsonObj.add("_id", context.serialize(src.getId()));
        jsonObj.add("name", context.serialize(src.getName()));
        jsonObj.add("author", context.serialize(src.getAuthor()));
        jsonObj.add("description", context.serialize(src.getDescription()));
        jsonObj.add("serves", context.serialize(src.getServes()));
        jsonObj.add("time", context.serialize(src.getTime()));
        jsonObj.add("level", context.serialize(src.getLevel().toString()));
        jsonObj.add("categories", context.serialize(src.getCategories()));
        jsonObj.add("image", context.serialize(src.getImage().toString()));
        jsonObj.add("ingredients", context.serialize(src.getIngredients()));
        jsonObj.add("instructions", context.serialize(src.getDirections()));

        return jsonObj;
    }
}
