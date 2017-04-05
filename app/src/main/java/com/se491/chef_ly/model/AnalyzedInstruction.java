package com.se491.chef_ly.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by davidchang on 4/4/17.
 */

public class AnalyzedInstruction implements Parcelable {
    private final String name;
    private final Step[] steps;

    public AnalyzedInstruction(String name, Step[] steps) {
        this.name = name;
        this.steps = steps;
    }

    protected AnalyzedInstruction(Parcel in) {
        name = in.readString();
        steps = in.createTypedArray(Step.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedArray(steps, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AnalyzedInstruction> CREATOR = new Creator<AnalyzedInstruction>() {
        @Override
        public AnalyzedInstruction createFromParcel(Parcel in) {
            return new AnalyzedInstruction(in);
        }

        @Override
        public AnalyzedInstruction[] newArray(int size) {
            return new AnalyzedInstruction[size];
        }
    };

    public String getName() {
        return name;
    }

    public Step[] getSteps() {
        return steps;
    }

}
