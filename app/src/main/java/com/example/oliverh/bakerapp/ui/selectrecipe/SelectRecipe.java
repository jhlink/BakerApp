package com.example.oliverh.bakerapp.ui.selectrecipe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.oliverh.bakerapp.R;

import timber.log.Timber;

public class SelectRecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_recipe_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_collection_container, SelectRecipeFragment.newInstance())
                    .commitNow();
        }

        Timber.plant(new Timber.DebugTree());
    }
}
