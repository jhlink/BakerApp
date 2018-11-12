package com.example.oliverh.bakerapp.ui.selectrecipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.database.Recipe;
import com.example.oliverh.bakerapp.ui.selectstep.SelectRecipeDetails;
import com.facebook.stetho.Stetho;

import timber.log.Timber;

public class SelectRecipe extends AppCompatActivity implements
        SelectRecipeFragment.OnListFragmentInteractionListener {

    private static final String RECIPE_LIST_FRAGMENT_TAG = "RECIPE_LIST_FRAG_TAG";
    private static final int TABLET_RECIPE_COLLECTION_CONTAINER_ID = R.id.tablet_recipe_collection_container;
    private static final int LAND_TABLET_RECIPE_COLLECTION_CONTAINER_ID = R.id.land_recipe_collection_container;
    private static final int RECIPE_COLLECTION_CONTAINER_ID = R.id.recipe_collection_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_recipe_activity);

        initializeTimber();


        if (savedInstanceState == null) {

            // TODO: Properly handle this.

            SelectRecipeFragment fragment = (SelectRecipeFragment) getSupportFragmentManager().findFragmentByTag(RECIPE_LIST_FRAGMENT_TAG);
            if (fragment == null) {
                Timber.d("Create fragment.");

                int columnSpan = 1;
                int targetContainerID = RECIPE_COLLECTION_CONTAINER_ID;
                if ( this.findViewById(TABLET_RECIPE_COLLECTION_CONTAINER_ID) != null ) {
                    columnSpan = 3;
                    targetContainerID = TABLET_RECIPE_COLLECTION_CONTAINER_ID;
                }

                fragment = SelectRecipeFragment.newInstance(columnSpan);
                getSupportFragmentManager().beginTransaction()
                        .replace(targetContainerID,
                                fragment,
                                RECIPE_LIST_FRAGMENT_TAG)
                        .commitNow();
            } else {
                Timber.d("Found fragment.");
            }
        } else {
            if ( this.findViewById(LAND_TABLET_RECIPE_COLLECTION_CONTAINER_ID) != null ) {
                Fragment fragment = SelectRecipeFragment.newInstance(2);
                getSupportFragmentManager().beginTransaction()
                        .replace(LAND_TABLET_RECIPE_COLLECTION_CONTAINER_ID,
                                fragment,
                                RECIPE_LIST_FRAGMENT_TAG)
                        .commitNow();
            }

        }

        initializeStetho();
    }

    private static void initializeTimber(){
        if (Timber.treeCount() < 1) {
            Timber.plant(new Timber.DebugTree());
            Timber.d("Timber Tree Count: %d", Timber.treeCount());
        }
    }

    private void initializeStetho() {
        Stetho.initializeWithDefaults(this);
    }

    @Override
    public void onListFragmentInteraction(Recipe recipe) {
        Toast.makeText(this, recipe.getRecipeName(), Toast.LENGTH_SHORT).show();

        final Intent intent = new Intent(this, SelectRecipeDetails.class);
        int recipeId = recipe.getId();

        Timber.d("Intent to pass to SelectRecipeDetails : %d", recipeId);

        intent.putExtra(getString(R.string.BUNDLE_RECIPE_ID), recipeId);
        startActivity(intent);
    }
}
