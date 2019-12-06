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

import java.util.ArrayList;

public class ItemAdapterTv extends RecyclerView.Adapter<ItemAdapterTv.ViewHolder> {
    private ArrayList<Tv> mData = new ArrayList<>();
    private onItemClickTv onItemClickTv;

    public void setOnItemClickTv(onItemClickTv onItemClickTv){
        this.onItemClickTv = onItemClickTv;
    }
    public void setData(ArrayList<Tv> data){
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_tv,parent,false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Tv tv = mData.get(position);
        Glide.with(holder.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w342/" + tv.getPoster_path())
                .apply(new RequestOptions().override(50,50))
                .into(holder.poster_path);
        holder.title.setText(tv.getTitle());
        holder.overview.setText(tv.getOverview());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickTv.onItemClicked(mData.get(holder.getAdapterPosition()));
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
            title = itemView.findViewById(R.id.judul_tv);
            poster_path = itemView.findViewById(R.id.poster_tv);
            overview = itemView.findViewById(R.id.deskripsi_tv);
        }
    }

    public interface onItemClickTv{
        void onItemClicked(Tv data);
    }
}
