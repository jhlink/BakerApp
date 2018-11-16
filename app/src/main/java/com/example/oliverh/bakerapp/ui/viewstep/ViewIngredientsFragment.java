package com.example.oliverh.bakerapp.ui.viewstep;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.database.RecipeIngredient;
import com.example.oliverh.bakerapp.data.network.RepositoryResponse;
import com.example.oliverh.bakerapp.ui.selectstep.SelectRecipeDetailsFragmentViewModel;
import com.example.oliverh.bakerapp.ui.selectstep.SelectRecipeDetailsFragmentViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ViewIngredientsFragment extends Fragment {

    private static final String LIST_STATE_KEY = "LIST_POS";
    private static final String ARG_RECIPE_ID = "RECIPE_ID";

    private int mRecipeId = -1;
    private ViewIngredientsRecyclerViewAdapter adapter;

    private SelectRecipeDetailsFragmentViewModel mViewModel;
    private RecyclerView recyclerView;
    private Parcelable mState;

    public ViewIngredientsFragment() {
    }

    @SuppressWarnings("unused")
    public static ViewIngredientsFragment newInstance(int recipeId) {
        Timber.d("Recipe ID: %d", recipeId);
        ViewIngredientsFragment fragment = new ViewIngredientsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(ARG_RECIPE_ID);
        }

        if (savedInstanceState != null) {
            mState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }

        SelectRecipeDetailsFragmentViewModelFactory factory = new SelectRecipeDetailsFragmentViewModelFactory(mRecipeId);
        mViewModel = ViewModelProviders.of(this, factory).get(SelectRecipeDetailsFragmentViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_generic_recycler_view, container, false);

        // Set the adapter
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.rv_generic_container);
        if (recyclerView != null) {
            adapter = new ViewIngredientsRecyclerViewAdapter(new ArrayList<RecipeIngredient>());
            recyclerView.setAdapter(adapter);

            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            mViewModel.getRecipeIngredients().observe(this, new Observer<RepositoryResponse>() {
                @Override
                public void onChanged(@Nullable RepositoryResponse repositoryResponse) {

                    if (isErrorPresent(repositoryResponse)) {
                        return;
                    }

                    List<RecipeIngredient> recipeIngredients = repositoryResponse.getListOfData();
                    adapter.setRecipeIngredients(recipeIngredients);
                    adapter.notifyDataSetChanged();
                    restorePosition();
                }
            });
        }
        return view;
    }

    private boolean isErrorPresent(RepositoryResponse repositoryResponse) {
        boolean result = false;
        if (repositoryResponse == null) {
            Timber.e("RepositoryResponse does not exist");
            result = true;
        } else if (repositoryResponse.getError() != null) {
            Timber.e("RepositoryResponse does not exist");
            result = true;
        }

        return result;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        RecyclerView.LayoutManager rvLayoutManager = recyclerView.getLayoutManager();
        if (rvLayoutManager != null) {
            mState = rvLayoutManager.onSaveInstanceState();
        }

        outState.putParcelable(LIST_STATE_KEY, mState);
    }

    private void restorePosition() {
        if (mState != null) {
            RecyclerView.LayoutManager rvLayoutManager = recyclerView.getLayoutManager();
            if (rvLayoutManager != null) {
                rvLayoutManager.onRestoreInstanceState(mState);
            }
            mState = null;
        }
    }

}
