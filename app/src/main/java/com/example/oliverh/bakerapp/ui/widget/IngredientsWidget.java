package com.example.oliverh.bakerapp.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.oliverh.bakerapp.R;

import java.util.Locale;

import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int recipeId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        Intent intent = new Intent(context, IngredientsRemoteViewsService.class);
        Timber.d("WidgetID %d, RecipeID Validation %d", appWidgetId, recipeId);
        intent.setData(Uri.fromParts("content", String.valueOf(recipeId), null));
        views.setRemoteAdapter(R.id.lv_widgetListContainer, intent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetManagerService.startActionUpdateIngredientsWidgets(context);
    }

    public static void updateIngredientListWidgets(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int mRecipeId) {
        updateAppWidget(context, appWidgetManager, appWidgetId, mRecipeId);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.SHARED_PREF_FILE_ID), 0);
        String sharedPrefStringKeyTemplate = context.getString(R.string.SHARED_PREF_WIDGET_DATA_KEY_FORMULA);

        SharedPreferences.Editor editor = prefs.edit();
        for (int id : appWidgetIds) {
            String formattedStringKey = String.format(Locale.ENGLISH, sharedPrefStringKeyTemplate, id);
            editor.remove(formattedStringKey);
        }
        editor.apply();

        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

