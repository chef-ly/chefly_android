package com.se491.chef_ly.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.se491.chef_ly.R;

import java.util.List;


public class RecipeDetail implements Parcelable{
    private String _id;
    private String name;
    private String author;
    private String description;
    private int serves;
    private int time;
    private Level level;
    private String[] categories;
    private String image;
    private Ingredient[] ingredients;
    private String[] directions;

    // Constructor for client to create new Recipe
    public RecipeDetail(String name, String author, String description, int serves, int time, Level level, String[] categories, String image, Ingredient[] ingredients,String[] directions) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.serves = serves;
        this.time = time;
        this.level = level;
        this.categories = categories;
        this.image = image;
        this.ingredients = ingredients;
        this.directions = directions;
        this._id = ""; // TODO get available id from server
    }
    // Constructor for recpie from server


    public RecipeDetail(String id, String name, String author, String description, int serves, int time, Level level, String[] categories, String image, Ingredient[] ingredients, String[] directions) {
        this._id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.serves = serves;
        this.time = time;
        this.level = level;
        this.categories = categories;
        this.image = image;
        this.ingredients = ingredients;
        this.directions = directions;
    }

    RecipeDetail(Parcel in){   //TODO test
        _id = in.readString();
        name = in.readString();
        author = in.readString();
        description = in.readString();
        serves = in.readInt();
        time = in.readInt();
        level = Level.valueOf(in.readString());
        categories = in.createStringArray();
        image = in.readString();

        ingredients = in.createTypedArray(Ingredient.CREATOR);
        directions = in.createStringArray();

    }

    public String getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public int getServes() {
        return serves;
    }

    public int getTime() {
        return time;
    }

    public String[] getCategories() {
        return categories;
    }

    public Uri getImage() {
        if(!(image == null)){
            return Uri.parse(image);
        }else{
            return Uri.EMPTY;
        }

    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public String[] getDirections() {
        return directions;
    }

    public static final Parcelable.Creator<RecipeDetail> CREATOR = new Parcelable.Creator<RecipeDetail>(){
        public RecipeDetail createFromParcel(Parcel in){
            return new RecipeDetail(in);
        }
        public RecipeDetail[] newArray(int size){
            return new RecipeDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(name);
        dest.writeString(author);
        dest.writeString(description);
        dest.writeInt(serves);
        dest.writeInt(time);
        dest.writeString(level.toString());
        dest.writeStringArray(categories);
        dest.writeString(image);
        dest.writeTypedArray(ingredients, 0);
        dest.writeStringArray(directions);


    }
}
