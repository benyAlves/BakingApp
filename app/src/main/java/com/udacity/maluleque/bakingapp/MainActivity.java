package com.udacity.maluleque.bakingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.udacity.maluleque.bakingapp.fragments.RecipesListFragment;
import com.udacity.maluleque.bakingapp.model.Recipe;
import com.udacity.maluleque.bakingapp.viewmodel.RecipeViewModel;
import com.udacity.maluleque.bakingapp.viewmodel.RecipeViewModelFactory;

import java.util.List;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RecipesListFragment.RecipeShowDetailsListener {

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

    }
}
