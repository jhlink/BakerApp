package com.example.oliverh.bakerapp.data.database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Recipe {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "name")
    private String mRecipeName;

    @ColumnInfo(name = "servings")
    private int mServings;

    @ColumnInfo(name = "image")
    private String mImageURL;

    public Recipe(int mId, String mRecipeName, int mServings, String mImageURL) {
        this.mId = mId;
        this.mRecipeName = mRecipeName;
        this.mServings = mServings;
        this.mImageURL = mImageURL;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getRecipeName() {
        return mRecipeName;
    }

    public void setRecipeName(String mRecipeName) {
        this.mRecipeName = mRecipeName;
    }

    public int getServings() {
        return mServings;
    }

    public void setServings(int mServings) {
        this.mServings = mServings;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setImageURL(String mImageURL) {
        this.mImageURL = mImageURL;
    }


}
