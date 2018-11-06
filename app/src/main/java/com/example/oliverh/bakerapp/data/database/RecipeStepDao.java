package com.example.oliverh.bakerapp.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RecipeStepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipeSteps(RecipeStep... recipeSteps);

    @Query("SELECT * FROM RecipeStep WHERE recipe_id =:recipeId")
    LiveData<List<RecipeStep>> getRecipeStepsByRecipeId(int recipeId);
}
