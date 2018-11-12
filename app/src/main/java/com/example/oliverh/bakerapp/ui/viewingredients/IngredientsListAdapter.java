package com.example.oliverh.bakerapp.ui.viewingredients;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.database.RecipeIngredient;

import java.util.List;

public class IngredientsListAdapter extends BaseAdapter {
    private Context context;
    private List<RecipeIngredient> mIngredients;

    public IngredientsListAdapter(Context context, List<RecipeIngredient> mIngredients) {
        this.context = context;
        this.mIngredients = mIngredients;
    }

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
        TextView textView;
        RecipeIngredient recipeIngredient = mIngredients.get(position);

        if (convertView == null) {
            textView = new TextView(context);

            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.layout(0,
                    8,
                    0,
                    0);
            textView.setTextAppearance(context, R.style.TextAppearance_AppCompat_Headline);
        } else {
            textView = (TextView) convertView;
        }

        textView.setText(recipeIngredient.toUIString());
        return textView;
    }
}
