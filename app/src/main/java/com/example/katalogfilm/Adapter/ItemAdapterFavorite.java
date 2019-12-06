package com.example.katalogfilm.Adapter;

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

public class ItemAdapterFavorite extends RecyclerView.Adapter<ItemAdapterFavorite.ViewHolder> {
    private ArrayList<Movies> moviesArrayList = new ArrayList<>();
    private ItemClickCallback callback ;

    public void setItemClicked(ItemClickCallback itemClickCallback){
        callback = itemClickCallback;
    }

    public void setFavoriteMovie(ArrayList<Movies> items){
        moviesArrayList.clear();
        moviesArrayList.addAll(items);
        notifyDataSetChanged();
    }

    public ArrayList<Movies> getMoviesArrayList(){
        return moviesArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_fav_movie,parent,false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Movies moviesData = moviesArrayList.get(position);

        Glide.with(holder.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w342" + moviesData.getPoster_path())
                .apply(new RequestOptions().override(50,50))
                .into(holder.posterFavorite);
        holder.titleFavorite.setText(moviesData.getTitle());
        holder.descriptionFavorite.setText(moviesData.getOverview());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.ItemClicked(moviesArrayList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView posterFavorite;
        TextView titleFavorite,descriptionFavorite;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            posterFavorite = itemView.findViewById(R.id.poster_fav_movie);
            titleFavorite = itemView.findViewById(R.id.judul_fav_movie);
            descriptionFavorite = itemView.findViewById(R.id.deskripsi_fav_movie);
        }
    }

    public interface ItemClickCallback{
        void ItemClicked(Movies movies);
    }
}
