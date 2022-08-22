package com.mobdeve.s15.group13.project;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
Class: finishedAdapter
Description: This class is the adapter used for the recycler view utilized in the finished activity.
 */

public class finishedAdapter extends RecyclerView.Adapter<finishedViewHolder>{

    private ArrayList<finishedModel> data;
    public finishedAdapter(ArrayList<finishedModel> data){ this.data = data; }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public finishedViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.finishedlist_shows_layout, parent, false);
        finishedViewHolder myViewHolder = new finishedViewHolder(v);

        // Pass all the details to the intent so that the new intent wouldn't need to access the database anymore.
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), finishedDetailsActivity.class);

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("FINISHED_POSTER", data.get(myViewHolder.getAdapterPosition()).getPoster());
                i.putExtra("FINISHED_PERSONALRATING", data.get(myViewHolder.getAdapterPosition()).getPersorating());
                i.putExtra("FINISHED_TITLE", data.get(myViewHolder.getAdapterPosition()).getTitle());
                i.putExtra("FINISHED_RATING", data.get(myViewHolder.getAdapterPosition()).getRating());
                i.putExtra("FINISHED_GENRE", data.get(myViewHolder.getAdapterPosition()).getGenre());
                i.putExtra("FINISHED_NOTES", data.get(myViewHolder.getAdapterPosition()).getNotes());
                i.putExtra("FINISHED_SYNOPSIS", data.get(myViewHolder.getAdapterPosition()).getSynopsis());
                i.putExtra("FINISHED_EPISODE", data.get(myViewHolder.getAdapterPosition()).getEpisode());
                i.putExtra("FINISHED_STATUS", data.get(myViewHolder.getAdapterPosition()).getStatus());
                view.getContext().startActivity(i);

            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull finishedViewHolder holder, int position) {
        holder.bindData(data.get(getItemViewType(position)));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
