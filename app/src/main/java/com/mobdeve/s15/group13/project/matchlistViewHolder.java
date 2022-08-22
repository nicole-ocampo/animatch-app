package com.mobdeve.s15.group13.project;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


/*
Class: matchlistViewHolder
Description: This class implements the view holder inflating matchlist_matches_layout.xml.
 */

public class matchlistViewHolder extends RecyclerView.ViewHolder{

    private ImageView poster;
    private TextView title, rating, genre;

    public matchlistViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView){
        super(itemView);

        this.poster = itemView.findViewById(R.id.animePicIv);
        this.title = itemView.findViewById(R.id.matchlistAnimeTitleTv);
        this.rating = itemView.findViewById(R.id.matchlistShowRatingTv);
        this.genre = itemView.findViewById(R.id.matchlistShowGenreTv);

    }

    public void setPoster(String posterurl, android.content.Context ctx) {
        Glide.with(ctx)
                .load(posterurl)
                .into(poster);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }
    public void setRating(String rating) {
        this.rating.setText(rating);
    }
    public void setGenre(String genre) {
        this.genre.setText(genre);
    }
}
