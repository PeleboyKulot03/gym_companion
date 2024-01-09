package com.example.gymcompanion.ui.DetailedProfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymcompanion.R;
import com.example.gymcompanion.staticValues.DifferentExercise;
import com.example.gymcompanion.utils.DetailedProfileModel;

import java.util.ArrayList;
import java.util.Objects;

public class DetailedProfileAdapter extends RecyclerView.Adapter<DetailedProfileAdapter.ViewHolder> {
    private final ArrayList<DetailedProfileModel> models;
    private final Context context;
    private final DifferentExercise differentExercise;

    public DetailedProfileAdapter(ArrayList<DetailedProfileModel> models, Context context) {
        this.models = models;
        this.context = context;
        differentExercise = new DifferentExercise(context);
    }

    @NonNull
    @Override
    public DetailedProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.program_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailedProfileAdapter.ViewHolder holder, int position) {
        DetailedProfileModel model = models.get(position);
        String sets = model.getSet() + " sets";
        String reps = model.getReps() + " reps";
        String weight = model.getWeight() + " lbs";

        holder.program.setText(model.getExercise());
        holder.sets.setText(sets);
        holder.reps.setText(reps);
        holder.weights.setText(weight);
        holder.image.setImageDrawable(Objects.requireNonNull(differentExercise.getDRAWABLES().get(model.getExercise())).get(0));

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView reps;
        private final TextView sets;
        private final TextView program;
        private final TextView weights;
        private final ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            weights = itemView.findViewById(R.id.weights);
            reps = itemView.findViewById(R.id.reps);
            sets = itemView.findViewById(R.id.sets);
            program = itemView.findViewById(R.id.program);
            image = itemView.findViewById(R.id.image);
        }
    }
}
