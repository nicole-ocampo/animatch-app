package com.mobdeve.s15.group13.project;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*
Class: introViewHolder
Description: This class implements the view holder inflating intro_itemlayout.xml.
 */

public class introViewHolder extends RecyclerView.ViewHolder{
    private ImageView bg;
    private TextView header, details;

    public introViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView){
        super(itemView);

        this.bg = itemView.findViewById(R.id.st_bgIv);
        this.header = itemView.findViewById(R.id.st_headerTv);
        this.details = itemView.findViewById(R.id.st_detailsTv);
    }

    public void setBgIv(int img) { this.bg.setImageResource(img);}

    public void setHeaderTv(String x) { this.header.setText(x); }

    public void setDetailsTv(String x) { this.details.setText(x); }
}
