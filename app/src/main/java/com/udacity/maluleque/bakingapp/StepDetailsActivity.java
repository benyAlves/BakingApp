package com.udacity.maluleque.bakingapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.udacity.maluleque.bakingapp.fragments.MediaFragment;
import com.udacity.maluleque.bakingapp.fragments.StepsFragment;
import com.udacity.maluleque.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.maluleque.bakingapp.fragments.MediaFragment.THUMBNAIL_URL;
import static com.udacity.maluleque.bakingapp.fragments.MediaFragment.VIDEO_URL;

public class StepDetailsActivity extends AppCompatActivity implements  ExoPlayer.EventListener{

    public static final String SELECTED_POSITION = "selected-position";
    public static final String STEP = "selected-step";
    private ArrayList<Step> steps;
    private int selectedIndex;
    @BindView(R.id.textViewInstructions)
    TextView textViewInstructions;
    @BindView(R.id.buttonNext)
    Button buttonNext;
    @BindView(R.id.buttonPrevious)
    Button buttonPrevious;
    private Step selectedStep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            steps = extras.getParcelableArrayList(StepsFragment.STEPS);
            selectedIndex = extras.getInt(StepsFragment.SELECTED_INDEX);
            String recipeName = extras.getString(StepsFragment.RECIPE);

            getSupportActionBar().setTitle(recipeName);

            buttonNext.setOnClickListener(view -> {
                selectedIndex = selectedIndex + 1;
                populateDetails(steps.get(selectedIndex));
            });

            buttonPrevious.setOnClickListener(view -> {
                selectedIndex = selectedIndex - 1;
                populateDetails(steps.get(selectedIndex));
            });

            if (savedInstanceState == null) {

                initVideoFragment(steps.get(selectedIndex));
            }
            updateView(steps.get(selectedIndex));

        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_POSITION, selectedIndex);
        outState.putParcelable(STEP, steps.get(selectedIndex));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selectedIndex = savedInstanceState.getInt(SELECTED_POSITION);
        selectedStep = savedInstanceState.getParcelable(STEP);
        populateDetails(selectedStep);
    }

    private void populateDetails(Step step) {
        updateView(step);
        initVideoFragment(step);

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

    private void updateView(Step step) {

        textViewInstructions.setText(step.getDescription());
    }


    public void initVideoFragment(Step step) {

        String videoUrl = step.getVideoURL();
        String thumbnailUrl = step.getThumbnailURL();

        MediaFragment videoFragment = new MediaFragment();

        Bundle args = new Bundle();
        args.putString(THUMBNAIL_URL, thumbnailUrl);
        args.putString(VIDEO_URL, videoUrl);

        videoFragment.setArguments(args);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_media, videoFragment)
                .commit();
    }
}
