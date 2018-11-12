package com.example.oliverh.bakerapp.ui.viewingredients;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.oliverh.bakerapp.data.database.RecipeIngredient;

import java.util.List;

public class IngredientsListAdapter extends BaseAdapter {
    private Context context;
    private List<RecipeIngredient> mIngredients;

    @Override
    public int getCount() {
        return mIngredients.size();
    }

    @Override
    public RecipeIngredient getItem(int position) {
        return mIngredients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mIngredients.get(position).getRecipeIngredientId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
