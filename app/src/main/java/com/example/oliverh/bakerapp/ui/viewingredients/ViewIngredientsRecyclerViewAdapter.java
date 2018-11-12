package com.example.oliverh.bakerapp.ui.viewingredients;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.database.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ViewIngredientsRecyclerViewAdapter extends RecyclerView.Adapter<ViewIngredientsRecyclerViewAdapter.IngredientViewHolder> {
    private List<RecipeIngredient> mValues;

    public ViewIngredientsRecyclerViewAdapter(List<RecipeIngredient> items) {
        mValues = items == null ? new ArrayList<RecipeIngredient>() : items;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_generic_details_box)
        TextView tv_details;
        private RecipeIngredient mRecipeIngredient;

        public IngredientViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void logContent() {
            Timber.d(tv_details.getText().toString());
        }

        public void bindData(RecipeIngredient recipeIngredient) {
            mRecipeIngredient = recipeIngredient;
            tv_details.setText(mRecipeIngredient.toUIString());
        }
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_details_list_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder viewHolder, int i) {
        if (mValues.size() > 0) {
            RecipeIngredient recipe = mValues.get(i);
            viewHolder.bindData(recipe);
        }
    }

    public void setRecipeIngredients(List<RecipeIngredient> mRecipeIngredients) {
        mValues = mRecipeIngredients;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}
