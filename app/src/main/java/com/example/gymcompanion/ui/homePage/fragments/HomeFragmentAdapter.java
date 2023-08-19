package com.example.gymcompanion.ui.homePage.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
