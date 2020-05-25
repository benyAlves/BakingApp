package com.udacity.maluleque.bakingapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.udacity.maluleque.bakingapp.R;
import com.udacity.maluleque.bakingapp.network.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MediaFragment extends Fragment implements Player.EventListener {


    public static final String THUMBNAIL_URL = "thumbnail-url";
    public static final String VIDEO_URL = "video-url";
    public static final String KEY_READY = "play-when-ready";
    public static final String KEY_POSITION = "playback-position";
    public static final String KEY_INDEX = "current-window";
    public static final String KEY_URL = "video-url-key";
    private static final String TAG = "MediaFragment";
    @BindView(R.id.playerView)
    PlayerView playerView;
    @BindView(R.id.imageView)
    ImageView imageView;
    private SimpleExoPlayer mExoPlayer;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private String thumbnailUrl;
    private String videoUrl;
    private Uri videoUri;

    public MediaFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_media, container, false);

        ButterKnife.bind(this, view);

        Bundle arguments = getArguments();
        if (arguments != null) {
            thumbnailUrl = arguments.getString(THUMBNAIL_URL);
            videoUrl = arguments.getString(VIDEO_URL);
            populateDetails(thumbnailUrl, videoUrl);
        }

        if (savedInstanceState != null) {
            playWhenReady = savedInstanceState.getBoolean(KEY_READY);
            playbackPosition = savedInstanceState.getLong(KEY_POSITION);
            currentWindow = savedInstanceState.getInt(KEY_INDEX);
            videoUrl = savedInstanceState.getString(KEY_URL);
            videoUri = Uri.parse(videoUrl);
        }

        return view;

    }


    private void populateDetails(String thumbnailUrl, String videoUrl) {
        if (videoUrl.endsWith(".mp4")) {

            videoUri = Uri.parse(videoUrl);


        } else {

            if (thumbnailUrl.equals("")) {

                playerView.setUseController(false);
                playerView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);

            } else {

                if (thumbnailUrl.endsWith(".mp4")) {

                    videoUri = Uri.parse(videoUrl);

                } else {

                    playerView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);

                    Picasso.get()
                            .load(thumbnailUrl)
                            .error(R.drawable.novideo)
                            .into(imageView);
                }
            }
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_READY, playWhenReady);
        outState.putLong(KEY_POSITION, playbackPosition);
        outState.putInt(KEY_INDEX, currentWindow);
        outState.putString(KEY_URL, videoUrl);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(getContext(), "exoplayer-codelab");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            playWhenReady = mExoPlayer.getPlayWhenReady();
            playbackPosition = mExoPlayer.getCurrentPosition();
            currentWindow = mExoPlayer.getCurrentWindowIndex();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    public void initExoPlayer(final Uri videoUri) {

        if (NetworkUtils.hasInternetConnection(getContext())) {
            if (mExoPlayer == null) {

                if (videoUri != null) {
                    mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext());

                    mExoPlayer.addListener(this);


                    playerView.setPlayer(mExoPlayer);

                    mExoPlayer.prepare(buildMediaSource(videoUri));
                    mExoPlayer.seekTo(currentWindow, playbackPosition);
                    mExoPlayer.setPlayWhenReady(playWhenReady);
                }
            }
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.no_internet_connection_message), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        initExoPlayer(videoUri);
    }

    @Override
    public void onResume() {
        super.onResume();
        initExoPlayer(videoUri);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        switch (error.type) {
            case ExoPlaybackException.TYPE_SOURCE:
                Timber.e("TYPE_SOURCE: %s", error.getSourceException().getMessage());
                break;

            case ExoPlaybackException.TYPE_RENDERER:
                Timber.e("TYPE_RENDERER: %s", error.getRendererException().getMessage());
                break;

            case ExoPlaybackException.TYPE_UNEXPECTED:
                Timber.e("TYPE_UNEXPECTED: %s", error.getUnexpectedException().getMessage());
                break;

            default:
                Timber.e("TYPE_OTHER: %s", error.getUnexpectedException().getMessage());
        }
        Toast.makeText(getContext(), "Sorry, We cant play this media file now", Toast.LENGTH_SHORT).show();
    }
}
