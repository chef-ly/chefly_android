package com.se491.chef_ly.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.se491.chef_ly.model.RecipeInformation;

import java.lang.reflect.Type;

public class RecipeDetailSerializer implements JsonSerializer<RecipeInformation> {

    public RecipeDetailSerializer(){
        super();
    }
    @Override
    public JsonElement serialize(RecipeInformation src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObj = new JsonObject();
        //jsonObj.add("_id", context.serialize(src.getId()));
        jsonObj.add("name", context.serialize(src.getTitle()));
        jsonObj.add("author", context.serialize(src.getCreditText()));
        jsonObj.add("serves", context.serialize(src.getServings()));
        jsonObj.add("time", context.serialize(src.getReadyInMinutes()));
        jsonObj.add("image", context.serialize(src.getImage()));
        jsonObj.add("ingredients", context.serialize(src.getExtendedIngredients()));
        jsonObj.add("instructions", context.serialize(src.getInstructions()));

        return jsonObj;
    }
}
