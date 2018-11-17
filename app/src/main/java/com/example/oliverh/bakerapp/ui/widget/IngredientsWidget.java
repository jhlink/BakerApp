package com.example.oliverh.bakerapp.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.oliverh.bakerapp.R;

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
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

