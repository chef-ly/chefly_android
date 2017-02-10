package com.se491.chef_ly.model;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {

    private int id;
    private String name;
    private String author;
    private Uri image_url;
    private int rating;
    private int time;
    private String[] categories;
    private String level;

    public Recipe(int id, String name, String author, Uri image_url, int rating,  String[] categories, int time, String level) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.image_url = image_url;
        this.rating = rating;
        this.time = time;
        this.categories = categories;
        this.level = level;
    }
    protected Recipe(Parcel in) {
        this.id=in.readInt();
        this.name = in.readString();
        this.author = in.readString();
        this.image_url=  Uri.parse(in.readString());
        this.rating = in.readInt();
        this.time = in.readInt();
        in.readStringArray(categories);
        this.level = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.author);
        image_url.writeToParcel(dest, flags);
        dest.writeInt(rating);
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public Uri getImage_url() {
        return image_url;
    }

    public int getRating() {
        return rating;
    }

    public int getTotalTime() {
        return time;
    }

    public String[] getCategories() {
        return categories;
    }

    public String getLevel() {
        return level;
    }
}