package com.se491.chef_ly.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by davidchang on 4/4/17.
 */

public class RecipeInformation implements Parcelable {
    private int id;
    private String title;
    private int readyInMinutes;
    private String image;
    private String instructions;
    private AnalyzedInstruction[] analyzedInstructions;
    private ExtendedIngredient[] extendedIngredients;
    private int preparationMinutes;
    private int cookingMinutes;
    private String sourceUrl;
    private String spoonacularSourceUrl;
    private String creditText;
    private int aggregateLikes;
    private int spoonacularScore;
    private int servings;
    private boolean favorite;


    public RecipeInformation(int id, String title, String image, int servings) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.servings = servings;
        this.favorite = false;
    }

    public RecipeInformation(int id, String title, int readyInMinutes, String image, String instructions,
                             AnalyzedInstruction[] analyzedInstructions, ExtendedIngredient[] extendedIngredients,
                             int preparationMinutes, int cookingMinutes, String sourceUrl, String spoonacularSourceUrl,
                             String creditText, int aggregateLikes, int spoonacularScore, int servings) {
        this.id = id;
        this.title = title;
        this.readyInMinutes = readyInMinutes;
        this.image = image;
        this.instructions = instructions;
        this.analyzedInstructions = analyzedInstructions;
        this.extendedIngredients = extendedIngredients;
        this.preparationMinutes = preparationMinutes;
        this.cookingMinutes = cookingMinutes;
        this.sourceUrl = sourceUrl;
        this.spoonacularSourceUrl = spoonacularSourceUrl;
        this.creditText = creditText;
        this.aggregateLikes = aggregateLikes;
        this.spoonacularScore = spoonacularScore;
        this.servings = servings;
        this.favorite = false;
    }

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
        favorite = in.readInt() != 0 ;
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
        dest.writeInt(favorite ? 1 : 0);
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

    public boolean isFavorite(){
        return favorite;
    }
    public void setFavorite(boolean fav){
        this.favorite = fav;
    }
}
