package com.mobdeve.s15.group13.project;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
Class: matchesAdapter
Description: This class is the adapter used for the recycler view utilized in the matches activity.
 */

public class matchesAdapter extends RecyclerView.Adapter<matchlistViewHolder>{

    private ArrayList<matchesModel> data;
    private ActivityResultLauncher<Intent> matchesLauncher;

    public matchesAdapter(ArrayList<matchesModel> data, ActivityResultLauncher<Intent> matchesLauncher){
        this.data = data;
        this.matchesLauncher = matchesLauncher;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public matchlistViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.matchlist_matches_layout, parent, false);
        matchlistViewHolder myViewHolder = new matchlistViewHolder(v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull matchlistViewHolder holder, int position) {

        holder.setPoster(data.get(position).getPoster(), holder.itemView.getContext());
        holder.setTitle(data.get(position).getTitle());
        holder.setGenre(data.get(position).getGenre());
        holder.setRating(data.get(position).getRating());

        // Pass all the details to the intent so that the new intent wouldn't need to access the database anymore.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), matchesDetailsActivity.class);
                i.putExtra("VIEW_POS", position);
                i.putExtra("MATCHES_POSTER", data.get(position).getPoster());
                i.putExtra("MATCHES_ID", data.get(position).getUid());
                i.putExtra("MATCHES_TITLE", data.get(position).getTitle());
                i.putExtra("MATCHES_RATING", data.get(position).getRating());
                i.putExtra("MATCHES_GENRE", data.get(position).getGenre());

                i.putExtra("MATCHES_SYNOPSIS", data.get(position).getSynopsis());
                i.putExtra("MATCHES_EPISODE", data.get(position).getEpisode());
                i.putExtra("MATCHES_STATUS", data.get(position).getStatus());
                matchesLauncher.launch(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
