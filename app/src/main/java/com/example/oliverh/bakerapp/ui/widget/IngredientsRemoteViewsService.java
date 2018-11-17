package com.example.oliverh.bakerapp.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.RecipeRepository;
import com.example.oliverh.bakerapp.data.database.AppDatabase;
import com.example.oliverh.bakerapp.data.database.RecipeIngredient;

import java.util.ArrayList;
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
    private List<String> ingredientNames = new ArrayList<>();
    private int recipeId;

    public IngredientsRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        recipeRepository = RecipeRepository.getInstance(context, AppDatabase.getInstance(context));
        recipeId = Integer.valueOf(intent.getData().getSchemeSpecificPart());
    }

    @Override
    public void onCreate() {
    }

    private void extractIngredientsFromRepository() {
        List<RecipeIngredient> ingredients = recipeRepository.getRawIngredientsList(recipeId);

        if (ingredients != null) {
            ingredientNames.clear();
            for (RecipeIngredient ingredient : ingredients) {
                ingredientNames.add(ingredient.toUIString());
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
    }

    @Override
    public int getCount() {
        return ingredientNames.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        String recipeName = ingredientNames.get(position);

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
