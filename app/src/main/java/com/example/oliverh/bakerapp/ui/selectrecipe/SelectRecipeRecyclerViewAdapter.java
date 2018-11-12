package com.example.oliverh.bakerapp.ui.selectrecipe;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.database.Recipe;
import com.example.oliverh.bakerapp.ui.selectrecipe.SelectRecipeFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Recipe} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class SelectRecipeRecyclerViewAdapter extends RecyclerView.Adapter<SelectRecipeRecyclerViewAdapter.RecipeViewHolder> {

    private List<Recipe> mValues;
    private final OnListFragmentInteractionListener mListener;

    public SelectRecipeRecyclerViewAdapter(List<Recipe> items, OnListFragmentInteractionListener listener) {
        mValues = items == null ? new ArrayList<Recipe>() : items;
        mListener = listener;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_select_recipe_name) TextView tv_recipeName;
        @BindView(R.id.tv_select_recipe_servings) TextView tv_servingSize;
        @BindView(R.id.iv_select_recipe_image) ImageView iv_recipeImage;
        private Recipe mRecipe;
        private OnListFragmentInteractionListener mvhListener;

        public RecipeViewHolder(View view, OnListFragmentInteractionListener vhListener) {
            super(view);
            ButterKnife.bind(this, view);
            mvhListener = vhListener;
        }

        public void logContent() {
            Timber.d("Recipe Name: %s, Servings: %s, ImageUrl: %s",
                    tv_recipeName.getText(),
                    tv_servingSize.getText(),
                    iv_recipeImage.getDrawable());
        }

        public void bindData(Recipe recipe) {
            mRecipe = recipe;
            tv_recipeName.setText(recipe.getRecipeName());
            tv_servingSize.setText(String.valueOf(recipe.getServings()));

            if (!recipe.getImageURL().isEmpty()) {
                Picasso.get()
                        .load(recipe.getImageURL())
                        .placeholder(R.drawable.ic_image)
                        .error(R.drawable.ic_broken_image)
                        .into(iv_recipeImage);
            } else {
                iv_recipeImage.setImageResource(R.drawable.ic_broken_image);
            }
        }

        @OnClick(R.id.cv_recipe_view)
        void onClickRecipeCard() {
            if (null != mvhListener) {
                mvhListener.onListFragmentInteraction(mRecipe);
            }
        }
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_cardview_item, parent, false);
        return new RecipeViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder viewHolder, int i) {
        if (mValues.size() > 0) {
            Recipe recipe = mValues.get(i);
            viewHolder.bindData(recipe);
        }
    }

    public void setRecipes(List<Recipe> mRecipes) {
        mValues = mRecipes;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}
