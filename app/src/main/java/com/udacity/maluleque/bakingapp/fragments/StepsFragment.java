package com.udacity.maluleque.bakingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.maluleque.bakingapp.R;
import com.udacity.maluleque.bakingapp.model.Ingredient;
import com.udacity.maluleque.bakingapp.model.Recipe;

import java.util.List;

public class StepsFragment extends Fragment {

    public static final String RECIPE = "recipe";
    TextView textViewIngredients;

    public StepsFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.steps_fragment, container, false);

        textViewIngredients = view.findViewById(R.id.textViewIngredients);
        RecyclerView recyclerViewSteps = view.findViewById(R.id.stepsRecyclerView);

        Bundle arguments = getArguments();
        if (arguments != null) {
            Recipe recipe = arguments.getParcelable(RECIPE);
            showIngredients(recipe.getIngredients());
        }

        return view;

    }

    private void showIngredients(List<Ingredient> ingredients) {
        StringBuilder builder = new StringBuilder();
        for (Ingredient ingredient : ingredients) {
            builder.append("\n").append(ingredient.getIngredient());
        }
        textViewIngredients.setText(builder.toString());
    }
}
