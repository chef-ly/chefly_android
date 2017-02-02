package com.se491.chef_ly.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class Recipe implements Parcelable{
    private String name;
    private List<String> ingredients;
    private List<String> directions;
    private Uri image;

    Recipe(String name, List<String> ingred, List<String> direc, Uri i){
        this.name = name;
        this.ingredients = ingred;
        this.directions = direc;
        this.image = i;
    }

    Recipe(Parcel in){   //TODO test
        name = in.readString();
        ingredients = in.readArrayList(String.class.getClassLoader());
        directions = in.readArrayList(String.class.getClassLoader());
        image = Uri.parse(in.readString());
    }

    public String getName() {
        return name;
    }

    public Uri getImage() {
        return image;
    }

    public List<String> getDirections() {
        return directions;
    }

    public List<String> getIngredients() {
        return ingredients;
    }




    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>(){
        public Recipe createFromParcel(Parcel in){
            return new Recipe(in);
        }
        public Recipe[] newArray(int size){
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeList(ingredients);
        dest.writeList(directions);
        dest.writeString(image.toString());

    }
}
