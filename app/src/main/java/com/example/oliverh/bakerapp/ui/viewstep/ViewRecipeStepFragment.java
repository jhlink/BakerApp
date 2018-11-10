package com.example.oliverh.bakerapp.ui.viewstep;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oliverh.bakerapp.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

public class ViewRecipeStepFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String ARGS_RECIPE_ID = "RECIPE_ID";
    private static final String ARGS_STEP_ID = "STEP_ID";
    private ViewRecipeStepViewModel mViewModel;
    private int mRecipeId;
    private int mStepId;

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
