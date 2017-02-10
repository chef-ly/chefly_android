package com.se491.chef_ly.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable{
    private IngredientItem item;
    private double qty;

    public Ingredient(IngredientItem item, double qty) {
        this.item = item;
        this.qty = qty;
    }
    public Ingredient(Parcel in){
        this.item = in.readParcelable(IngredientItem.class.getClassLoader());
        this.qty = in.readDouble();
    }

    public IngredientItem getItem() {
        return item;
    }

    public double getQty() {
        return qty;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getName());
        sb.append(" - ");
        sb.append(qty);
        sb.append(" ");
        sb.append(item.getUom());

        return sb.toString();
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(item, flags);
        dest.writeDouble(qty);


    }
}
