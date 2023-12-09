package com.example.gymcompanion.ui.homepage.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymcompanion.R;

import java.util.ArrayList;

public class DashBoardFragmentAdapter extends RecyclerView.Adapter<DashBoardFragmentAdapter.ViewHolder> {

    private ArrayList<String> list;
    private Context context;

    public DashBoardFragmentAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public DashBoardFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.progress_bar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashBoardFragmentAdapter.ViewHolder holder, int position) {
        String program = list.get(position);
        holder.program.setText(program);
        holder.program.setOnClickListener(v -> {
          // TODO: ADD FUNCTION
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView program;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            program = itemView.findViewById(R.id.program);
        }
    }
}
