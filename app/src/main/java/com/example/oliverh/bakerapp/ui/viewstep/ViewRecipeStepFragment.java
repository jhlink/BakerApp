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
import com.example.oliverh.bakerapp.data.database.Recipe;
import com.example.oliverh.bakerapp.data.database.RecipeStep;
import com.example.oliverh.bakerapp.data.network.RepositoryResponse;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ViewRecipeStepFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String ARGS_RECIPE_ID = "RECIPE_ID";
    private static final String ARGS_STEP_ID = "STEP_ID";
    private ViewRecipeStepViewModel mViewModel;
    private int mRecipeId;
    private int mStepId;

    @BindView(R.id.recipePlayerView)
    SimpleExoPlayerView playerView;

    @BindView(R.id.tv_recipeStepHeader)
    TextView recipeStepHeader;

    @BindView(R.id.tv_recipeStepDesc)
    TextView recipeStepDescription;

    @BindView(R.id.btn_nextStep)
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

        ViewRecipeStepFragmentViewModelFactory factory = new ViewRecipeStepFragmentViewModelFactory(mRecipeId, mStepId);
        mViewModel = ViewModelProviders.of(this, factory).get(ViewRecipeStepViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_recipe_step_activity, container, false);

        ButterKnife.bind(this, view);

        mViewModel.getRecipeStep().observe(this, new Observer<RepositoryResponse>() {
            @Override
            public void onChanged(@Nullable RepositoryResponse repositoryResponse) {
                if (repositoryResponse.getError() != null) {
                    Timber.d(repositoryResponse.getError());
                    return;
                }
                RecipeStep recipeStep = (RecipeStep) repositoryResponse.getObject();
                recipeStepHeader.setText(String.format("Step %d", recipeStep.getStepIndex() + 1));
                recipeStepDescription.setText(recipeStep.getDescription());
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

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
