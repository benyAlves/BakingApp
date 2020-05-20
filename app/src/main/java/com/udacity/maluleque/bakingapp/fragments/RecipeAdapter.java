package com.udacity.maluleque.bakingapp.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.maluleque.bakingapp.R;
import com.udacity.maluleque.bakingapp.model.Recipe;

import java.util.List;
import java.util.Locale;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {


    private final List<Recipe> recipes;
    private final RecipeItemClickListener recipeListener;


    public RecipeAdapter(List<Recipe> recipes, RecipeItemClickListener recipeListener){
        this.recipes = recipes;
        this.recipeListener = recipeListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.textViewName.setText(recipe.getName());
        holder.textViewServings.setText(String.format(Locale.getDefault(), "%d Servings", recipe.getServings()));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewName;
        private final TextView textViewServings;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewServings = itemView.findViewById(R.id.textViewServings);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int itemIndex = getAdapterPosition();
            recipeListener.onRecipeItemClicked(itemIndex);
        }
    }


    public interface RecipeItemClickListener{
        void onRecipeItemClicked(int index);
    }
}
