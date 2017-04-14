package com.se491.chef_ly.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by davidchang on 4/4/17.
 */

public class Step implements Parcelable{

    private final int number;
    private final String step;
    private final Ingredient[] ingredients;
    private final EquipmentItem[] equipment;

    public Step(String step){
        this.number = 0;
        this.step = step;
        this.ingredients = null;
        this.equipment = null;
    }
    public Step(int number, String step, Ingredient[] ingredients, EquipmentItem[] equipment) {
        this.number = number;
        this.step = step;
        this.ingredients = ingredients;
        this.equipment = equipment;
    }

    protected Step(Parcel in) {
        number = in.readInt();
        step = in.readString();
        ingredients = in.createTypedArray(Ingredient.CREATOR);
        equipment = in.createTypedArray(EquipmentItem.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeString(step);
        dest.writeTypedArray(ingredients, flags);
        dest.writeTypedArray(equipment, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public int getNumber() {
        return number;
    }

    public String getStep() {
        return step;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public EquipmentItem[] getEquipment() {
        return equipment;
    }

}
