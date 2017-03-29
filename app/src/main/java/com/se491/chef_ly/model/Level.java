package com.se491.chef_ly.model;

import com.google.gson.annotations.SerializedName;

public enum Level {
    @SerializedName(value = "easy", alternate = {"Easy", "EASY"})
    EASY,
    @SerializedName(value = "medium", alternate = {"Medium", "MEDIUM"})
    MEDIUM,
    @SerializedName(value = "hard", alternate = {"Hard", "HARD"})
    HARD;

    @Override
    public String toString() {
       switch(this){
           case EASY:
               return "Easy";
           case MEDIUM:
               return "Medium";
           case HARD:
               return "Hard";
       }
       throw new IllegalArgumentException();
    }
}
