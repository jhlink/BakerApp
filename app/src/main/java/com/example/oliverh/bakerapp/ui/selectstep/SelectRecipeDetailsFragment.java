package com.example.oliverh.bakerapp.ui.selectstep;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.SelectRecipeDetailsFragmentViewModel;
import com.example.oliverh.bakerapp.data.database.RecipeStep;
import com.example.oliverh.bakerapp.data.network.RepositoryResponse;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnDetailInteractionListener}
 * interface.
 */
public class SelectRecipeDetailsFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "recipe-id";
    private static final String RV_DETAILS_STATE_KEY = "RECIPE_DETAILS_STATE";
    private int recipeId = -1;

    private OnDetailInteractionListener mListener;
    private SelectRecipeDetailsRecyclerViewAdapter adapter;
    private SelectRecipeDetailsFragmentViewModel mViewModel;
    private RecyclerView recyclerView;
    private Parcelable mSavedState;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SelectRecipeDetailsFragment() {
    }

    @SuppressWarnings("unused")
    public static SelectRecipeDetailsFragment newInstance(int recipeId) {
        SelectRecipeDetailsFragment fragment = new SelectRecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            recipeId = getArguments().getInt(ARG_RECIPE_ID);
        }

        if (savedInstanceState != null) {
            mSavedState = savedInstanceState.getParcelable(RV_DETAILS_STATE_KEY);
        }

        SelectRecipeDetailsFragmentViewModelFactory factory = new SelectRecipeDetailsFragmentViewModelFactory(recipeId);
        mViewModel = ViewModelProviders.of(this, factory).get(SelectRecipeDetailsFragmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_recycler_view, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            adapter = new SelectRecipeDetailsRecyclerViewAdapter(null, mListener);
            recyclerView.setAdapter(adapter);

            mViewModel.getRecipeSteps().observe(this, new Observer<RepositoryResponse>() {
                @Override
                public void onChanged(@Nullable RepositoryResponse repositoryResponse) {
                    List<RecipeStep> steps = repositoryResponse.getListOfData();

                    SparseArray sparseArray = new SparseArray<>();
                    for (int i = 0; i < steps.size() + 1; i++) {
                        if (i == 0) {
                            sparseArray.put(0, "Recipe Ingredients");
                        } else {
                            sparseArray.put(i, steps.get(i - 1));
                        }
                    }
                    adapter.setDataset(sparseArray);
                    adapter.notifyDataSetChanged();
                    restorePosition();
                }
            });
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        mSavedState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(RV_DETAILS_STATE_KEY, mSavedState);
    }

    private void restorePosition() {
        if (mSavedState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(mSavedState);
            mSavedState = null;
        }
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailInteractionListener) {
            mListener = (OnDetailInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDetailInteractionListener {
        void onDetailInteractionListener(int position);
    }
}
