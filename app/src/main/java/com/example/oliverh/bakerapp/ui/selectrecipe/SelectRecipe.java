package com.example.oliverh.bakerapp.ui.selectrecipe;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.database.Recipe;
import com.example.oliverh.bakerapp.ui.selectstep.SelectRecipeDetails;
import com.example.oliverh.bakerapp.ui.widget.WidgetManagerService;
import com.facebook.stetho.Stetho;

import timber.log.Timber;

public class SelectRecipe extends AppCompatActivity implements
        SelectRecipeFragment.OnListFragmentInteractionListener {

    private static final int TABLET_RECIPE_COLLECTION_CONTAINER_ID = R.id.tablet_recipe_collection_container;
    private static final int LAND_RECIPE_COLLECTION_CONTAINER_ID = R.id.land_recipe_collection_container;
    private static final int RECIPE_COLLECTION_CONTAINER_ID = R.id.recipe_collection_container;
    private static final String INTENT_ACTION_MAIN = Intent.ACTION_MAIN;
    private static final String INTENT_WIDGET_CONFIGURE = AppWidgetManager.ACTION_APPWIDGET_CONFIGURE;

    private int columnSpan = 1;
    private int targetContainerID = RECIPE_COLLECTION_CONTAINER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_recipe_activity);

        initializeTimber();

        determineDeviceOrientationState();

        SelectRecipeFragment fragment = (SelectRecipeFragment) getSupportFragmentManager().findFragmentById(targetContainerID);

        if (fragment == null) {
            Timber.d("Create fragment.");
            fragment = SelectRecipeFragment.newInstance(columnSpan);
        } else {
            Timber.d("Found fragment.");
        }

        getSupportFragmentManager().beginTransaction()
                .replace(targetContainerID,
                        fragment)
                .commit();

        // initializeStetho();
    }

    private boolean doesViewExist(int resourceId) {
        return this.findViewById(resourceId) != null;
    }

    private void determineDeviceOrientationState() {
        if (doesViewExist(LAND_RECIPE_COLLECTION_CONTAINER_ID)) {
            columnSpan = 2;
            targetContainerID = LAND_RECIPE_COLLECTION_CONTAINER_ID;
        } else if (doesViewExist(TABLET_RECIPE_COLLECTION_CONTAINER_ID)) {
            columnSpan = 3;
            targetContainerID = TABLET_RECIPE_COLLECTION_CONTAINER_ID;
        }
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
        String currentAction = getIntent().getAction() == null ? "" : getIntent().getAction();

        int recipeId = recipe.getId();

        switch (currentAction) {
            case INTENT_WIDGET_CONFIGURE:
                Timber.d(getString(R.string.SELECT_RECIPE_INTENT_FORMULA_RECIPE_ID), INTENT_WIDGET_CONFIGURE, recipeId);
                handleWidgetConfigurationIntent(recipeId);
                break;

            case INTENT_ACTION_MAIN:
            default:
                Timber.d(getString(R.string.SELECT_RECIPE_INTENT_FORMULA_RECIPE_ID), INTENT_ACTION_MAIN, recipeId);
                handleActionMainIntent(recipeId);
        }

    }

    private void handleWidgetConfigurationIntent(int mRecipeId) {
        setResult(RESULT_CANCELED);

        Bundle widgetExtras = getIntent().getExtras();
        int appWidgetID = AppWidgetManager.INVALID_APPWIDGET_ID;

        if ( widgetExtras != null ) {
            appWidgetID = widgetExtras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        } else {
            finish();
            return;
        }

        // Assemble Bundle and send to Widget service
        Bundle parameterBundle = new Bundle();
        parameterBundle.putInt(getString(R.string.EXTRA_WIDGET_RECIPE_ID), mRecipeId);
        parameterBundle.putInt(getString(R.string.BUNDLE_APP_WIDGET_ID), appWidgetID);

        WidgetManagerService.configureNewIngredientsList(this, parameterBundle);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    private void handleActionMainIntent(int mRecipeId) {
        final Intent intent = new Intent(this, SelectRecipeDetails.class);
        intent.putExtra(getString(R.string.BUNDLE_RECIPE_ID), mRecipeId);
        startActivity(intent);
    }
}
