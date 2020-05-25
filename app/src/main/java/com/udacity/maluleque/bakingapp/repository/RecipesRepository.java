package com.udacity.maluleque.bakingapp.repository;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.udacity.maluleque.bakingapp.model.Recipe;
import com.udacity.maluleque.bakingapp.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class RecipesRepository {

    MutableLiveData<List<Recipe>> mutableLiveData = new MediatorLiveData();

    public RecipesRepository(){

        RetrofitClient.createBakingService().retriveRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(response.isSuccessful()){
                    mutableLiveData.setValue(response.body()    );
                    Timber.d("successful");
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Timber.e(t);
                mutableLiveData.setValue(null);
            }
        });
    }

    public MutableLiveData<List<Recipe>> getRecipesMutableLiveData() {
        return mutableLiveData;
    }
}
