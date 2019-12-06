package com.example.basefavorit.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.basefavorit.Item.Movies;
import com.example.basefavorit.R;


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
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_item,parent,false);
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
                Toast.makeText(view.getContext(), "Item di klik "+view.getId(), Toast.LENGTH_SHORT).show();
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
            posterFavorite = itemView.findViewById(R.id.poster_item);
            titleFavorite = itemView.findViewById(R.id.judul_item);
            descriptionFavorite = itemView.findViewById(R.id.deskripsi_item);
        }
    }

    public interface ItemClickCallback{
        void ItemClicked(Movies movies);
    }
}
