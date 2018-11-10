package com.example.oliverh.bakerapp.ui.viewstep;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.oliverh.bakerapp.R;
import com.example.oliverh.bakerapp.data.database.RecipeStep;
import com.example.oliverh.bakerapp.data.network.RepositoryResponse;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ViewRecipeStepFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String ARGS_RECIPE_ID = "RECIPE_ID";
    private static final String ARGS_STEP_ID = "STEP_ID";
    private static final String TAG = ViewRecipeStepFragment.class.getSimpleName();
    private static MediaSessionCompat mMediaSession;
    private static final DefaultBandwidthMeter BANDWIDTH_METER =
            new DefaultBandwidthMeter();
    private PlaybackStateCompat.Builder mStateBuilder;
    private SimpleExoPlayer mExoPlayer;
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
        final View view = inflater.inflate(R.layout.view_recipe_step_activity, container, false);

        ButterKnife.bind(this, view);

        initializeMediaSession(view.getContext());

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
                initializePlayer(view.getContext(), recipeStep.getVideoUrl());
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

    private void initializePlayer(Context context, String mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
            playerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(context, "BakerApp");
            Uri uri = Uri.parse(mediaUri);
            MediaSource mediaSource = buildMediaSource(uri);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        DefaultExtractorsFactory extractorsFactory =
                new DefaultExtractorsFactory();
        DataSource.Factory manifestDataSourceFactory =
                new DefaultHttpDataSourceFactory("ua");

        DashChunkSource.Factory dashChunkSourceFactory =
                new DefaultDashChunkSource.Factory(
                        new DefaultHttpDataSourceFactory("ua", BANDWIDTH_METER));

        ExtractorMediaSource videoSource =
                new ExtractorMediaSource(uri, manifestDataSourceFactory, extractorsFactory,
                        null, null);

        return videoSource;
    }


    private void initializeMediaSession(Context context) {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(context, TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new RecipeVideoSessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
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

    private class RecipeVideoSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }
}
