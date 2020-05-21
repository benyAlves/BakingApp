package com.udacity.maluleque.bakingapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.udacity.maluleque.bakingapp.fragments.StepsFragment;
import com.udacity.maluleque.bakingapp.model.Recipe;

public class RecipeStepsActivity extends AppCompatActivity {

    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);


        Bundle extras = getIntent().getExtras();
        if (extras.containsKey("recipe")) {
            recipe = getIntent().getParcelableExtra("recipe");
            getSupportActionBar().setTitle(recipe.getName());
        }

        StepsFragment stepsFragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putParcelable(StepsFragment.RECIPE, recipe);
        stepsFragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.recipes_steps_list_container, stepsFragment)
                .commit();
    }
}
