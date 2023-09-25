package com.example.gymcompanion.ui.home.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymcompanion.R;
import com.example.gymcompanion.utils.HomeFragmentModel;

import java.util.ArrayList;

public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<HomeFragmentModel> list;

    public HomeFragmentAdapter(Context context, ArrayList<HomeFragmentModel> list) {
        this.context = context;
        this.list = list;
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

        holder.program.setText(model.getProgram());
        holder.sets.setText(model.getSet());
        holder.reps.setText(model.getReps());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView reps, sets, program;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reps = itemView.findViewById(R.id.reps);
            sets = itemView.findViewById(R.id.sets);
            program = itemView.findViewById(R.id.program);
        }
    }
}
