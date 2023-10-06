package com.example.gymcompanion.ui.homepage.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymcompanion.R;
import com.example.gymcompanion.tutorial_activity;
import com.example.gymcompanion.utils.HomeFragmentModel;

import java.util.ArrayList;

public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder> {
    private final Context context;
    private final Activity activity;
    private final ArrayList<HomeFragmentModel> list;

    public HomeFragmentAdapter(Context context, Activity activity, ArrayList<HomeFragmentModel> list) {
        this.context = context;
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public HomeFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.program_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFragmentAdapter.ViewHolder holder, int position) {
        HomeFragmentModel model = list.get(position);
        String sets = model.getSet() + " sets";
        String reps = model.getReps() + " reps";
        String weight = model.getWeight() + " lbs";

        holder.program.setText(model.getProgram());
        holder.sets.setText(sets);
        holder.reps.setText(reps);
        holder.weights.setText(weight);

        if (model.getIsDone()) {
            holder.finishInfos.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
            String timeValue = "Time Spent: " + model.getTime();
            String accuracy = "Accuracy: " + model.getAccuracy();
            holder.timeSpent.setText(timeValue);
            holder.accuracy.setText(accuracy);
        }

        holder.card.setOnClickListener(view -> {
            Intent intent = new Intent(context, tutorial_activity.class);
            intent.putExtra("exercise", model.getProgram());
            intent.putExtra("isDone", model.getIsDone());
            activity.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView reps;
        private final TextView sets;
        private final TextView program;
        private final TextView weights;
        private final TextView accuracy;
        private final TextView timeSpent;

        private final RelativeLayout card;
        private final LinearLayout finishInfos;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            weights = itemView.findViewById(R.id.weights);
            reps = itemView.findViewById(R.id.reps);
            sets = itemView.findViewById(R.id.sets);
            program = itemView.findViewById(R.id.program);
            accuracy = itemView.findViewById(R.id.accuracy);
            timeSpent = itemView.findViewById(R.id.timeSpent);
            card = itemView.findViewById(R.id.card);
            finishInfos = itemView.findViewById(R.id.finishInfos);

        }
    }
}
