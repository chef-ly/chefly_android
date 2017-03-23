package com.se491.chef_ly.model;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Recipe implements Parcelable {

    private String _id;
    private String name;
    private String author;
    private String image_url;
    private double rating;
    private int time;
    private String[] categories;
    private String level;

    public Recipe(String name, String author, String image_url, double rating, int time, String[] categories, String level) {
        this.name = name;
        this.author = author;
        this.image_url = image_url;
        this.rating = rating;
        this.time = time;
        this.categories = categories;
        this.level = level;
    }

    public Recipe(String id, String name, String author, String image_url, double rating, int time, String[] categories, String level) {
        this._id = id;
        this.name = name;
        this.author = author;
        this.image_url = image_url;
        this.rating = rating;
        this.time = time;
        this.categories = categories;
        this.level = level;
    }
    protected Recipe(Parcel in) {
        this._id=in.readString();
        this.name = in.readString();
        this.author = in.readString();
        this.image_url=  in.readString();
        this.rating = in.readDouble();
        this.time = in.readInt();
        categories = in.createStringArray();
        this.level = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.name);
        dest.writeString(this.author);
        dest.writeString(image_url);
        dest.writeDouble(rating);
        dest.writeInt(this.time);
        dest.writeStringArray(categories);
        dest.writeSerializable(level);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public Uri getImage() {
        if(image_url != null){
            return Uri.parse(image_url);
        }else{
            return Uri.EMPTY;
        }

    }

    public double getRating() {
        return rating;
    }

    public int getTime() {
        return time;
    }

    public String[] getCategories() {
        return categories;
    }

    public String getLevel() {
        return level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        if (_id != null ? !_id.equals(recipe._id) : recipe._id != null) return false;
        if (name != null ? !name.equals(recipe.name) : recipe.name != null) return false;
        if (author != null ? !author.equals(recipe.author) : recipe.author != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = _id != null ? _id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (image_url != null ? image_url.hashCode() : 0);
        temp = Double.doubleToLongBits(rating);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + time;
        result = 31 * result + Arrays.hashCode(categories);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        return result;
    }
}