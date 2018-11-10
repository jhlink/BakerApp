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
public class SelectRecipeDetailsRecyclerViewAdapter extends RecyclerView.Adapter<SelectRecipeDetailsRecyclerViewAdapter.ViewHolder> {

    private final List<?> mValues;
    private final OnListFragmentInteractionListener mListener;
    private static final int INGREDIENTS_VIEW_TYPE = 1;
    private static final int STEPS_VIEW_TYPE = 2;

    public SelectRecipeDetailsRecyclerViewAdapter(List<?> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_details_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        public void bindString(String content) {

        }
    }
}
