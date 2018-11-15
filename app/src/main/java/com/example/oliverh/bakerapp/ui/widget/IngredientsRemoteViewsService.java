package com.example.oliverh.bakerapp.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.oliverh.bakerapp.AppExecutors;
import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.RecipeRepository;
import com.example.oliverh.bakerapp.data.database.AppDatabase;
import com.example.oliverh.bakerapp.data.database.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class IngredientsRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsRemoteViewsFactory(getApplicationContext(), intent);
    }
}

class IngredientsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private RecipeRepository recipeRepository;
    private AppExecutors appExecutors;
    private List<String> ingredientNames = new ArrayList<>();
    private int recipeId;

    public IngredientsRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        appExecutors = AppExecutors.getInstance();
        recipeRepository = RecipeRepository.getInstance(context, AppDatabase.getInstance(context));
        recipeId = intent.getIntExtra("WIDGET_RECIPE_ID", -1);
    }

    @Override
    public void onCreate() {
    }

    private void extractIngredientsFromRepository() {
        Log.d("RECIPE WIDGE", "starting query");
        List<RecipeIngredient> ingredients = recipeRepository.getRawIngredientsList(recipeId);

        Timber.d("CAN YOU... " + String.valueOf(ingredients.size()));
        if (ingredients != null) {
            Timber.d("VALIDATE INTERNAL");
            ingredientNames.clear();
            for (RecipeIngredient ingredient : ingredients) {
                ingredientNames.add(ingredient.toUIString());
                Timber.d(" Widget Logging %s", ingredient.toUIString());
            }
        }
    }

    @Override
    public void onDataSetChanged() {
        extractIngredientsFromRepository();
    }

    @Override
    public void onDestroy() {
        ingredientNames.clear();
        appExecutors = null;
    }

    @Override
    public int getCount() {
        Log.d("WIDGET", String.valueOf(ingredientNames.size()));
        return ingredientNames.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        String recipeName = ingredientNames.get(position);

        Timber.d(" View at ... %d ", position);

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.recipe_details_list_item);
        remoteViews.setTextViewText(R.id.tv_generic_details_box, recipeName);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
