package com.example.katalogfilm.Detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.katalogfilm.Item.Tv;
import com.example.katalogfilm.R;
import com.example.katalogfilm.db.DatabaseContract;
import com.google.android.material.snackbar.Snackbar;

public class DetailActivityTv extends AppCompatActivity implements View.OnClickListener {


    Tv tv;
    ActionBar actionBar;
    Button favorit;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tv = getIntent().getParcelableExtra("data");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv);


        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(tv.getTitle());
        }


        progressBar = findViewById(R.id.progressbarr_detailTv);
        ImageView posterDetail = findViewById(R.id.poster_detail_Tv);
        TextView titleDetail = findViewById(R.id.judul_detail_Tv);
        TextView overview = findViewById(R.id.deskripsi_detail_Tv);
        favorit = findViewById(R.id.buttonFavTv);
        favorit.setOnClickListener(this);


        if (tv != null) {
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w342" + tv.getPoster_path())
                    .into(posterDetail);
            Log.d("poster_path", tv.getPoster_path());
            titleDetail.setText(tv.getTitle());
            overview.setText(tv.getOverview());
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }

    }


    // Aksi ketika Back Button ditekan
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.buttonFavTv) {
            int idMovie = tv.getId();
            String poster_path = tv.getPoster_path();
            String title = tv.getTitle();
            String overview = tv.getOverview();
            Cursor cursor;
            Uri uri = Uri.parse(DatabaseContract.TvColumns.CONTENT_URI + "/" + tv.getId());
            cursor = getContentResolver().query(uri, null, null, null, null);

            assert cursor != null;
            if (cursor.getCount() > 0) {
                Snackbar.make(view, "Film Telah ada di Daftar Film Favorit", Snackbar.LENGTH_LONG).show();
                cursor.close();
            } else {
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.TvColumns.ID_TV, idMovie);
                values.put(DatabaseContract.TvColumns.POSTER_ITEM, poster_path);
                values.put(DatabaseContract.TvColumns.TITLE_ITEM, title);
                values.put(DatabaseContract.TvColumns.DESCRIPTION_ITEM, overview);
                getContentResolver().insert(DatabaseContract.TvColumns.CONTENT_URI, values);
                Toast.makeText(this, "Succes menambahkan favorit", Toast.LENGTH_SHORT).show();
                Log.wtf("send data", String.format("%s", values));
                Log.wtf("Movie ID ", String.format("%s", tv.getId()));
            }


        }
    }
}