package com.udacity.maluleque.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.udacity.maluleque.bakingapp.fragments.StepsFragment;
import com.udacity.maluleque.bakingapp.model.Step;

import java.util.ArrayList;

public class StepDetailsActivity extends AppCompatActivity implements  ExoPlayer.EventListener{

    private ArrayList<Step> steps;
    private int selectedIndex;
    private String recipeName;
    private SimpleExoPlayer mExoPlayer;
    private PlayerView playerView;
    private TextView textViewInstructions;
    private Button buttonNext;
    private Button buttonPrevious;
    private ImageView imageView;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            steps = extras.getParcelableArrayList(StepsFragment.STEPS);
            selectedIndex = extras.getInt(StepsFragment.SELECTED_INDEX);
            recipeName = extras.getString(StepsFragment.RECIPE);

            getSupportActionBar().setTitle(recipeName);

            playerView = findViewById(R.id.playerView);
            imageView = findViewById(R.id.imageView);
            textViewInstructions = findViewById(R.id.textViewInstructions);
            buttonNext = findViewById(R.id.buttonNext);
            buttonPrevious = findViewById(R.id.buttonPrevious);

            buttonNext.setOnClickListener(view -> {
                selectedIndex = selectedIndex + 1;
                populateDetails();
            });

            buttonPrevious.setOnClickListener(view -> {
                selectedIndex = selectedIndex - 1;
                populateDetails();
            });


            populateDetails();



        }


    }

    private void populateDetails() {
        Step step = steps.get(selectedIndex);
        textViewInstructions.setText(step.getDescription());
        initPlayer(step);

        if(selectedIndex == 0){
            buttonPrevious.setEnabled(false);
        }else {
            buttonPrevious.setEnabled(true);
        }

        if(selectedIndex >= steps.size()-1){
            buttonNext.setEnabled(false);
        }else {
            buttonNext.setEnabled(true);
        }
    }


    private void initPlayer(Step step){

        if(!step.getVideoURL().trim().isEmpty()) {
            playerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
            playerView.setPlayer(mExoPlayer);


            Uri uri = Uri.parse(step.getVideoURL());
            MediaSource mediaSource = buildMediaSource(uri);

            /*mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(currentWindow, playbackPosition);*/
            mExoPlayer.prepare(mediaSource);
            
            
        }else if(!step.getThumbnailURL().trim().isEmpty()){

            Uri uri = Uri.parse(step.getThumbnailURL());

            String mimeType = getContentResolver().getType(uri);

            if(mimeType.contains("image")) {

                playerView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);

                Picasso.get()
                        .load(uri)
                        .into(imageView);
            }else if(mimeType.contains("image")){

                playerView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);

                mExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
                playerView.setPlayer(mExoPlayer);

                MediaSource mediaSource = buildMediaSource(uri);
                mExoPlayer.prepare(mediaSource);
            }
        }else {
            playerView.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        }

    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, "exoplayer-codelab");
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
