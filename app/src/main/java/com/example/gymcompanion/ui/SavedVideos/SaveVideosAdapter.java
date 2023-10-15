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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymcompanion.R;
import com.example.gymcompanion.ui.exercise.PreviewVideoActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

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
        holder.title.setText(uri.getLastPathSegment());
        holder.date.setText(uri.getLastPathSegment().substring(0, 10));
        holder.card.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Returning Notice");
            builder.setMessage("Are you sure you want to delete this video?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                int pos = holder.getAdapterPosition();
                list.remove(pos);
                File file = new File(Objects.requireNonNull(uri.getPath()));
                if (file.delete()) {
                    Toast.makeText(context, "deleting video successfully!", Toast.LENGTH_SHORT).show();
                    notifyItemRemoved(pos);
                    return;
                }
                Toast.makeText(context, "an error occurred while deleting the video, please try again later!", Toast.LENGTH_SHORT).show();

            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            builder.create().show();
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView thumbnail;
        private final RelativeLayout card;
        private final TextView title, date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            card = itemView.findViewById(R.id.card);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
        }
    }
    private Bitmap getThumbnail(Uri uri) {
        return ThumbnailUtils.createVideoThumbnail(uri.getPath() , MediaStore.Images.Thumbnails.MINI_KIND);
    }
}
