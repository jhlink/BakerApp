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
import com.example.oliverh.bakerapp.data.database.Recipe;

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
    private List<String> recipeNames = new ArrayList<>();

    public IngredientsRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        appExecutors = AppExecutors.getInstance();
        recipeRepository = RecipeRepository.getInstance(context, AppDatabase.getInstance(context));
    }

    @Override
    public void onCreate() {
    }

    private void extractRecipeNamesFromRepository() {
        Log.d("RECIPE WIDGE", "starting query");
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Recipe> recipes = recipeRepository.getRawRecipeList();

                Log.d("RECIPE WIDGE", "CAN YOU... " + String.valueOf(recipes.size()));
                if (recipes != null) {
                    Log.d("RECIPE WIDGE", "VALIDATE INTERNAL");
                    recipeNames.clear();
                    for (Recipe recipe : recipes) {
                        recipeNames.add(recipe.getRecipeName());
                        Timber.d(" Widget Logging %s", recipe.getRecipeName());
                    }
                }
            }
        });
    }

    @Override
    public void onDataSetChanged() {
        List<Recipe> recipes = recipeRepository.getRawRecipeList();

        Log.d("RECIPE WIDGE", "CAN YOU... " + String.valueOf(recipes.size()));
        if (recipes != null) {
            Log.d("RECIPE WIDGE", "VALIDATE INTERNAL");
            recipeNames.clear();
            for (Recipe recipe : recipes) {
                recipeNames.add(recipe.getRecipeName());
                Timber.d(" Widget Logging %s", recipe.getRecipeName());
            }
        }
    }

    @Override
    public void onDestroy() {
        recipeNames.clear();
        appExecutors = null;
    }

    @Override
    public int getCount() {
        Log.d("WIDGET", String.valueOf(recipeNames.size()));
        return recipeNames.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        String recipeName = recipeNames.get(position);

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
