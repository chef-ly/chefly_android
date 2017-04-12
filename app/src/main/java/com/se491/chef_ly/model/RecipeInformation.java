package com.se491.chef_ly.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by davidchang on 4/4/17.
 */

public class RecipeInformation implements Parcelable {
    private final int id;
    private final String title;
    private final int readyInMinutes;
    private final String image;
    private final String instructions;
    private final AnalyzedInstruction[] analyzedInstructions;
    private final ExtendedIngredient[] extendedIngredients;
    private final int preparationMinutes;
    private final int cookingMinutes;
    private final String sourceUrl;
    private final String spoonacularSourceUrl;
    private final String creditText;
    private final int aggregateLikes;
    private final int spoonacularScore;
    private final int servings;

    protected RecipeInformation(Parcel in) {
        id = in.readInt();
        title = in.readString();
        readyInMinutes = in.readInt();
        image = in.readString();
        instructions = in.readString();
        analyzedInstructions = in.createTypedArray(AnalyzedInstruction.CREATOR);
        extendedIngredients = in.createTypedArray(ExtendedIngredient.CREATOR);
        preparationMinutes = in.readInt();
        cookingMinutes = in.readInt();
        sourceUrl = in.readString();
        spoonacularSourceUrl = in.readString();
        creditText = in.readString();
        aggregateLikes = in.readInt();
        spoonacularScore = in.readInt();
        servings = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(readyInMinutes);
        dest.writeString(image);
        dest.writeString(instructions);
        dest.writeTypedArray(analyzedInstructions, flags);
        dest.writeTypedArray(extendedIngredients, flags);
        dest.writeInt(preparationMinutes);
        dest.writeInt(cookingMinutes);
        dest.writeString(sourceUrl);
        dest.writeString(spoonacularSourceUrl);
        dest.writeString(creditText);
        dest.writeInt(aggregateLikes);
        dest.writeInt(spoonacularScore);
        dest.writeInt(servings);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecipeInformation> CREATOR = new Creator<RecipeInformation>() {
        @Override
        public RecipeInformation createFromParcel(Parcel in) {
            return new RecipeInformation(in);
        }

        @Override
        public RecipeInformation[] newArray(int size) {
            return new RecipeInformation[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public String getImage() {
        return image;
    }

    public String getInstructions() {
        return instructions;
    }

    public AnalyzedInstruction[] getAnalyzedInstructions() {
        return analyzedInstructions;
    }

    public ExtendedIngredient[] getExtendedIngredients() {
        return extendedIngredients;
    }

    public int getPreparationMinutes() {
        return preparationMinutes;
    }

    public int getCookingMinutes() {
        return cookingMinutes;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getSpoonacularSourceUrl() {
        return spoonacularSourceUrl;
    }

    public String getCreditText() {
        return creditText;
    }

    public int getAggregateLikes() {
        return aggregateLikes;
    }

    public int getSpoonacularScore() {
        return spoonacularScore;
    }

    public int getServings() {
        return servings;
    }
}
