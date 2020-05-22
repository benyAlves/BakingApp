package com.udacity.maluleque.bakingapp.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.maluleque.bakingapp.R;
import com.udacity.maluleque.bakingapp.model.Step;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {


    private final List<Step> steps;
    private final StepItemClickListener stepListener;


    public StepsAdapter(List<Step> steps, StepItemClickListener stepListener){
        this.steps = steps;
        this.stepListener = stepListener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_step_item, parent, false);
        return new StepViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        Step step = steps.get(position);
        int drawable = 0;

        if(!step.getVideoURL().trim().isEmpty()){
            drawable = R.drawable.ic_play_arrow_red_24dp;
        }else if(!step.getThumbnailURL().trim().isEmpty()) {
            drawable = R.drawable.ic_image_red_24dp;
        }
        holder.textViewDescription.setText(step.getShortDescription());

        holder.textViewDescription.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public interface StepItemClickListener{
        void onStepItemClicked(int index);
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewDescription;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int itemIndex = getAdapterPosition();
            stepListener.onStepItemClicked(itemIndex);
        }
    }
}
