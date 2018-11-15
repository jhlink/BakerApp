package com.example.oliverh.bakerapp.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RecipeIngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipeIngredients(List<RecipeIngredient> ingredients);

    @Query("SELECT * FROM RecipeIngredient WHERE recipe_id =:recipeId")
    LiveData<List<RecipeIngredient>> getRecipeIngredientsById(int recipeId);

    @Query("SELECT * FROM RecipeIngredient WHERE recipe_id =:recipeId")
    List<RecipeIngredient> getRawListOfRecipeIngredientsById(int recipeId);

    @Query("DELETE FROM Recipe")
    void nukeTable();
}
