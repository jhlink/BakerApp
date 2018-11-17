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
            }
        }
    }

    public static void configureNewIngredientsList(Context context, Bundle configParams) {
        Intent intent = new Intent(context, WidgetManagerService.class);
        intent.setAction(ACTION_CONFIGURE_NEW_INGREDIENTS);
        intent.putExtras(configParams);
        context.startService(intent);
    }

    private void handleNewWidgetConfig(Bundle configParams) {
        String callingPackageName = configParams.getString(this.getString(R.string.BUNDLE_CONFIG_ACTIVITY_PKG_NAME));
        int recipeId = configParams.getInt(this.getString(R.string.EXTRA_WIDGET_RECIPE_ID));
        int appWidgetId = configParams.getInt(this.getString(R.string.BUNDLE_APP_WIDGET_ID));

        Timber.d("RecipeID %d, CallerPackageName %s, widgetID %d", recipeId, callingPackageName, appWidgetId);

        // Save Widget configuration into SharedPreferences
        SharedPreferences prefs = this.getSharedPreferences("com.example.oliverh.bakerapp.widget_configs", 0);
        SharedPreferences.Editor editor = prefs.edit();
        String formattedStringKey = String.format(Locale.ENGLISH, "WIDGET_%d_RECIPE_ID", appWidgetId);
        editor.putInt(formattedStringKey, recipeId);
        editor.apply();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidget.class));

        IngredientsWidget.updateAppWidget(this, appWidgetManager, appWidgetId, recipeId, callingPackageName);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_widgetListContainer);
    }

    public static void startActionUpdateIngredientsWidgets(Context context) {
    }

}
