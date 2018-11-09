package com.example.oliverh.bakerapp.ui.selectrecipe;

import android.arch.persistence.room.Transaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.RecipeRepository;
import com.example.oliverh.bakerapp.data.database.AppDatabase;
import com.example.oliverh.bakerapp.data.database.Recipe;
import com.facebook.stetho.Stetho;

import timber.log.Timber;

public class SelectRecipe extends AppCompatActivity implements
        SelectRecipeFragment.OnListFragmentInteractionListener {

    private static final String RECIPE_LIST_FRAGMENT_TAG = "RECIPE_LIST_FRAG_TAG";

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
                fragment = SelectRecipeFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_collection_container,
                                fragment,
                                RECIPE_LIST_FRAGMENT_TAG)
                        .commitNow();
            } else {
                Timber.d("Found fragment.");
            }

        }

//        initializeStetho();
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
    }
}
