package com.udacity.maluleque.bakingapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.maluleque.bakingapp.R;
import com.udacity.maluleque.bakingapp.StepDetailsActivity;
import com.udacity.maluleque.bakingapp.model.Ingredient;
import com.udacity.maluleque.bakingapp.model.Recipe;
import com.udacity.maluleque.bakingapp.model.Step;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StepsFragment extends Fragment implements StepsAdapter.StepItemClickListener {

    public static final String RECIPE = "recipe";
    public static final String SELECTED_INDEX = "selected-index";
    public static final String STEPS = "steps";
    TextView textViewIngredients;
    ArrayList<Step> steps;
    private RecyclerView recyclerView;
    private Recipe recipe;

    public StepsFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.steps_fragment, container, false);

        textViewIngredients = view.findViewById(R.id.textViewIngredients);
        recyclerView = view.findViewById(R.id.stepsRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        Bundle arguments = getArguments();
        if (arguments != null) {
            recipe = arguments.getParcelable(RECIPE);
            showIngredients(recipe.getIngredients());
            steps = new ArrayList<>(recipe.getSteps());
            showSteps();
        }

        return view;

    }

    private void showSteps() {
        StepsAdapter stepsAdapter = new StepsAdapter(steps, this::onStepItemClicked);
        recyclerView.setAdapter(stepsAdapter);
    }

    private void showIngredients(List<Ingredient> ingredients) {
        StringBuilder builder = new StringBuilder();

        DecimalFormat df = new DecimalFormat();
        df.setRoundingMode(RoundingMode.DOWN);

        for (Ingredient ingredient : ingredients) {
            builder.append("\n\u2022 ")
                    .append(df.format(ingredient.getQuantity()))
                    .append(ingredient.getMeasure().toLowerCase())
                    .append(" ")
                    .append(ingredient.getIngredient());
        }
        textViewIngredients.setText(builder.toString());
    }

    @Override
    public void onStepItemClicked(int index) {
        Intent intent = new Intent(getContext(), StepDetailsActivity.class);
        intent.putExtra(SELECTED_INDEX, index);
        intent.putExtra(STEPS, steps);
        intent.putExtra(RECIPE, recipe.getName());
        startActivity(intent);
    }
}
