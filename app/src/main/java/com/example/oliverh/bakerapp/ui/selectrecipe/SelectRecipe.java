package com.example.oliverh.bakerapp.ui.selectrecipe;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.RecipeRepository;
import com.example.oliverh.bakerapp.data.database.AppDatabase;
import com.example.oliverh.bakerapp.data.database.Recipe;
import com.facebook.stetho.Stetho;

import timber.log.Timber;

public class SelectRecipe extends AppCompatActivity implements
        SelectRecipeFragment.OnListFragmentInteractionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_recipe_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_collection_container, SelectRecipeFragment.newInstance())
                    .commitNow();
        }

        initializeStetho();

        Timber.plant(new Timber.DebugTree());

        Context context = getApplicationContext();
        AppDatabase mDb = AppDatabase.getInstance(context);
        RecipeRepository.getInstance(context, mDb).getRecipeList();

    }

    private void initializeStetho() {
        Stetho.initializeWithDefaults(this);
    }

    @Override
    public void onListFragmentInteraction(Recipe recipe) {
        Toast.makeText(this, recipe.getRecipeName(), Toast.LENGTH_SHORT).show();
    }
}
