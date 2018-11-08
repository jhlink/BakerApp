package com.example.oliverh.bakerapp.ui.selectrecipe;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.database.Recipe;
//import com.example.oliverh.bakerapp.ui.selectrecipe.SelectRecipeFragment.OnListFragmentInteractionListener;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Recipe} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class SelectRecipeRecyclerViewAdapter extends RecyclerView.Adapter<SelectRecipeRecyclerViewAdapter.RecipeViewHolder> {

    private List<Recipe> mValues;
    //private final OnListFragmentInteractionListener mListener;

    public SelectRecipeRecyclerViewAdapter(List<Recipe> items){
       // , OnListFragmentInteractionListener listener) {
        mValues = items;
     //   mListener = listener;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_select_recipe_name) TextView tv_recipeName;
        @BindView(R.id.tv_select_recipe_servings) TextView tv_servingSize;

        public RecipeViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void logContent() {
            Timber.d("Recipe Name: %s, Servings: %s, ImageUrl: %s",
                    tv_recipeName.getText(),
                    tv_servingSize.getText(),
                    "") ;
        }

        public void bindData(Recipe recipe) {
            tv_recipeName.setText(recipe.getRecipeName());
            tv_servingSize.setText(String.valueOf(recipe.getServings()));
        }
    }


    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_cardview_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder viewHolder, int i) {
        if (mValues.size() > 0) {
            Recipe recipe = mValues.get(i);
            viewHolder.bindData(recipe);
        }

        // TODO: Implement onClickListener for Recipe Card -> RecipeDetails view
        //holder.mView.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        if (null != mListener) {
        //            // Notify the active callbacks interface (the activity, if the
        //            // fragment is attached to one) that an item has been selected.
        //            //mListener.onListFragmentInteraction(holder.mItem);
        //        }
        //    }
        //});
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

}
