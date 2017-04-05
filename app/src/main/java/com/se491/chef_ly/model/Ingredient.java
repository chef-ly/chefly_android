package com.se491.chef_ly.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable{
    private final int id;
    private final String name;
    private final String image;

    public Ingredient(int id, String name, String image){
        this.id = id;
        this.name = name;
        this.image = image;
    }

    protected Ingredient(Parcel in) {
        id = in.readInt();
        name = in.readString();
        image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getName(){
        return this.name;
    }
    public int getId(){
        return this.id;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return name + " - " + id + " " + image;
    }

}
