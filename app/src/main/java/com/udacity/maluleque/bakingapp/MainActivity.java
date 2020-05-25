package com.udacity.maluleque.bakingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.test.espresso.IdlingResource;

import com.udacity.maluleque.bakingapp.fragments.RecipesListFragment;
import com.udacity.maluleque.bakingapp.model.Recipe;

public class MainActivity extends AppCompatActivity implements RecipesListFragment.RecipeShowDetailsListener {

    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecipesListFragment recipesListFragment = new RecipesListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.recipes_list_container, recipesListFragment)
                .commit();
    }

    @Override
    public void onShowRecipeDetails(Recipe recipe) {
        Intent intent = new Intent(this, RecipeStepsActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
