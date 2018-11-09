package com.example.oliverh.bakerapp.ui.selectrecipe;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.database.Recipe;

import java.util.ArrayList;
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
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private SelectRecipeViewModel mViewModel;

    public static SelectRecipeFragment newInstance() {
        return new SelectRecipeFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(SelectRecipeViewModel.class);
        // TODO: Use the ViewModel
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_recycler_view, container, false);

        List<Recipe> recipes = new ArrayList<Recipe>();
        recipes.add(new Recipe(5, "test", 8, ""));
        recipes.add(new Recipe(6, "testFirst", 99, ""));
        recipes.add(new Recipe(7, "peanut butter", 0, ""));
        recipes.add(new Recipe(8, "jelly", 9, ""));
        recipes.add(new Recipe(9, "bread", 2, ""));
        recipes.add(new Recipe(10, "testFirst", 99, ""));
        recipes.add(new Recipe(11, "peanut butter", 0, ""));
        recipes.add(new Recipe(12, "jelly", 9, ""));
        recipes.add(new Recipe(13, "bread", 2, ""));

        // Set the adapter
        if (view instanceof RecyclerView) {
            Timber.d("This is RecyclerView");
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                Timber.d("RV: Set to LinearLayout");
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                Timber.d("RV: Set to GridLayout");
                recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            }
            recyclerView.setAdapter(new SelectRecipeRecyclerViewAdapter(recipes, mListener));
        }
        return view;
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
        // TODO: Update argument type and name
        void onListFragmentInteraction(Recipe recipe);
    }
}
