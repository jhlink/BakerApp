package com.example.oliverh.bakerapp.ui.selectrecipe;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.database.Recipe;
import com.example.oliverh.bakerapp.ui.selectrecipe.SelectRecipeFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Recipe} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SelectRecipeRecyclerViewAdapter extends RecyclerView.Adapter<SelectRecipeRecyclerViewAdapter.ViewHolder> {

    private final List<Recipe> mValues;
    private final OnListFragmentInteractionListener mListener;

    public SelectRecipeRecyclerViewAdapter(List<Recipe> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_cardview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Recipe recipe = mValues.get(position);
        holder.tv_recipeName.setText(recipe.getRecipeName());
        holder.tv_servingSize.setText(recipe.getServings());

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tv_recipeName;
        public final ImageView iv_recipeImage;
        public final TextView tv_servingSize;


        public ViewHolder(View view) {
            super(view);
            tv_recipeName = view.findViewById(R.id.tv_select_recipe_name);
            tv_servingSize = view.findViewById(R.id.tv_select_recipe_servings);
            iv_recipeImage = view.findViewById(R.id.iv_select_recipe_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tv_recipeName.getText() + "'";
        }
    }
}
