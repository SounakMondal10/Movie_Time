package com.sounakmondal.movietime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class Adaptery extends RecyclerView.Adapter<Adaptery.myViewHolder> {

    private Context mContext;
    private List<MovieModelClass> mData;

    public Adaptery(Context mContext, List<MovieModelClass> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.movie_item, parent,false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        try {
            holder.name.setText(mData.get(position).getName());
            holder.rating.setText(mData.get(position).getRating());
            //holder.description.setText(mData.get(position).getDescription());
            Glide.with(mContext)
                    .load(mData.get(position).getImg())
                    .into(holder.img);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView name, rating;
        ImageView img;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_txt);
            rating = itemView.findViewById(R.id.rating_txt);
            img = itemView.findViewById(R.id.imageView);
        }
    }


    //item list copy in adapter
    public static List<MovieModelClass> movieList1 = new ArrayList<>();

    public static void setMovieListUpdatedCopy(List<MovieModelClass> movieList)
    {
        movieList1 = new ArrayList<>();
        movieList1 = movieList;
    }

    public static List<MovieModelClass> getMovieListUpdatedCopy()
    {
        return movieList1;
    }

}
