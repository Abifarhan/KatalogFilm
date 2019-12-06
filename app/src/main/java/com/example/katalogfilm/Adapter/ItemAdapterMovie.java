package com.example.katalogfilm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.katalogfilm.Item.Movies;
import com.example.katalogfilm.R;

import java.util.ArrayList;

public class ItemAdapterMovie extends RecyclerView.Adapter<ItemAdapterMovie.ViewHolder> {
    private ArrayList<Movies> mData = new ArrayList<>();
    private onItemClickMovie onItemClickMovie;

    public void setOnItemClickMovie(onItemClickMovie onItemClickMovie){
        this.onItemClickMovie = onItemClickMovie;
    }
    public void setData(ArrayList<Movies> data){
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ItemAdapterMovie.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_item,parent,false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemAdapterMovie.ViewHolder holder, int position) {
        Movies movies = mData.get(position);
        Glide.with(holder.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w342/" + movies.getPoster_path())
                .apply(new RequestOptions().override(50,50))
                .into(holder.poster_path);
        holder.title.setText(movies.getTitle());
        holder.overview.setText(movies.getOverview());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickMovie.onItemClicked(mData.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView poster_path;
        TextView overview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.judul_item);
            poster_path = itemView.findViewById(R.id.poster_item);
            overview = itemView.findViewById(R.id.deskripsi_item);
        }
    }

    public interface onItemClickMovie{
        void onItemClicked(Movies data);
    }
}
