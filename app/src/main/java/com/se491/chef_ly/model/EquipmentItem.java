package com.se491.chef_ly.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by davidchang on 4/4/17.
 */

public class EquipmentItem implements Parcelable {
    private final int id;
    private final String name;
    private final String image;

    public EquipmentItem(int id, String name, String image){
        this.id = id;
        this.name = name;
        this.image = image;
    }

    private EquipmentItem(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
        this.image = in.readString();
    }

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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EquipmentItem> CREATOR = new Creator<EquipmentItem>() {
        @Override
        public EquipmentItem createFromParcel(Parcel in) {
            return new EquipmentItem(in);
        }

        @Override
        public EquipmentItem[] newArray(int size) {
            return new EquipmentItem[size];
        }
    };
}
