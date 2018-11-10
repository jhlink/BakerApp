package com.example.oliverh.bakerapp.ui.selectstep;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.database.RecipeStep;
import com.example.oliverh.bakerapp.ui.selectstep.SelectRecipeDetailsFragment.OnDetailInteractionListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * {@link RecyclerView.Adapter} that can display a generic text description and makes a call to the
 * specified {@link OnDetailInteractionListener}.
 */
public class SelectRecipeDetailsRecyclerViewAdapter extends RecyclerView.Adapter<SelectRecipeDetailsRecyclerViewAdapter.RecipeDetailViewHolder> {

    private SparseArray mValues;
    private final OnDetailInteractionListener mListener;

    public SelectRecipeDetailsRecyclerViewAdapter(SparseArray items, OnDetailInteractionListener listener) {
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
                String RecipeIngredient_ContentText = (String) mValues.get(0);
                break;

            default:
                // Pass 'Recipe Steps' text to ViewHolder.
                RecipeStep step = (RecipeStep) mValues.get(0);
                content = String.format("Recipe Step %d.", step.getStepIndex());
                break;
        }
        holder.bindString(content);
    }

    public void setDataset( SparseArray sparseArray ) {
        this.mValues = sparseArray;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class RecipeDetailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_generic_details_box) TextView mContentView;

        public RecipeDetailViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void logContent() {
            Timber.d("Recipe Type: %s",
                    mContentView.getText());
        }

        public void bindString(String content) {
            mContentView.setText(content);
        }

        @OnClick(R.id.tv_generic_details_box)
        public void onViewHolderClick() {
            if (null != mListener) {
                mListener.onDetailInteractionListener(getAdapterPosition());
            }
        }
    }
}
