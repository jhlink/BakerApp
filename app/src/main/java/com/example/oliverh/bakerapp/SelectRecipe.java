package com.example.oliverh.bakerapp;

import android.arch.persistence.room.Database;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.oliverh.bakerapp.data.RecipeRepository;
import com.example.oliverh.bakerapp.data.database.AppDatabase;
import com.example.oliverh.bakerapp.ui.selectrecipe.SelectRecipeFragment;
import com.facebook.stetho.Stetho;

import timber.log.Timber;

public class SelectRecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_recipe_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SelectRecipeFragment.newInstance())
                    .commitNow();
        }

        initializeStetho();

        Timber.plant(new Timber.DebugTree());

        Context context = getApplicationContext();
        AppDatabase mDb = AppDatabase.getInstance(context);
        RecipeRepository.getInstance(context, mDb).getRecipeList();

    }

    private void initializeStetho() {
        Stetho.initializeWithDefaults(this);
    }
}
