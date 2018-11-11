package com.example.oliverh.bakerapp.ui.viewstep;

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

import com.example.oliverh.bakerapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeVideoFragment extends Fragment implements Player.EventListener {

    private static final String ARGS_VIDEO_URL = "RECIPE_VIDEO_URL";
    private static final String TAG = RecipeVideoFragment.class.getSimpleName();
    private static MediaSessionCompat mMediaSession;
    private static final DefaultBandwidthMeter BANDWIDTH_METER =
            new DefaultBandwidthMeter();
    private PlaybackStateCompat.Builder mStateBuilder;
    private SimpleExoPlayer mExoPlayer;
    private String mVideoUrl;

    @BindView(R.id.recipePlayerView)
    @Nullable
    PlayerView playerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeVideoFragment() {
    }


    public static RecipeVideoFragment newInstance(String videoUrl) {
        Bundle args = new Bundle();

        RecipeVideoFragment fragment = new RecipeVideoFragment();
        args.putString(ARGS_VIDEO_URL, videoUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mVideoUrl = getArguments().getString(ARGS_VIDEO_URL);

            Timber.d("Loading variables from savedInstanceState Bundle: V_URL - %s", mVideoUrl);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_video_view, container, false);

        if (view instanceof PlayerView) {
            ButterKnife.bind(this, view);

            initializeMediaSession(view.getContext());
            initializePlayer(view.getContext());

            if (mVideoUrl == null) {
                Timber.d("No Video Exist ");
            }
        }

        return view;
    }

    public void setAndInitializePlayer(Bundle bundle) {
        this.setArguments(bundle);
        mVideoUrl = bundle.getString(ARGS_VIDEO_URL);
        createAndSetMediaSource();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
    }

    private void initializePlayer(Context context) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelection.Factory adaptiveTrackSelectFactor = new AdaptiveTrackSelection.Factory();

            TrackSelector trackSelector = new DefaultTrackSelector(adaptiveTrackSelectFactor);
            LoadControl loadControl = new DefaultLoadControl();
            RenderersFactory renderersFactory = new DefaultRenderersFactory(context);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, renderersFactory, trackSelector, loadControl, null, BANDWIDTH_METER);

            playerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            createAndSetMediaSource();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(ARGS_VIDEO_URL, mVideoUrl);
        super.onSaveInstanceState(outState);
    }

    private void createAndSetMediaSource() {
        if (mVideoUrl != null) {
            // Prepare the MediaSource.
            Uri uri = Uri.parse(mVideoUrl);
            MediaSource mediaSource = buildMediaSource(uri);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory manifestDataSourceFactory = new DefaultHttpDataSourceFactory("ua");

        DashChunkSource.Factory dashChunkSourceFactory =
                new DefaultDashChunkSource.Factory(
                        new DefaultHttpDataSourceFactory("ua", BANDWIDTH_METER));

        //new DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory).createMediaSource(uri);

        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("ua")).
                createMediaSource(uri);

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
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new RecipeVideoSessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == Player.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == Player.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

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

        @Override
        public void onSkipToNext() {
            long seekNext = mExoPlayer.getCurrentPosition() + 5;
            mExoPlayer.seekTo(seekNext);
        }
    }

}
