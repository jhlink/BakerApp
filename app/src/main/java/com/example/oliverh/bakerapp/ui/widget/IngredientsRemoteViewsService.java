package com.example.oliverh.bakerapp.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.oliverh.bakerapp.data.RecipeRepository;
import com.example.oliverh.bakerapp.data.database.AppDatabase;
import com.example.oliverh.bakerapp.data.database.Recipe;

import java.util.List;

public class IngredientsRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsRemoteViewsFactory(getApplicationContext(), intent);
    }
}

class IngredientsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private RecipeRepository recipeRepository;
    private List<String> recipeNames;

    public IngredientsRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        recipeRepository = RecipeRepository.getInstance(context, AppDatabase.getInstance(context));
    }

    @Override
    public void onCreate() {
        extractRecipeNamesFromRepository();
    }

    private void extractRecipeNamesFromRepository() {
        List<Recipe> recipes = recipeRepository.getRawRecipeList();

        if (recipes.size() > 0) {
            for (Recipe recipe : recipes) {
                recipeNames.add(recipe.getRecipeName());
            }
        }

    }

    @Override
    public void onDataSetChanged() {
        extractRecipeNamesFromRepository();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
