package com.se491.chef_ly.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by davidchang on 4/4/17.
 */

public class ExtendedIngredient implements Parcelable{

    private final int id;
    private final String image;
    private final String name;
    private final float amount;
    private final String unit;
    private final String unitShort;
    private final String unitLong;
    private final String originalString;

    public ExtendedIngredient(int id, String image, String name, float amount, String unit, String unitShort, String unitLong, String originalString) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.unitShort = unitShort;
        this.unitLong = unitLong;
        this.originalString = originalString;
    }

    protected ExtendedIngredient(Parcel in) {
        id = in.readInt();
        image = in.readString();
        name = in.readString();
        amount = in.readFloat();
        unit = in.readString();
        unitShort = in.readString();
        unitLong = in.readString();
        originalString = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(image);
        dest.writeString(name);
        dest.writeFloat(amount);
        dest.writeString(unit);
        dest.writeString(unitShort);
        dest.writeString(unitLong);
        dest.writeString(originalString);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ExtendedIngredient> CREATOR = new Creator<ExtendedIngredient>() {
        @Override
        public ExtendedIngredient createFromParcel(Parcel in) {
            return new ExtendedIngredient(in);
        }

        @Override
        public ExtendedIngredient[] newArray(int size) {
            return new ExtendedIngredient[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public float getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }

    public String getOriginalString() {
        return originalString;
    }

}
