package com.se491.chef_ly.model;
import android.os.Parcel;
import android.os.Parcelable;

public class Example implements Parcelable {

    private String itemName;
    private String author;
    private String description;
    private int feeds;
    private int totalTime;
    private String category;


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String author) {
        this.itemName = itemName;
    }

    public String getauthor() {
        return author;
    }

    public void setauthor(String author) {
        this.author = author;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getFeeds() {
        return feeds;
    }

    public void setFeeds(int feeds) {
        this.feeds = feeds;
    }


    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemName);
        dest.writeString(this.author);
        dest.writeString(this.description);
        dest.writeInt(this.feeds);
        dest.writeInt(this.totalTime);
        dest.writeString(this.category);


    }

    public Example() {
    }

    protected Example(Parcel in) {
        this.itemName = in.readString();
        this.author = in.readString();
        this.description = in.readString();
        this.feeds = in.readInt();
        this.totalTime = in.readInt();
        this.category = in.readString();
    }

    public static final Parcelable.Creator<Example> CREATOR = new Parcelable.Creator<Example>() {
        @Override
        public Example createFromParcel(Parcel source) {
            return new Example(source);
        }

        @Override
        public Example[] newArray(int size) {
            return new Example[size];
        }
    };
}