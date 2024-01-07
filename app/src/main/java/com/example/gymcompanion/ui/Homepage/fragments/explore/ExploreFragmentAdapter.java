package com.example.gymcompanion.ui.Homepage.fragments.explore;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gymcompanion.R;
import com.example.gymcompanion.utils.ExploreFragmentModel;

import java.util.ArrayList;

public class ExploreFragmentAdapter extends RecyclerView.Adapter<ExploreFragmentAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<ExploreFragmentModel> models;

    public ExploreFragmentAdapter(Context context, ArrayList<ExploreFragmentModel> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public ExploreFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.information_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreFragmentAdapter.ViewHolder holder, int position) {
        ExploreFragmentModel model = models.get(position);
        holder.name.setText(model.getDisplayName());
        holder.height.setText(model.getHeight());
        holder.weight.setText(model.getWeight());
        holder.level.setText(model.getExperience());
        Log.i("beybi","onBindViewHolder: " + model.getImageSrc());

        if (model.getImageSrc() != null) {
            Glide.with(context)
                    .load(model.getImageSrc())
                    .circleCrop()
                    .into(holder.displayPicture)
                    .waitForLayout();
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView weight;
        private final TextView height;
        private final TextView level;
        private final ImageView displayPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            weight = itemView.findViewById(R.id.weight);
            height = itemView.findViewById(R.id.height);
            level = itemView.findViewById(R.id.level);
            displayPicture = itemView.findViewById(R.id.displayPicture);
        }
    }
}
