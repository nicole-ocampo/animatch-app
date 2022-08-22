package com.mobdeve.s15.group13.project;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

/*
Class: finishedViewHolder
Description: This class implements the view holder inflating finishedlist_shows_layout.xml.
 */

public class finishedViewHolder extends RecyclerView.ViewHolder{
    private ImageView poster;
    private TextView title, rating, genre, persorating;
    private Context ctx;

    public finishedViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView){
        super(itemView);

        this.poster = itemView.findViewById(R.id.finishedShowsAnimePicIv);
        this.title = itemView.findViewById(R.id.finishedShowsAnimeTitleTv);
        this.rating = itemView.findViewById(R.id.finishedShowsShowRatingTv);
        this.genre = itemView.findViewById(R.id.finishedShowsShowGenreTv);
        this.persorating = itemView.findViewById(R.id.finishedShowsYourActualRatingTv);
        ctx = itemView.getContext();
    }

    public void bindData(finishedModel t){
        this.title.setText(t.getTitle());
        this.rating.setText(t.getRating());
        this.genre.setText(t.getGenre());
        this.persorating.setText(t.getPersorating());

        Glide.with(ctx)
                .load(t.getPoster())
                .into(poster);
    }



}
