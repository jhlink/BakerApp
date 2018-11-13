package com.example.oliverh.bakerapp.ui.selectstep;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.database.RecipeStep;
import com.example.oliverh.bakerapp.data.network.RepositoryResponse;
import com.example.oliverh.bakerapp.ui.viewstep.RecipeVideoFragment;
import com.example.oliverh.bakerapp.ui.viewstep.ViewRecipeStepHolder;
import com.example.oliverh.bakerapp.ui.viewstep.ViewRecipeStepTextFragment;
import com.example.oliverh.bakerapp.ui.viewstep.ViewRecipeStepViewModel;
import com.example.oliverh.bakerapp.ui.viewstep.ViewRecipeStepViewModelFactory;

import timber.log.Timber;

public class SelectRecipeDetails extends AppCompatActivity
        implements SelectRecipeDetailsFragment.OnDetailInteractionListener,
        ViewRecipeStepTextFragment.OnFragmentInteractionListener {

    public static final String RECIPE_DETAILS_FRAGMENT_TAG = "RECIPE_DETAILS_FRAGMENT";
    private static final int LAND_RECIPE_DETAILS_COLLECTION_CONTAINER_ID = R.id.land_recipe_detail_collection_container;
    private static final int RECIPE_DETAIL_COLLECTION_CONTAINER_ID = R.id.recipe_detail_collection_container;

    //  TODO: Implement onRecipeDetail selection callback -> Pass recipeId / stepId to
    //      RecipeStep and Video fragment collection container id
    private static final int TABLET_RECIPE_DETAILS_COLLECTION_CONTAINER_ID = R.id.tbl_recipeDetailsListFrag;
    private static final int TABLET_RECIPE_STEP_TEXT_COLLECTION_CONTAINER_ID = R.id.tbl_recipeStepTextFrag;
    private static final int TABLET_RECIPE_STEP_VIDEO_COLLECTION_CONTAINER_ID = R.id.tbl_recipeStepVideoFrag;
    private int recipeId;
    private boolean isTablet = false;
    private ViewRecipeStepViewModel viewRecipeStepViewModel;
    private int stepId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.master_recipe_step_list);

        recipeId = getIntent().getIntExtra(getString(R.string.BUNDLE_RECIPE_ID), -1);
        isTablet = this.findViewById(TABLET_RECIPE_DETAILS_COLLECTION_CONTAINER_ID) != null;

        Timber.d("BackStack Count %d ", getSupportFragmentManager().getBackStackEntryCount());

        if (isTablet) {
            if (savedInstanceState == null) {
                Timber.d("We are in Tablet mode w/ id: %d.", recipeId);
                // Handle Master List Frag
                SelectRecipeDetailsFragment masterListFrag = (SelectRecipeDetailsFragment) getSupportFragmentManager().findFragmentById(TABLET_RECIPE_DETAILS_COLLECTION_CONTAINER_ID);

                if (masterListFrag == null) {
                    Timber.d("Create fragment.");
                    masterListFrag = SelectRecipeDetailsFragment.newInstance(recipeId);
                } else {
                    Timber.d("Found fragment.");
                    Bundle masterListFragBundle = new Bundle();
                    masterListFragBundle.putInt(SelectRecipeDetailsFragment.ARG_RECIPE_ID, recipeId);
                    masterListFrag.setRecipeId(recipeId);
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(TABLET_RECIPE_DETAILS_COLLECTION_CONTAINER_ID,
                                masterListFrag,
                                RECIPE_DETAILS_FRAGMENT_TAG)
                        .commitNow();
            }

        } else {
            if (savedInstanceState == null) {
                SelectRecipeDetailsFragment fragment = (SelectRecipeDetailsFragment) getSupportFragmentManager().findFragmentByTag(RECIPE_DETAILS_FRAGMENT_TAG);
                if (fragment == null) {
                    Timber.d("Create fragment.");

                    fragment = SelectRecipeDetailsFragment.newInstance(recipeId);
                } else {
                    Timber.d("Found fragment.");
                }

                fragment.setRecipeId(recipeId);
                getSupportFragmentManager().beginTransaction()
                        .add(RECIPE_DETAIL_COLLECTION_CONTAINER_ID,
                                fragment,
                                RECIPE_DETAILS_FRAGMENT_TAG)
                        .commitNow();
            } else {
                if (this.findViewById(LAND_RECIPE_DETAILS_COLLECTION_CONTAINER_ID) != null) {
                    SelectRecipeDetailsFragment fragment = SelectRecipeDetailsFragment.newInstance(recipeId);
                    getSupportFragmentManager().beginTransaction()
                            .replace(LAND_RECIPE_DETAILS_COLLECTION_CONTAINER_ID,
                                    fragment,
                                    RECIPE_DETAILS_FRAGMENT_TAG)
                            .commitNow();
                }
            }
        }
    }

    @Override
    public void OnNextStepFragmentInteraction() {
        stepId++;
        viewRecipeStepViewModel.queryRecipe(recipeId, stepId);
    }

    @Override
    public void onDetailInteractionListener(int position) {
        stepId = position - 1;
        String recipeIdBundleTag = getString(R.string.BUNDLE_RECIPE_ID);
        String stepIdBundleTag = getString(R.string.BUNDLE_STEP_ID);
        String viewStepState = getString(R.string.BUNDLE_STEP_STATE);

        if (isTablet) {
            if (position == 0) {
                Toast.makeText(this, "THIS WORKS! - " + String.valueOf(position), Toast.LENGTH_SHORT).show();
            } else {
                Timber.d("RecipeId: %d, StepId: %d", recipeId, stepId);
                if (viewRecipeStepViewModel == null) {
                    ViewRecipeStepViewModelFactory factory = new ViewRecipeStepViewModelFactory(recipeId, stepId);
                    viewRecipeStepViewModel = ViewModelProviders.of(this, factory).get(ViewRecipeStepViewModel.class);
                } else {
                    viewRecipeStepViewModel.queryRecipe(recipeId, stepId);
                }

                viewRecipeStepViewModel.getRecipeStep().observe(this, new Observer<RepositoryResponse>() {
                    @Override
                    public void onChanged(@Nullable RepositoryResponse repositoryResponse) {
                        if (repositoryResponse.getError() != null) {
                            Timber.d(repositoryResponse.getError());
                            return;
                        }

                        RecipeStep payload = (RecipeStep) repositoryResponse.getObject();

                        String recipeStepHeader = String.format("Step %d", payload.getStepIndex() + 1);
                        String recipeDescription = payload.getDescription();

                        Timber.d("Selected query result" + payload.toString());
                        handleTextPayload(recipeStepHeader, recipeDescription);
                        handleVideoUrl(payload.getVideoUrl());
                    }
                });
            }
        } else {
            if (false) {
                int containerId = RECIPE_DETAIL_COLLECTION_CONTAINER_ID;

                boolean isLandscape = this.findViewById(LAND_RECIPE_DETAILS_COLLECTION_CONTAINER_ID) != null;

                if (isLandscape) {
                    containerId = LAND_RECIPE_DETAILS_COLLECTION_CONTAINER_ID;
                }

                //ViewIngredientsFragment fragment = ViewIngredientsFragment.newInstance(recipeId);
                //FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                //        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                //        .replace(containerId, fragment);
                //fragmentTransaction.addToBackStack("TEST");
                //fragmentTransaction.commit();
            } else {

                // This will refer to either the ingredients list or the step screen.
                //  In either case, this will be easier to handle than juggling three fragments in this class.
                int vsState = 1;
                Intent intent = new Intent(this, ViewRecipeStepHolder.class);
                intent.putExtra(recipeIdBundleTag, recipeId);
                intent.putExtra(stepIdBundleTag, stepId);
                intent.putExtra(viewStepState, vsState);

                Timber.d("Launch ViewRecipeStepHolder activity - recipeId: %d, stepId: %d", recipeId, stepId);
                startActivity(intent);
            }
        }
    }

    private void handleTextPayload(String header, String desc) {
        String nullSafeHeader = header == null ? "" : header;
        String nullSafeDesc = desc == null ? "" : desc;

        Bundle bundle = new Bundle();
        bundle.putString(ViewRecipeStepTextFragment.ARG_STEP_HEADER, nullSafeHeader);
        bundle.putString(ViewRecipeStepTextFragment.ARG_STEP_DESC, nullSafeDesc);


        ViewRecipeStepTextFragment fragment = (ViewRecipeStepTextFragment) getSupportFragmentManager().findFragmentById(TABLET_RECIPE_STEP_TEXT_COLLECTION_CONTAINER_ID);
        fragment.updateFragmentUI(bundle);
    }

    private void handleVideoUrl(String url) {
        // Check if videoUrl is null
        if (url != null) {
            RecipeVideoFragment fragment = (RecipeVideoFragment) getSupportFragmentManager().findFragmentById(TABLET_RECIPE_STEP_VIDEO_COLLECTION_CONTAINER_ID);

            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.VIDEO_FRAG_ARGS), url);
            fragment.setAndInitializePlayer(bundle);
        }
    }

}

