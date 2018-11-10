package com.example.oliverh.bakerapp.ui.viewstep;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.oliverh.bakerapp.R;

import timber.log.Timber;

public class ViewRecipeStep extends AppCompatActivity {

    public static final String VIEW_RECIPE_STEP_FRAGMENT_TAG = "RECIPE_DETAILS_FRAGMENT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recipe_step_activity);

        int recipeId = getIntent().getIntExtra(getString(R.string.BUNDLE_RECIPE_ID), -1);
        int stepId = getIntent().getIntExtra(getString(R.string.BUNDLE_STEP_ID), -1);

        if (savedInstanceState == null) {
            ViewRecipeStepFragment fragment = (ViewRecipeStepFragment) getSupportFragmentManager().findFragmentByTag(VIEW_RECIPE_STEP_FRAGMENT_TAG);
            if (fragment == null) {
                Timber.d("Create fragment.");
                fragment = ViewRecipeStepFragment.newInstance(recipeId, stepId);
            } else {
                Timber.d("Found fragment.");
            }

            fragment.setRecipeId(recipeId);
            fragment.setStepId(stepId);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_collection_container,
                            fragment,
                            VIEW_RECIPE_STEP_FRAGMENT_TAG)
                    .commitNow();
        }
    }
}
