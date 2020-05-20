package com.udacity.maluleque.bakingapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.maluleque.bakingapp.model.Recipe;
import com.udacity.maluleque.bakingapp.repository.RecipesRepository;

import java.util.List;

public class RecipeViewModel extends ViewModel {

    private RecipesRepository recipesRepository;

    public RecipeViewModel(){
        recipesRepository = new RecipesRepository();
    }

    public LiveData<List<Recipe>> getRecipesLiveData(){
        return recipesRepository.getRecipesMutableLiveData();
    }
}
