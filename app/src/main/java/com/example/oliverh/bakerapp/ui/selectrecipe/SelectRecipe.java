package com.example.oliverh.bakerapp.ui.selectrecipe;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.database.Recipe;
import com.example.oliverh.bakerapp.ui.selectstep.SelectRecipeDetails;
import com.example.oliverh.bakerapp.ui.widget.IngredientsRemoteViewsService;
import com.facebook.stetho.Stetho;

import timber.log.Timber;

public class SelectRecipe extends AppCompatActivity implements
        SelectRecipeFragment.OnListFragmentInteractionListener {

    private static final String RECIPE_LIST_FRAGMENT_TAG = "RECIPE_LIST_FRAG_TAG";
    private static final int TABLET_RECIPE_COLLECTION_CONTAINER_ID = R.id.tablet_recipe_collection_container;
    private static final int LAND_TABLET_RECIPE_COLLECTION_CONTAINER_ID = R.id.land_recipe_collection_container;
    private static final int RECIPE_COLLECTION_CONTAINER_ID = R.id.recipe_collection_container;
    private static final String INTENT_ACTION_MAIN = Intent.ACTION_MAIN;
    private static final String INTENT_WIDGET_CONFIGURE = AppWidgetManager.ACTION_APPWIDGET_CONFIGURE;


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

    private static void initializeTimber() {
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

        String currentAction = getIntent().getAction() == null ? "" : getIntent().getAction();

        switch (currentAction) {
            case INTENT_WIDGET_CONFIGURE:
                handleWidgetConfigurationIntent(recipe);
                break;

            case INTENT_ACTION_MAIN:
            default:
                handleActionMainIntent(recipe);
        }

    }

    private void handleWidgetConfigurationIntent(Recipe recipe) {
        setResult(RESULT_CANCELED);

        Context context = getApplicationContext();
        Bundle widgetExtras = getIntent().getExtras();
        int appWidgetID = AppWidgetManager.INVALID_APPWIDGET_ID;

        if ( widgetExtras != null ) {
            appWidgetID = widgetExtras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        int recipeId = recipe.getId();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        Intent intent = new Intent(context, IngredientsRemoteViewsService.class);
        intent.putExtra("WIDGET_RECIPE_ID", recipeId);
        remoteViews.setRemoteAdapter(R.id.lv_widgetListContainer, intent);

        appWidgetManager.updateAppWidget(appWidgetID, remoteViews);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    private void handleActionMainIntent(Recipe recipe) {
        int recipeId = recipe.getId();
        final Intent intent = new Intent(this, SelectRecipeDetails.class);

        Timber.d("ACTION_MAIN Intent / RecipeId %d", recipeId);

        intent.putExtra(getString(R.string.BUNDLE_RECIPE_ID), recipeId);
        startActivity(intent);
    }
}
