package com.udacity.maluleque.bakingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.maluleque.bakingapp.R;
import com.udacity.maluleque.bakingapp.model.Recipe;
import com.udacity.maluleque.bakingapp.viewmodel.RecipeViewModel;
import com.udacity.maluleque.bakingapp.viewmodel.RecipeViewModelFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesListFragment extends Fragment implements RecipeAdapter.RecipeItemClickListener {


    @BindView(R.id.recipesRecyclerView)
    RecyclerView recyclerView;
    RecipeShowDetailsListener recipeShowDetailsListener;
    private ArrayList<Recipe> recipesList;

    public RecipesListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recipes_list_fragment, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);

        populateRecipes();

        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            recipeShowDetailsListener = (RecipeShowDetailsListener)context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement RecipeShowDetailsListener");
        }
    }

    void populateRecipes(){

        RecipeViewModelFactory recipeViewModelFactory = new RecipeViewModelFactory();

        RecipeViewModel recipeViewModel = new ViewModelProvider(this, recipeViewModelFactory).get(RecipeViewModel.class);

        recipeViewModel.getRecipesLiveData().observe(this, recipes -> {
            recipesList = new ArrayList<>(recipes);
            RecipeAdapter recipeAdapter = new RecipeAdapter(recipes, RecipesListFragment.this::onRecipeItemClicked);
            recyclerView.setAdapter(recipeAdapter);
        });


    }

    @Override
    public void onRecipeItemClicked(int index) {
        recipeShowDetailsListener.onShowRecipeDetails(recipesList.get(index));
    }


    public interface RecipeShowDetailsListener{
        void onShowRecipeDetails(Recipe recipe);
    }
}
