package com.udacity.maluleque.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.udacity.maluleque.bakingapp.fragments.MediaFragment;
import com.udacity.maluleque.bakingapp.fragments.StepsFragment;
import com.udacity.maluleque.bakingapp.model.Recipe;
import com.udacity.maluleque.bakingapp.model.Step;

import java.util.ArrayList;

import static com.udacity.maluleque.bakingapp.fragments.MediaFragment.THUMBNAIL_URL;
import static com.udacity.maluleque.bakingapp.fragments.MediaFragment.VIDEO_URL;

public class RecipeStepsActivity extends AppCompatActivity implements StepsFragment.StepDetailsListener {

    public static final String RECIPE = "recipe";
    public static final String SELECTED_INDEX = "selected-index";
    public static final String STEPS = "steps";

    private Recipe recipe;
    private ArrayList<Step> steps;
    private TextView textViewInstructions;
    private int selectedIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);


        Bundle extras = getIntent().getExtras();
        if (extras.containsKey("recipe")) {
            recipe = getIntent().getParcelableExtra("recipe");
            getSupportActionBar().setTitle(recipe.getName());
            steps = new ArrayList<>(recipe.getSteps());
        }
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        if (isTablet) {

            updateStepsListView();
            Step step = steps.get(selectedIndex);
            textViewInstructions = findViewById(R.id.textViewInstructions);
            updateView(step);
            initVideoFragment(step);

        } else {

            updateStepsListView();
        }
    }

    private void updateStepsListView() {
        StepsFragment stepsFragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putParcelable(StepsFragment.RECIPE, recipe);
        stepsFragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.recipes_steps_list_container, stepsFragment)
                .commit();
    }

    @Override
    public void onShowStepDetails(int index) {

        selectedIndex = index;
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        if (isTablet) {
            Step step = steps.get(index);
            textViewInstructions = findViewById(R.id.textViewInstructions);
            updateView(step);
            initVideoFragment(step);

        } else {

            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra(SELECTED_INDEX, index);
            intent.putExtra(STEPS, steps);
            intent.putExtra(RECIPE, recipe.getName());
            startActivity(intent);
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
