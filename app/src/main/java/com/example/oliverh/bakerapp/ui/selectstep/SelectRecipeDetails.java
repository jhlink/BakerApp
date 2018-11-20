package com.example.oliverh.bakerapp.ui.selectstep;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.example.oliverh.bakerapp.Constants;
import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.database.RecipeStep;
import com.example.oliverh.bakerapp.data.network.RepositoryResponse;
import com.example.oliverh.bakerapp.ui.viewstep.RecipeVideoFragment;
import com.example.oliverh.bakerapp.ui.viewstep.ViewIngredientsFragment;
import com.example.oliverh.bakerapp.ui.viewstep.ViewRecipeStepHolder;
import com.example.oliverh.bakerapp.ui.viewstep.ViewRecipeStepTextFragment;
import com.example.oliverh.bakerapp.ui.viewstep.ViewRecipeStepViewModel;
import com.example.oliverh.bakerapp.ui.viewstep.ViewRecipeStepViewModelFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class SelectRecipeDetails extends AppCompatActivity
        implements SelectRecipeDetailsFragment.OnDetailInteractionListener,
        ViewRecipeStepTextFragment.OnFragmentInteractionListener {

    private static final int LAND_RECIPE_DETAILS_COLLECTION_CONTAINER_ID = R.id.land_recipe_detail_collection_container;
    private static final int RECIPE_DETAIL_COLLECTION_CONTAINER_ID = R.id.recipe_detail_collection_container;

    private static final int TABLET_RECIPE_DETAILS_COLLECTION_CONTAINER_ID = R.id.tbl_recipeDetailsListFrag;
    private static final int TABLET_RECIPE_STEP_TEXT_COLLECTION_CONTAINER_ID = R.id.tbl_recipeStepTextFrag;
    private static final int TABLET_RECIPE_STEP_VIDEO_COLLECTION_CONTAINER_ID = R.id.tbl_recipeStepVideoFrag;
    private static final int TABLET_RECIPE_INGREDIENT_COLLECTION_CONTAINER_ID = R.id.tbl_recipeIngredientFrag;

    private int recipeId;
    private boolean isTablet = false;
    private ViewRecipeStepViewModel viewRecipeStepViewModel;
    private int stepId;

    @BindView(LAND_RECIPE_DETAILS_COLLECTION_CONTAINER_ID)
    @Nullable
    FrameLayout landRecipeDetailsContainer;

    @BindView(RECIPE_DETAIL_COLLECTION_CONTAINER_ID)
    @Nullable
    FrameLayout recipeDetailsContainer;

    @BindView(R.id.tbl_recipeIngredientFrag)
    @Nullable
    FrameLayout layout;

    @BindView(R.id.tbl_recipeDetailsListFrag)
    @Nullable
    FrameLayout detailsLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_recipe_step_list);
        ButterKnife.bind(this);

        recipeId = getIntent().getIntExtra(getString(R.string.BUNDLE_RECIPE_ID),
                Constants.INVALID_RECIPE_ID);

        Timber.d("BackStack Count %d ", getSupportFragmentManager().getBackStackEntryCount());

        int containerId = getContainerResourceIDForCurrentOrientationState();
        initializeTabletRecipeStepLayout();

        SelectRecipeDetailsFragment fragment = (SelectRecipeDetailsFragment) getSupportFragmentManager().findFragmentById(containerId);

        if (fragment == null) {
            Timber.d("Create fragment.");
            fragment = SelectRecipeDetailsFragment.newInstance(recipeId);
        } else {
            Timber.d("Found fragment.");
            fragment.setRecipeId(recipeId);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment)
                .commit();
    }

    private void initializeTabletRecipeStepLayout() {
        showStepDetails();
        handleTextPayload("", "Select a Step!");
    }

    private int getContainerResourceIDForCurrentOrientationState() {
        int targetContainerID = RECIPE_DETAIL_COLLECTION_CONTAINER_ID;
        if (landRecipeDetailsContainer != null) {
            targetContainerID = LAND_RECIPE_DETAILS_COLLECTION_CONTAINER_ID;
        } else if (detailsLayout != null) {
            targetContainerID = TABLET_RECIPE_DETAILS_COLLECTION_CONTAINER_ID;
            isTablet = true;
        }
        return targetContainerID;
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
                handleDependentFragmentState(0);
                ViewIngredientsFragment viewIngredientsFragment = ViewIngredientsFragment.newInstance(recipeId);

                getSupportFragmentManager().executePendingTransactions();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.tbl_recipeIngredientFrag, viewIngredientsFragment)
                        .commitNow();


            } else {
                // Request for data given recipeId and stepId
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
                        handleDependentFragmentState(1);
                        handleTextPayload(recipeStepHeader, recipeDescription);
                        handleVideoUrl(payload.getVideoUrl());
                    }
                });
            }
        } else {
            int vsState;

            // This will refer to either the ingredients list [0] or the step screen [1].
            //  In either case, this will be easier to handle than juggling three fragments in this class.
            if (position == 0) {
                vsState = 0;
            } else {
                vsState = 1;
            }

            Intent intent = new Intent(this, ViewRecipeStepHolder.class);
            intent.putExtra(recipeIdBundleTag, recipeId);
            intent.putExtra(stepIdBundleTag, stepId);
            intent.putExtra(viewStepState, vsState);

            Timber.d("Launch ViewRecipeStepHolder activity - recipeId: %d, stepId: %d, state: %d", recipeId, stepId, vsState);
            startActivity(intent);
        }
    }

    private void handleDependentFragmentState(int state) {
        switch (state) {
            case 1:
                showStepDetails();
                break;

            case 0:
            default:
                showIngredients();
        }
    }

    private void showStepDetails() {
        RecipeVideoFragment recipeVideoFragment = (RecipeVideoFragment) getSupportFragmentManager().findFragmentById(TABLET_RECIPE_STEP_VIDEO_COLLECTION_CONTAINER_ID);
        ViewRecipeStepTextFragment recipeStepTextFragment = (ViewRecipeStepTextFragment) getSupportFragmentManager().findFragmentById(TABLET_RECIPE_STEP_TEXT_COLLECTION_CONTAINER_ID);
        ViewIngredientsFragment viewIngredientsFragment = (ViewIngredientsFragment) getSupportFragmentManager().findFragmentById(TABLET_RECIPE_INGREDIENT_COLLECTION_CONTAINER_ID);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .show(recipeVideoFragment)
                .show(recipeStepTextFragment);

        if (viewIngredientsFragment != null) {
            fragmentTransaction.hide(viewIngredientsFragment);
        }
        if (layout != null) {
            layout.setVisibility(View.GONE);
        }

        fragmentTransaction.commit();
    }

    private void showIngredients() {
        RecipeVideoFragment recipeVideoFragment = (RecipeVideoFragment) getSupportFragmentManager().findFragmentById(TABLET_RECIPE_STEP_VIDEO_COLLECTION_CONTAINER_ID);
        ViewRecipeStepTextFragment recipeStepTextFragment = (ViewRecipeStepTextFragment) getSupportFragmentManager().findFragmentById(TABLET_RECIPE_STEP_TEXT_COLLECTION_CONTAINER_ID);
        ViewIngredientsFragment viewIngredientsFragment = (ViewIngredientsFragment) getSupportFragmentManager().findFragmentById(TABLET_RECIPE_INGREDIENT_COLLECTION_CONTAINER_ID);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .hide(recipeVideoFragment)
                .hide(recipeStepTextFragment);

        if (viewIngredientsFragment != null) {
            fragmentTransaction.show(viewIngredientsFragment);
        }
        if (layout != null) {
            layout.setVisibility(View.VISIBLE);
        }

        fragmentTransaction.commit();
    }

    private void handleTextPayload(String header, String desc) {
        String nullSafeHeader = header == null ? "" : header;
        String nullSafeDesc = desc == null ? "" : desc;

        Bundle bundle = new Bundle();
        bundle.putString(ViewRecipeStepTextFragment.ARG_STEP_HEADER, nullSafeHeader);
        bundle.putString(ViewRecipeStepTextFragment.ARG_STEP_DESC, nullSafeDesc);
        bundle.putBoolean(ViewRecipeStepTextFragment.ARG_IS_NEXT_BTN_VISIBLE, isTablet);

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

