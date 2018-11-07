package com.example.oliverh.bakerapp.data.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.squareup.moshi.Json;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Recipe.class,
        parentColumns = "id",
        childColumns = "recipe_id",
        onDelete = CASCADE))
public class RecipeStep {
    //  The following two properties need clarification.
    //      recipeStepId or recipe_step_id is the autogenerated "UNIQUE" PrimaryKey
    //      required by Room.
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recipe_step_id")
    private int recipeStepId;

    //  stepIndex or recipe_step_index refers to the instruction step index.
    //      The step ids found in the raw JSON file imply that the order of the recipe instructions
    //      and the id progression are the same.
    @ColumnInfo(name = "recipe_step_index")
    @Json(name = "id")
    private int stepIndex;

    @ColumnInfo(name = "recipe_id")
    private transient int recipeId;

    @ColumnInfo(name = "short_description")
    private String shortDescription;

    @ColumnInfo(name = "description")
    private int description;

    @ColumnInfo(name = "video_url")
    @Json(name = "videoURL")
    private String videoUrl;

    @ColumnInfo(name = "thumbnail_url")
    @Json(name = "thumbnailURL")
    private String thumbnailUrl;


    public RecipeStep(int recipeStepId, int stepIndex, int recipeId, String shortDescription, int description, String videoUrl, String thumbnailUrl) {
        this.recipeStepId = recipeStepId;
        this.stepIndex = stepIndex;
        this.recipeId = recipeId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getRecipeStepId() {
        return recipeStepId;
    }

    public void setRecipeStepId(int recipeStepId) {
        this.recipeStepId = recipeStepId;
    }

    public int getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
