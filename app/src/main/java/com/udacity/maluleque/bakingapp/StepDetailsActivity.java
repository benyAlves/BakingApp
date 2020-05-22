package com.udacity.maluleque.bakingapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.exoplayer2.ExoPlayer;
import com.udacity.maluleque.bakingapp.fragments.StepDetailsFragment;
import com.udacity.maluleque.bakingapp.fragments.StepsFragment;
import com.udacity.maluleque.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsActivity extends AppCompatActivity implements  ExoPlayer.EventListener{

    public static final String STEP = "step";
    private ArrayList<Step> steps;
    private int selectedIndex;
    @BindView(R.id.buttonNext)
    Button buttonNext;
    @BindView(R.id.buttonPrevious)
    Button buttonPrevious;

    

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
        updateView(step);

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

        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(STEP, step);
        stepDetailsFragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.step_details_container, stepDetailsFragment)
                .commit();
    }

}
