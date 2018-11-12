package com.example.oliverh.bakerapp.ui.selectstep;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.ui.viewstep.ViewRecipeStep;

import timber.log.Timber;

public class SelectRecipeDetails extends AppCompatActivity implements SelectRecipeDetailsFragment.OnDetailInteractionListener {

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_recipe_step_list);

        recipeId = getIntent().getIntExtra(getString(R.string.BUNDLE_RECIPE_ID), -1);

        isTablet = this.findViewById(TABLET_RECIPE_DETAILS_COLLECTION_CONTAINER_ID) != null;

        if (isTablet) {


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
                        .replace(RECIPE_DETAIL_COLLECTION_CONTAINER_ID,
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
    public void onDetailInteractionListener(int position) {
        if ( position == 0 ) {
            Toast.makeText(this, "THIS WORKS! - " + String.valueOf(position), Toast.LENGTH_SHORT).show();
        } else {
            String recipeIdBundleTag = getString(R.string.BUNDLE_RECIPE_ID);

            String stepIdBundleTag = getString(R.string.BUNDLE_STEP_ID);
            int stepId = position - 1;

            Intent intent = new Intent(this, ViewRecipeStep.class);
            intent.putExtra(recipeIdBundleTag, recipeId);
            intent.putExtra(stepIdBundleTag, stepId);

            Timber.d("Launch ViewRecipeStep activity - recipeId: %d, stepId: %d", recipeId, stepId);
            startActivity(intent);
        }
    }
}
