package com.mobdeve.s15.group13.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/*
Class: DeckAdapter
Description: This class is the adapter used for the deck view utilized in the matching activity.
 */

public class DeckAdapter extends BaseAdapter {

    private ArrayList<ShortDetailsModel> data;
    private Context context;

    public DeckAdapter(ArrayList<ShortDetailsModel> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        // This sets the individual card view to be swiped
        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.matching_v1_layout, parent, false);
            ((TextView) v.findViewById(R.id.matchingv1_titleTv)).setText(data.get(position).getTitle());
            ((TextView) v.findViewById(R.id.matchingv1_showRatingsTv)).setText(data.get(position).getRating());
            ((TextView) v.findViewById(R.id.matchingv1_showGenresTv)).setText(data.get(position).getGenre());

            ((FloatingActionButton) v.findViewById(R.id.cardLikeBtn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(
                            v.getContext(),
                            "Swipe right to add this to your watchlist!",
                            Toast.LENGTH_LONG
                    ).show();
                }
            });

            ((FloatingActionButton) v.findViewById(R.id.cardDislikeBtn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(
                            v.getContext(),
                            "Swipe left to remove this from your deck!",
                            Toast.LENGTH_LONG
                    ).show();
                }
            });

            ImageView bg = v.findViewById(R.id.matchingv1_animeImageIv);
            Glide.with(v.getContext())
                    .load(data.get(position).getImg_url())
                    .into(bg);

        }

        // When the card view is tapped, additional information is shown to the user
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog(v.getContext(), position);
            }

        });

        return v;
    }


    public void showCustomDialog(final Context context, int position) {
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.matching_v2_layout, null);

        // This sets the layout containing additional information about the anime show
        ((TextView) v.findViewById(R.id.matchingv2Rl_titleTv)).setText(data.get(position).getTitle());
        ((TextView) v.findViewById(R.id.matchingv2Rl_showRatingTv)).setText(data.get(position).getRating());
        ((TextView) v.findViewById(R.id.matchingv2Rl_showGenreTv)).setText(data.get(position).getGenre());
        ((TextView) v.findViewById(R.id.matchingv2_EpisodesNumberTv)).setText(data.get(position).getEpisodes().toString());
        ((TextView) v.findViewById(R.id.matchingv2_StatusDetailTv)).setText(data.get(position).getStatus());
        ((TextView) v.findViewById(R.id.matchingv2_fullSynopsisTv)).setText(data.get(position).getSynopsis());

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(v);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    public int getUid (int position){
        int animeUid = data.get(position).getUid();
        return animeUid;
    }

    public int getdatacount(){
        return data.size();
    }
}