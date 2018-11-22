package com.example.oliverh.bakerapp.ui.viewstep;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.database.RecipeStep;
import com.example.oliverh.bakerapp.data.network.RepositoryResponse;

import timber.log.Timber;

public class ViewRecipeStepHolder extends AppCompatActivity implements ViewRecipeStepTextFragment.OnFragmentInteractionListener {

    private static final int FULLSCREEN_CONTAINER_ID = R.id.fullViewContainer;
    private static final int VIEW_RECIPE_STEP_INGREDIENTS = 0;
    private static final int VIEW_RECIPE_STEP_DETAILS = 1;

    private boolean landscape_videoFullScreen = false;
    private ViewRecipeStepViewModel mViewModel;
    private int recipeId;
    private int stepId;
    private int vsState;
    private boolean isTablet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  Retrieve passed Activity data
        recipeId = getIntent().getIntExtra(getString(R.string.BUNDLE_RECIPE_ID), -1);
        stepId = getIntent().getIntExtra(getString(R.string.BUNDLE_STEP_ID), -1);
        vsState = getIntent().getIntExtra(getString(R.string.BUNDLE_STEP_STATE), 0);
        isTablet = getIntent().getBooleanExtra(ViewRecipeStepTextFragment.ARG_IS_NEXT_BTN_VISIBLE, false);

        switch ( vsState ) {
            case VIEW_RECIPE_STEP_INGREDIENTS:
                setContentView(R.layout.recipe_step_ingredient_list);
                populateIngredientsLayout();
                break;

            case VIEW_RECIPE_STEP_DETAILS:
            default:
                setContentView(R.layout.view_recipe_step_activity);
                populateDetailsLayout();
        }
    }

    private void populateIngredientsLayout() {
        ViewIngredientsFragment viewIngredientsFragment = ViewIngredientsFragment.newInstance(recipeId);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ingredient_collection_container, viewIngredientsFragment)
                .commit();
    }

    private void populateDetailsLayout() {
        //  Query if orientation = landscape
        landscape_videoFullScreen = findViewById(FULLSCREEN_CONTAINER_ID) != null;

        if (mViewModel == null) {
            //  Initialize ViewModel members
            Timber.d("Initialize View Model");
            ViewRecipeStepViewModelFactory factory = new ViewRecipeStepViewModelFactory(recipeId, stepId);
            mViewModel = ViewModelProviders.of(this, factory).get(ViewRecipeStepViewModel.class);

            mViewModel.getRecipeStep().observe(this, new Observer<RepositoryResponse>() {
                @Override
                public void onChanged(@Nullable RepositoryResponse repositoryResponse) {
                    if (repositoryResponse.getError() != null) {
                        Timber.d(repositoryResponse.getError());
                        return;
                    }

                    RecipeStep recipeStep = (RecipeStep) repositoryResponse.getObject();
                    Timber.d("Selected Recipe Step %s", recipeStep.toString());

                    // Check if we're in landscape state
                    if (!landscape_videoFullScreen) {
                        int recipeStepIndex = recipeStep.getStepIndex();
                        String recipeDescription = recipeStep.getDescription();
                        handleTextPayload(recipeStepIndex, recipeDescription);
                    }

                    handleVideoUrl(recipeStep.getVideoUrl());
                }
            });
        } else {
            Timber.d("View Model already established");
        }
    }

    private void handleTextPayload(int stepIndex, String desc) {
        String nullSafeHeader = stepIndex == 0 ? desc : String.valueOf(stepIndex);
        String nullSafeDesc = desc == null || stepIndex == 0 ? "" : desc;

        Bundle bundle = new Bundle();
        bundle.putString(ViewRecipeStepTextFragment.ARG_STEP_HEADER, nullSafeHeader);
        bundle.putString(ViewRecipeStepTextFragment.ARG_STEP_DESC, nullSafeDesc);
        bundle.putBoolean(ViewRecipeStepTextFragment.ARG_IS_NEXT_BTN_VISIBLE, isTablet);

        ViewRecipeStepTextFragment fragment = (ViewRecipeStepTextFragment) getSupportFragmentManager().findFragmentById(R.id.recipeStepTextFragment);
        fragment.updateFragmentUI(bundle);
    }

    private void handleVideoUrl(String url) {
        // Check if videoUrl is null
        if (url != null) {
            int containerId = R.id.recipePlayerViewFragment;
            RecipeVideoFragment fragment = (RecipeVideoFragment) getSupportFragmentManager().findFragmentById(containerId);

            if (landscape_videoFullScreen) {
                hide();
            } else if (fragment == null) {
                fragment = RecipeVideoFragment.newInstance(url);
                Timber.d("New Fragment w/ %s", url);
            } else {
                Timber.d("Found Portrait Fragment");
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.VIDEO_FRAG_ARGS), url);
                fragment.setAndInitializePlayer(bundle);
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(containerId, fragment)
                    .commit();
        }
    }

    private void hide() {
        if (findViewById(FULLSCREEN_CONTAINER_ID) == null) {
            return;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        findViewById(FULLSCREEN_CONTAINER_ID).setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void OnNextStepFragmentInteraction() {
        stepId++;
        mViewModel.queryRecipe(recipeId, stepId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.BUNDLE_STEP_ID), stepId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            stepId = savedInstanceState.getInt(getString(R.string.BUNDLE_STEP_ID), 0);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}
