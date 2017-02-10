package com.se491.chef_ly.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class RecipeDetail implements Parcelable{
    private int id;
    private String name;
    private String author;
    private String description;
    private int serves;
    private int time;
    private String[] categories;
    private Uri image;
    private List<Ingredient> ingredients;
    private List<String> directions;

    // Constructor for client to create new Recipe
    public RecipeDetail(String name, String author, String description, int serves, int time, String[] categories, Uri image, List<Ingredient> ingredients, List<String> directions) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.serves = serves;
        this.time = time;
        this.categories = categories;
        this.image = image;
        this.ingredients = ingredients;
        this.directions = directions;
        this.id = 0; // TODO get available id from server
    }
    // Constructor for recpie from server


    public RecipeDetail(int id, String name, String author, String description, int serves, int time, String[] categories, Uri image, List<Ingredient> ingredients, List<String> directions) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.serves = serves;
        this.time = time;
        this.categories = categories;
        this.image = image;
        this.ingredients = ingredients;
        this.directions = directions;
    }

    RecipeDetail(Parcel in){   //TODO test
        id = in.readInt();
        name = in.readString();
        author = in.readString();
        description = in.readString();
        serves = in.readInt();
        time = in.readInt();
        categories = in.createStringArray();
        image = Uri.parse(in.readString());
        ingredients = in.readArrayList(Ingredient.class.getClassLoader());
        directions = in.readArrayList(String.class.getClassLoader());

    }

    public int getId() {
        return id;
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
        return image;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<String> getDirections() {
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
        dest.writeInt(id);
        dest.writeString(this.name);
        dest.writeString(this.author);
        dest.writeString(this.description);
        dest.writeInt(this.serves);
        dest.writeInt(this.time);
        dest.writeStringArray(categories);
        dest.writeString(image.toString());
        dest.writeList(ingredients);
        dest.writeList(directions);


    }
}
