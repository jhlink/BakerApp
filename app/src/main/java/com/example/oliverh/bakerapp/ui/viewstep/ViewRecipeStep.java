package com.example.oliverh.bakerapp.ui.viewstep;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.oliverh.bakerapp.R;

import timber.log.Timber;

public class ViewRecipeStep extends AppCompatActivity {

    public static final String VIEW_RECIPE_STEP_FRAGMENT_TAG = "RECIPE_DETAILS_FRAGMENT";
    private static final int LAND_TABLET_RECIPE_COLLECTION_CONTAINER_ID = R.id.land_recipe_collection_container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_recipe_activity);

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
        } else {
            if (this.findViewById(LAND_TABLET_RECIPE_COLLECTION_CONTAINER_ID) != null) {
                ViewRecipeStepFragment fragment = ViewRecipeStepFragment.newInstance(recipeId, stepId);
                getSupportFragmentManager().beginTransaction()
                        .replace(LAND_TABLET_RECIPE_COLLECTION_CONTAINER_ID,
                                fragment,
                                VIEW_RECIPE_STEP_FRAGMENT_TAG)
                        .commitNow();
            }
        }
    }
}
