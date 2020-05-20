package com.udacity.maluleque.bakingapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RecipeViewModelFactory extends ViewModelProvider.NewInstanceFactory {


    public RecipeViewModelFactory() {
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeViewModel();
    }
}
