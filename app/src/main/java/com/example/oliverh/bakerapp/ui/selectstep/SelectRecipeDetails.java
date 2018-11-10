package com.example.oliverh.bakerapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.oliverh.bakerapp.ui.selectstep.SelectRecipeDetailsFragment;

import timber.log.Timber;

public class SelectRecipeDetails extends AppCompatActivity {

    public static final String RECIPE_DETAILS_FRAGMENT_TAG = "RECIPE_DETAILS_FRAGMENT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_recipe_activity);

        int recipeId = getIntent().getIntExtra(getString(R.string.BUNDLE_RECIPE_ID), -1);

        if (savedInstanceState == null) {

            // TODO: Properly handle this.

            SelectRecipeDetailsFragment fragment = (SelectRecipeDetailsFragment) getSupportFragmentManager().findFragmentByTag(RECIPE_DETAILS_FRAGMENT_TAG);
            if (fragment == null) {
                Timber.d("Create fragment.");

                fragment = SelectRecipeDetailsFragment.newInstance(recipeId);
            } else {
                Timber.d("Found fragment.");
            }

            fragment.setRecipeId(recipeId);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_collection_container,
                            fragment,
                            RECIPE_DETAILS_FRAGMENT_TAG)
                    .commitNow();
        }
    }
}
