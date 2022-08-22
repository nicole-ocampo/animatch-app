package com.mobdeve.s15.group13.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
Class: introAdapter
Description: This class implements the adapter for the recycler view implemented in the Main Activity.
 */

public class introAdapter extends RecyclerView.Adapter<introViewHolder>{

    private ArrayList<introModel> data;

    public introAdapter(ArrayList<introModel> data){
        this.data = data;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public introViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.intro_itemlayout, parent, false);
        introViewHolder myViewHolder = new introViewHolder(v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull introViewHolder holder, int position) {

        holder.setHeaderTv(data.get(position).getHeader());
        holder.setDetailsTv(data.get(position).getDetails());
        holder.setBgIv(data.get(position).getImg());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
