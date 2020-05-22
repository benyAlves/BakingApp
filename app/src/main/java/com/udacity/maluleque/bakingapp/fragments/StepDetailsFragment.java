package com.udacity.maluleque.bakingapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.udacity.maluleque.bakingapp.R;
import com.udacity.maluleque.bakingapp.model.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.maluleque.bakingapp.StepDetailsActivity.STEP;

public class StepDetailsFragment extends Fragment {


    @BindView(R.id.playerView)
    PlayerView playerView;
    @BindView(R.id.textViewInstructions)
    TextView textViewInstructions;
    @BindView(R.id.imageView)
    ImageView imageView;
    private SimpleExoPlayer mExoPlayer;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private Step step;

    public StepDetailsFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);

        ButterKnife.bind(this, view);

        Bundle arguments = getArguments();
        if (arguments != null) {
            step = arguments.getParcelable(STEP);
            populateDetails(step);
        }

        return view;

    }


    private void populateDetails(Step step) {
        textViewInstructions.setText(step.getDescription());
        initPlayer(step);
    }


    private void initPlayer(Step step) {

        if (!step.getVideoURL().trim().isEmpty()) {
            playerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext());
            playerView.setPlayer(mExoPlayer);


            Uri uri = Uri.parse(step.getVideoURL());
            MediaSource mediaSource = buildMediaSource(uri);

            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(currentWindow, playbackPosition);
            mExoPlayer.prepare(mediaSource);


        } else if (!step.getThumbnailURL().trim().isEmpty()) {

            Uri uri = Uri.parse(step.getThumbnailURL());

            String mimeType = getContext().getContentResolver().getType(uri);

            if (mimeType.contains("image")) {

                playerView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);

                Picasso.get()
                        .load(uri)
                        .into(imageView);
            } else if (mimeType.contains("image")) {

                playerView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);

                mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext());
                playerView.setPlayer(mExoPlayer);

                MediaSource mediaSource = buildMediaSource(uri);
                mExoPlayer.prepare(mediaSource);
            }
        } else {
            playerView.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        }

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
            mExoPlayer.release();
            mExoPlayer = null;
        }
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
}
