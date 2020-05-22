package com.udacity.maluleque.bakingapp.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.maluleque.bakingapp.R;
import com.udacity.maluleque.bakingapp.model.Recipe;
import com.udacity.maluleque.bakingapp.network.NetworkUtils;
import com.udacity.maluleque.bakingapp.viewmodel.RecipeViewModel;
import com.udacity.maluleque.bakingapp.viewmodel.RecipeViewModelFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesListFragment extends Fragment implements RecipeAdapter.RecipeItemClickListener {


    @BindView(R.id.recipesRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.textViewErrorMessage)
    TextView textViewErrorMessage;
    @BindView(R.id.buttonTryAgain)
    Button buttonTryAgain;
    RecipeShowDetailsListener recipeShowDetailsListener;
    private ArrayList<Recipe> recipesList;
    private DividerItemDecoration dividerItemDecoration;

    public RecipesListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recipes_list_fragment, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setHasFixedSize(true);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        setListLayout();

        buttonTryAgain.setOnClickListener(v -> loadData());

        showProgress();
        loadData();

        return view;

    }

    private void setListLayout() {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setListLayout();
    }

    private void loadData() {
        if (NetworkUtils.hasInternetConnection(getContext())) {
            populateRecipes();
        } else {
            showError("Please, check your internet connection and try again");
        }
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
            if (!recipesList.isEmpty()) {
                showData();
                RecipeAdapter recipeAdapter = new RecipeAdapter(recipes, RecipesListFragment.this::onRecipeItemClicked);
                recyclerView.setAdapter(recipeAdapter);
            } else {
                showError("No recipes to display try again later");
            }
        });


    }

    @Override
    public void onRecipeItemClicked(int index) {
        recipeShowDetailsListener.onShowRecipeDetails(recipesList.get(index));
    }


    public interface RecipeShowDetailsListener{
        void onShowRecipeDetails(Recipe recipe);
    }

    void showProgress() {
        recyclerView.setVisibility(View.GONE);
        textViewErrorMessage.setVisibility(View.GONE);
        buttonTryAgain.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    void showData() {
        progressBar.setVisibility(View.GONE);
        textViewErrorMessage.setVisibility(View.GONE);
        buttonTryAgain.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    void showError(String msg) {
        textViewErrorMessage.setText(msg);
        progressBar.setVisibility(View.GONE);
        textViewErrorMessage.setVisibility(View.VISIBLE);
        buttonTryAgain.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }
}
