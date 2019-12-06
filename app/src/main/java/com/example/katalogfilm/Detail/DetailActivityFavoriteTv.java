package com.example.katalogfilm.Detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.katalogfilm.Item.Tv;
import com.example.katalogfilm.R;
import com.example.katalogfilm.Widget.ImageBannerWidget;
import com.example.katalogfilm.db.DatabaseContract;

public class DetailActivityFavoriteTv extends AppCompatActivity implements View.OnClickListener{
    Tv tv;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tv = getIntent().getParcelableExtra("data");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_favorite_tv);


        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(tv.getTitle());
        }

        ImageView imgPosterFav = findViewById(R.id.poster_detail_fav_tv);
        TextView title = findViewById(R.id.judul_detail_fav_tv);
        TextView overview = findViewById(R.id.deskripsi_detail_movie_fav_tv);
        Button btnFavDelete = findViewById(R.id.btn_Fav_Delete_tv);

        if (tv != null) {
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w342" + tv.getPoster_path())
                    .into(imgPosterFav);
            title.setText(tv.getTitle());
            overview.setText(tv.getOverview());
        }

        btnFavDelete.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_Fav_Delete_tv) {
            final Uri uriForDelete = Uri.parse(DatabaseContract.TvColumns.CONTENT_URI +
                    "/" + tv.getId());
            Log.wtf("uri", String.format("%s", uriForDelete));

            if (uriForDelete != null){
                Cursor cursor = getContentResolver().query(uriForDelete, null, null, null, null);

                if (cursor!= null){
                    Log.wtf("Cursor ", String.format("%s", cursor.getCount()));
                    getContentResolver().delete(uriForDelete,null,null);
                    Toast.makeText(this, "dihapus" + tv.getTitle(), Toast.LENGTH_SHORT).show();
                    finish();
                    cursor.close();
                }
            }
        }
    }




    public static void updateWidget(Context context){
        Intent updateIntent = new Intent(context, ImageBannerWidget.class);
        updateIntent.setAction(ImageBannerWidget.UPDATE_WIDGET);
        context.sendBroadcast(updateIntent);
    }
}
