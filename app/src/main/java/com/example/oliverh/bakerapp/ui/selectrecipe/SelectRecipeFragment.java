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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.database.Recipe;
import com.example.oliverh.bakerapp.data.network.RepositoryResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SelectRecipeFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String GRID_STATE_KEY = "GRID_POS";

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private SelectRecipeRecyclerViewAdapter adapter;

    private SelectRecipeViewModel mViewModel;
    private Parcelable mState;

    @BindView(R.id.rv_generic_container)
    RecyclerView recyclerView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar progressBar;

    @BindView(R.id.tv_error_message)
    TextView errorMessage;

    public static SelectRecipeFragment newInstance() {
        return new SelectRecipeFragment();
    }

    public SelectRecipeFragment() {
    }

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
            mState = savedInstanceState.getParcelable(GRID_STATE_KEY);
        }
        mViewModel = ViewModelProviders.of(this).get(SelectRecipeViewModel.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_generic_recycler_view, container, false);
        ButterKnife.bind(this, view);

        if (recyclerView != null) {
            Context context = view.getContext();
            adapter = new SelectRecipeRecyclerViewAdapter(null, mListener);
            recyclerView.setAdapter(adapter);

            if (mColumnCount <= 1) {
                Timber.d("RV: Set to LinearLayout");
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                Timber.d("RV: Set to GridLayout");
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            onNetworkRequest();
            mViewModel.getRecipes().observe(this, new Observer<RepositoryResponse>() {
                @Override
                public void onChanged(@Nullable RepositoryResponse repositoryResponse) {
                    if (isErrorPresent(repositoryResponse)) {
                        return;
                    }

                    List<Recipe> recipes = repositoryResponse.getListOfData();
                    adapter.setRecipes(recipes);
                    adapter.notifyDataSetChanged();
                    restorePosition();
                    onNetworkSuccess();
                }
            });
        }
        return view;
    }

    private void onNetworkFailure() {
        errorMessage.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void onNetworkRequest() {
        errorMessage.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void onNetworkSuccess() {
        errorMessage.setVisibility(View.GONE);
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private boolean isErrorPresent(RepositoryResponse repositoryResponse) {
        boolean result = false;
        if (repositoryResponse == null) {
            Timber.e("RepositoryResponse does not exist");
            result = true;
        } else if (repositoryResponse.getError() != null) {
            Timber.e(repositoryResponse.getError());
            onNetworkFailure();
            result = true;
        }

        return result;
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        RecyclerView.LayoutManager rvLayoutManager = recyclerView.getLayoutManager();
        if (rvLayoutManager != null) {
            mState = rvLayoutManager.onSaveInstanceState();
        }
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

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Recipe recipe);
    }
}
