package com.example.oliverh.bakerapp.data.database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Recipe {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "name")
    private String recipeName;

    @ColumnInfo(name = "servings")
    private int servings;

    @ColumnInfo(name = "image")
    private String imageURL;

    public Recipe(int mId, String mRecipeName, int mServings, String mImageURL) {
        this.id = mId;
        this.recipeName = mRecipeName;
        this.servings = mServings;
        this.imageURL = mImageURL;
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


}
