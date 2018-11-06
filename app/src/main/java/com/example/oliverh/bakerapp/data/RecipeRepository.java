package com.example.oliverh.bakerapp.data;

import android.graphics.Movie;

import com.example.oliverh.bakerapp.data.database.AppDatabase;
import com.example.oliverh.bakerapp.data.database.RecipeDao;
import com.example.oliverh.bakerapp.data.database.RecipeIngredient;
import com.example.oliverh.bakerapp.data.database.RecipeIngredientDao;
import com.example.oliverh.bakerapp.data.database.RecipeStep;
import com.example.oliverh.bakerapp.data.database.RecipeStepDao;

import java.util.List;

public class RecipeRepository {
    private static RecipeRepository sInstance;
    private final AppDatabase mDb;
    private RecipeDao mRecipeDao;
    private RecipeIngredientDao mRecipeIngredientDao;
    private RecipeStepDao mRecipeStepDao;

    private RecipeRepository(final AppDatabase database) {
        mDb = database;
        mRecipeDao = mDb.recipeDao();
        mRecipeIngredientDao = mDb.recipeIngredientDao();
        mRecipeStepDao = mDb.recipeStepDao();
    }

    public static RecipeRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (RecipeRepository.class) {
                if (sInstance == null) {
                    sInstance = new RecipeRepository(database);
                }
            }
        }
        return sInstance;
    }

}
