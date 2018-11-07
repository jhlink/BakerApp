package com.example.oliverh.bakerapp.data.database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


import com.example.oliverh.bakerapp.Constants;
import com.squareup.moshi.Json;

import java.util.ArrayList;

@Entity
public class Recipe {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "name")
    @Json(name = "name")
    private String recipeName;

    @ColumnInfo(name = "servings")
    private int servings;

    @ColumnInfo(name = "image")
    @Json(name = "image")
    private String imageURL = Constants.INVALID_URL;

    @Ignore
    private ArrayList<RecipeIngredient> ingredients;

    @Ignore
    private ArrayList<RecipeStep> steps;

    // No parameter constructor used by Moshi to initialize missing fields
    //  within inbound JSON objects.
    //  SRC: https://github.com/square/moshi#default-values--constructors
    @SuppressWarnings("unused")
    public Recipe() {}

    public Recipe(int id, String recipeName, int servings, String imageURL) {
        this.id = id;
        this.recipeName = recipeName;
        this.servings = servings;
        this.imageURL = imageURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int mId) {
        this.id = mId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String mRecipeName) {
        this.recipeName = mRecipeName;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int mServings) {
        this.servings = mServings;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String mImageURL) {
        this.imageURL = mImageURL;
    }

    public ArrayList<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<RecipeStep> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<RecipeStep> steps) {
        this.steps = steps;
    }
}
