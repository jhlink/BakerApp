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

public class ViewRecipeStep extends AppCompatActivity implements ViewRecipeStepText.OnFragmentInteractionListener {

    private static final int FULLSCREEN_CONTAINER_ID = R.id.full_video_view_screen;

    private boolean landscape_videoFullScreen = false;
    private ViewRecipeStepViewModel mViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recipe_step_activity);


        //  Retrieve passed Activity data
        int recipeId = getIntent().getIntExtra(getString(R.string.BUNDLE_RECIPE_ID), -1);
        int stepId = getIntent().getIntExtra(getString(R.string.BUNDLE_STEP_ID), -1);

        //  Query if orientation = landscape
        landscape_videoFullScreen = findViewById(FULLSCREEN_CONTAINER_ID) != null;

        //  Initialize ViewModel members
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

                // Check if we're in landscape state
                if (!landscape_videoFullScreen) {
                    String recipeStepHeader = String.format("Step %d", recipeStep.getStepIndex() + 1);
                    String recipeDescription = recipeStep.getDescription();
                    handleTextPayload(recipeStepHeader, recipeDescription);
                }

                handleVideoUrl(recipeStep.getVideoUrl());
            }
        });
    }

    private void handleTextPayload(String header, String desc) {
        String nullSafeHeader = header == null ? "" : header;
        String nullSafeDesc = desc == null ? "" : desc;

        Bundle bundle = new Bundle();
        bundle.putString(ViewRecipeStepText.ARG_STEP_HEADER, nullSafeHeader);
        bundle.putString(ViewRecipeStepText.ARG_STEP_DESC, nullSafeDesc);


        ViewRecipeStepText fragment = (ViewRecipeStepText) getSupportFragmentManager().findFragmentById(R.id.recipeStepTextFragment);
        fragment.updateFragmentUI(bundle);
    }


    private void handleVideoUrl(String url) {
        // Check if videoUrl is null
        if (url != null) {
            RecipeVideoFragment fragment = (RecipeVideoFragment) getSupportFragmentManager().findFragmentById(R.id.recipePlayerViewFragment);

            if (landscape_videoFullScreen) {
                fragment = (RecipeVideoFragment) getSupportFragmentManager().findFragmentById(FULLSCREEN_CONTAINER_ID);
                hide();
            }

            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.VIDEO_FRAG_ARGS), url);
            fragment.setArguments(bundle);
            fragment.setAndInitializePlayer(bundle);
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

    }
}
