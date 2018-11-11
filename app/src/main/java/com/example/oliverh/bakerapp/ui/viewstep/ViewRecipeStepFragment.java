package com.example.oliverh.bakerapp.ui.viewstep;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.database.RecipeStep;
import com.example.oliverh.bakerapp.data.network.RepositoryResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ViewRecipeStepFragment extends Fragment {

    private static final String ARGS_RECIPE_ID = "RECIPE_ID";
    private static final String ARGS_STEP_ID = "STEP_ID";
    private ViewRecipeStepViewModel mViewModel;
    private int mRecipeId;
    private int mStepId;
    private boolean landscape_videoFullScreen = false;

    @BindView(R.id.tv_recipeStepHeader)
    @Nullable
    TextView recipeStepHeader;

    @BindView(R.id.tv_recipeStepDesc)
    @Nullable
    TextView recipeStepDescription;

    @BindView(R.id.btn_nextStep)
    @Nullable
    Button nextStepButton;

    public static ViewRecipeStepFragment newInstance(int recipeId, int stepId ) {
        ViewRecipeStepFragment fragment = new ViewRecipeStepFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_RECIPE_ID, recipeId);
        args.putInt(ARGS_STEP_ID, stepId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(ARGS_RECIPE_ID);
            mStepId = getArguments().getInt(ARGS_STEP_ID);

            Timber.d("Loading variables from savedInstanceState Bundle: R_id - %d, S_id - %d", mRecipeId, mStepId);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.view_recipe_step_activity, container, false);

        ButterKnife.bind(this, view);

        landscape_videoFullScreen = view.findViewById(R.id.land_recipePlayerView) != null;

        ViewRecipeStepFragmentViewModelFactory factory = new ViewRecipeStepFragmentViewModelFactory(mRecipeId, mStepId);
        mViewModel = ViewModelProviders.of(this, factory).get(ViewRecipeStepViewModel.class);
        mViewModel.getRecipeStep().observe(this, new Observer<RepositoryResponse>() {
            @Override
            public void onChanged(@Nullable RepositoryResponse repositoryResponse) {
                if (repositoryResponse.getError() != null) {
                    Timber.d(repositoryResponse.getError());
                    return;
                }
                RecipeStep recipeStep = (RecipeStep) repositoryResponse.getObject();
                if (!landscape_videoFullScreen) {
                    recipeStepHeader.setText(String.format("Step %d", recipeStep.getStepIndex() + 1));
                    recipeStepDescription.setText(recipeStep.getDescription());
                }
            }
        });

        return view;
    }

    public void setRecipeId(int id) {
        this.mRecipeId = id;
    }

    public void setStepId(int id) {
        this.mStepId = id;
    }
}
