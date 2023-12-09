package com.example.gymcompanion.ui.homepage.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymcompanion.R;
import com.example.gymcompanion.utils.DashBoardFragmentModel;

import java.util.ArrayList;

public class DashBoardFragmentAdapterCards extends RecyclerView.Adapter<DashBoardFragmentAdapterCards.ViewHolder> {
    private final ArrayList<DashBoardFragmentModel> models;
    private final Context context;

    public DashBoardFragmentAdapterCards(ArrayList<DashBoardFragmentModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public DashBoardFragmentAdapterCards.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.progress_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashBoardFragmentAdapterCards.ViewHolder holder, int position) {
        DashBoardFragmentModel model = models.get(position);
        holder.program.setText(model.getProgressName());
        String temp = model.getValue() + "KG";
        holder.value.setText(temp);
        holder.progressBar.setProgress(model.getProgress());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView program;
        private final TextView value;
        private final ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            program = itemView.findViewById(R.id.program);
            value = itemView.findViewById(R.id.value);
        }
    }
}
