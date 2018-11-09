package com.example.oliverh.bakerapp.ui.selectrecipe;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.database.Recipe;
import com.example.oliverh.bakerapp.data.network.RepositoryResponse;

import java.util.List;

import timber.log.Timber;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SelectRecipeFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String GRID_STATE_KEY = "GRID_POS";

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private SelectRecipeRecyclerViewAdapter adapter;

    private SelectRecipeViewModel mViewModel;
    private RecyclerView recyclerView;
    private Parcelable mState;

    private static int count = 0;

    public static SelectRecipeFragment newInstance() {
        return new SelectRecipeFragment();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SelectRecipeFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SelectRecipeFragment newInstance(int columnCount) {
        Timber.d("Column Count: %d", columnCount);
        SelectRecipeFragment fragment = new SelectRecipeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        if (savedInstanceState != null) {
            String gridStateParcelableKey = GRID_STATE_KEY;
            mState = savedInstanceState.getParcelable(gridStateParcelableKey);
        }
        mViewModel = ViewModelProviders.of(this).get(SelectRecipeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_recycler_view, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Log.d("Test", "This is RecyclerView");
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            adapter = new SelectRecipeRecyclerViewAdapter(null, mListener);
            recyclerView.setAdapter(adapter);

            if (mColumnCount <= 1) {
                Timber.d("RV: Set to LinearLayout");
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                Timber.d("RV: Set to GridLayout");
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            mViewModel.getRecipes().observe(this, new Observer<RepositoryResponse>() {
                @Override
                public void onChanged(@Nullable RepositoryResponse repositoryResponse) {
                    List<Recipe> recipes = repositoryResponse.getListOfData();
                    adapter.setRecipes(recipes);
                    adapter.notifyDataSetChanged();
                    restorePosition();
                }
            });
        }
        return view;
    }

    private void restorePosition() {
        if (mState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(mState);
            mState = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        mState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(GRID_STATE_KEY, mState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
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
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Recipe recipe);
    }
}
