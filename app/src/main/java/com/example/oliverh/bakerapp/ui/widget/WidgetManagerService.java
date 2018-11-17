package com.example.oliverh.bakerapp.ui.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.oliverh.bakerapp.R;

import java.util.Locale;

import timber.log.Timber;

public class WidgetManagerService extends IntentService {
    public static final String ACTION_CONFIGURE_NEW_INGREDIENTS = "com.example.oliverh.bakerapp.action.configure_new_ingredients";
    public static final String ACTION_UPDATE_INGREDIENTS = "com.example.oliverh.bakerapp.action.update_ingredients";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public WidgetManagerService() {
        super("BakerAppWidgetManagerService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CONFIGURE_NEW_INGREDIENTS.equals(action)) {
                Bundle bundle = intent.getExtras();
                handleNewWidgetConfig(bundle);
            } else if (ACTION_UPDATE_INGREDIENTS.equals(action)) {
                handleIngredientWidgetUpdate(this);
            }
        }
    }

    public static void configureNewIngredientsList(Context context, Bundle configParams) {
        Intent intent = new Intent(context, WidgetManagerService.class);
        intent.setAction(ACTION_CONFIGURE_NEW_INGREDIENTS);
        intent.putExtras(configParams);
        context.startService(intent);
    }

    public static void startActionUpdateIngredientsWidgets(Context context) {
        Intent intent = new Intent(context, WidgetManagerService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENTS);
        context.startService(intent);
    }

    private void handleNewWidgetConfig(Bundle configParams) {
        int recipeId = configParams.getInt(this.getString(R.string.EXTRA_WIDGET_RECIPE_ID));
        int appWidgetId = configParams.getInt(this.getString(R.string.BUNDLE_APP_WIDGET_ID));

        Timber.d("RecipeID %d, widgetID %d", recipeId, appWidgetId);

        // Save Widget configuration into SharedPreferences
        SharedPreferences prefs = this.getSharedPreferences("com.example.oliverh.bakerapp.widget_configs", 0);
        SharedPreferences.Editor editor = prefs.edit();
        String formattedStringKey = String.format(Locale.ENGLISH, "WIDGET_%d_RECIPE_ID", appWidgetId);
        editor.putInt(formattedStringKey, recipeId);
        editor.apply();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_widgetListContainer);

        IngredientsWidget.updateAppWidget(this, appWidgetManager, appWidgetId, recipeId);
    }

    public void handleIngredientWidgetUpdate(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, IngredientsWidget.class));

        SharedPreferences prefs = context.getSharedPreferences(getString(R.string.SHARED_PREF_FILE_ID), 0);

        for (int appWidgetId : appWidgetIds) {
            String formattedStringKey = String.format(Locale.ENGLISH, getString(R.string.SHARED_PREF_WIDGET_DATA_KEY_FORMULA), appWidgetId);
            int recipeID = prefs.getInt(formattedStringKey, AppWidgetManager.INVALID_APPWIDGET_ID);

            Timber.d("SharedPreference Key %s / RecipeID %d", formattedStringKey, recipeID);
            IngredientsWidget.updateIngredientListWidgets(context, appWidgetManager, appWidgetId, recipeID);
        }
    }
}
