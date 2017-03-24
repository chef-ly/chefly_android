package com.se491.chef_ly.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable{
    //private IngredientItem item;
    private final String name;
    private final String uom;
    private final double qty;

   // public Ingredient(IngredientItem item, double qty) {
        //this.item = item;
    public Ingredient(String name, String uom, double qty){
        this.name = name;
        this.uom = uom;
        this.qty = qty;
    }
    private Ingredient(Parcel in){
        //this.item = in.readParcelable(IngredientItem.class.getClassLoader());
        this.name = in.readString();
        this.uom = in.readString();
        this.qty = in.readDouble();
    }

    //public IngredientItem getItem() {return item; }

    public String getName(){
        return this.name;
    }
    public String getUom(){
        return this.uom;
    }

    public double getQty() {
        return qty;
    }

    @Override
    public String toString() {
        return name + " - " + qty + " " + uom;
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
        //dest.writeParcelable(item, flags);
        dest.writeString(name);
        dest.writeString(uom);
        dest.writeDouble(qty);


    }
}
