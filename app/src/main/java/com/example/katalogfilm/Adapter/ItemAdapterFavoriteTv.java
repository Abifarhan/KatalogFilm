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
import com.example.katalogfilm.Item.Tv;
import com.example.katalogfilm.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ItemAdapterFavoriteTv extends RecyclerView.Adapter<ItemAdapterFavoriteTv.ViewHolder> {
    private ArrayList<Tv> tvArrayList = new ArrayList<>();
    private ItemClickCallbackTv callbackTv;

    public void setItemClicked(ItemClickCallbackTv itemClickCallbackTv){
        callbackTv = itemClickCallbackTv;
    }

    public void setFavoriteTv(ArrayList<Tv> items){
        tvArrayList.clear();
        tvArrayList.addAll(items);
        notifyDataSetChanged();
    }

    public ArrayList<Tv> getTvArrayList(){
        return tvArrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_fav_tv,parent,false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Tv tvData = tvArrayList.get(position);
        Glide.with(holder.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w342" + tvData.getPoster_path())
                .apply(new RequestOptions().override(50,50))
                .into(holder.posterFavorite);
        holder.titleFavorite.setText(tvData.getTitle());
        holder.descriptionFavorite.setText(tvData.getOverview());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbackTv.ItemClicked(tvArrayList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tvArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView posterFavorite;
        TextView titleFavorite,descriptionFavorite;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            posterFavorite = itemView.findViewById(R.id.poster_fav_tv);
            titleFavorite = itemView.findViewById(R.id.judul_fav_tv);
            descriptionFavorite = itemView.findViewById(R.id.deskripsi_fav_tv);
        }
    }

    public interface ItemClickCallbackTv{
        void ItemClicked(Tv tv);
    }
}
