package com.se491.chef_ly.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class IngredientItem implements Parcelable{
    private final String name;
    private final String uom;
    private HashMap<String, Integer> nutrition;

    public IngredientItem(String name, String uom) {
        this.name = name;
        this.uom = uom;
    }
    private IngredientItem(Parcel in){
        this.name = in.readString();
        this.uom = in.readString();
    }

    public IngredientItem(String name, String uom, HashMap<String, Integer> nutrition) {
        this.name = name;
        this.uom = uom;
        this.nutrition = nutrition;
    }

    public String getName() {
        return name;
    }

    public String getUom() {
        return uom;
    }

    public HashMap<String, Integer> getNutrition() {
        return nutrition;
    }

    public static final Parcelable.Creator<IngredientItem> CREATOR = new Parcelable.Creator<IngredientItem>() {
        @Override
        public IngredientItem createFromParcel(Parcel source) {
            return new IngredientItem(source);
        }

        @Override
        public IngredientItem[] newArray(int size) {
            return new IngredientItem[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(uom);
    }
}
