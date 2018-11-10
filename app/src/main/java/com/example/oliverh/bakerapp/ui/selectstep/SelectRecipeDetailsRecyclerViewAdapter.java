package com.example.oliverh.bakerapp.ui.selectstep;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.ui.selectstep.SelectRecipeDetailsFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a generic text description and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SelectRecipeDetailsRecyclerViewAdapter extends RecyclerView.Adapter<SelectRecipeDetailsRecyclerViewAdapter.RecipeDetailViewHolder> {

    private final List<?> mValues;
    private final OnListFragmentInteractionListener mListener;

    public SelectRecipeDetailsRecyclerViewAdapter(List<?> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public RecipeDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_details_list_item, parent, false);
        return new RecipeDetailViewHolder(view);
    }

    //  It is assumed that the first element in the mValues position will be
    //      RecipeIngredients 'Text', and the following elements are RecipeStep
    //      instantiations.
    @Override
    public void onBindViewHolder(final RecipeDetailViewHolder holder, int position) {
        String content = "";
        switch ( position ) {
            case 0:
                // Pass 'Recipe Ingredients' static text to ViewHolder
                break;

            default:
                // Pass 'Recipe Steps' text to ViewHolder.
                break;
        }
        holder.bindString(content);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class RecipeDetailViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;

        public RecipeDetailViewHolder(View view) {
            super(view);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        public void bindString(String content) {

        }
    }
}
