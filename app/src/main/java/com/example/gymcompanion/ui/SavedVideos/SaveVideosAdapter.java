package com.example.gymcompanion.ui.SavedVideos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymcompanion.R;

import java.util.ArrayList;

public class SaveVideosAdapter extends RecyclerView.Adapter<SaveVideosAdapter.ViewHolder> {

    private final ArrayList<Uri> list;
    private final Context context;
    private final Activity activity;

    public SaveVideosAdapter(ArrayList<Uri> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public SaveVideosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.saved_videos_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaveVideosAdapter.ViewHolder holder, int position) {
        Uri uri = list.get(position);
        holder.thumbnail.setImageBitmap(getThumbnail(uri));
        holder.card.setOnClickListener(view -> {
            Intent intent = new Intent(context, PlayVideoActivity.class);
            intent.putExtra("uri", uri.toString());
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView thumbnail;
        private final RelativeLayout card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            card = itemView.findViewById(R.id.card);
        }
    }
    private Bitmap getThumbnail(Uri uri) {
        return ThumbnailUtils.createVideoThumbnail(uri.getPath() , MediaStore.Images.Thumbnails.MINI_KIND);
    }
}
