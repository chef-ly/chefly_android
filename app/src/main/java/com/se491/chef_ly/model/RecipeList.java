package com.se491.chef_ly.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RecipeList implements Parcelable, Iterable<RecipeInformation>{
    private final ArrayList<RecipeInformation> recipes;

    public RecipeList (){
        this.recipes = new ArrayList<>();
    }
    public RecipeList(RecipeInformation[] recipes){
        this.recipes = new ArrayList<>(Arrays.asList(recipes));
    }
    public  RecipeList(ArrayList<RecipeInformation> recipes){
        this.recipes = recipes;
    }

    protected RecipeList(Parcel in) {
        recipes = new ArrayList<>();
        in.readTypedList(recipes, RecipeInformation.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(recipes);
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

    @Override
    public Iterator<RecipeInformation> iterator() {
        return recipes.iterator();
    }


    public ArrayList<RecipeInformation> getRecipes(){
        return recipes;
    }

    public int size(){
        return recipes.size();
    }

    public RecipeInformation get(int position){
        return recipes.get(position);
    }
    public void add(RecipeInformation recipe){
        recipes.add(recipe);
    }
    public int remove(RecipeInformation recipe){
        int res = recipes.indexOf(recipe);
        recipes.remove(recipe);
        return res;
    }
    public int indexOf(RecipeInformation r){
        return recipes.indexOf(r);
    }
    public boolean contains(RecipeInformation recipe){
        return recipes.contains(recipe);
    }
    public void addAll(ArrayList<RecipeInformation> list){
        recipes.addAll(list);
    }
    public void clear(){
        recipes.clear();
    }

}
