package com.se491.chef_ly.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeList implements Parcelable{
    private final RecipeInformation[] recipes;

    public RecipeList(RecipeInformation[] recipes){
        this.recipes = recipes;
    }

    protected RecipeList(Parcel in) {
        recipes = in.createTypedArray(RecipeInformation.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(recipes, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecipeList> CREATOR = new Creator<RecipeList>() {
        @Override
        public RecipeList createFromParcel(Parcel in) {
            return new RecipeList(in);
        }

        @Override
        public RecipeList[] newArray(int size) {
            return new RecipeList[size];
        }
    };

    public RecipeInformation[] getRecipes(){
        return recipes;
    }

}
