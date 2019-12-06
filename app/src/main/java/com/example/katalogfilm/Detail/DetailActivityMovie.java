package com.example.katalogfilm.Detail;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.katalogfilm.Adapter.ItemAdapterFavorite;
import com.example.katalogfilm.Helper.MappingHelper;
import com.example.katalogfilm.Item.Movies;
import com.example.katalogfilm.MainView.MainViewModelMovie;
import com.example.katalogfilm.R;
import com.example.katalogfilm.Widget.ImageBannerWidget;
import com.example.katalogfilm.db.DatabaseContract;
import com.example.katalogfilm.db.FavoriteHelper;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

public class DetailActivityMovie extends AppCompatActivity implements View.OnClickListener {

    Movies moviesforDetail;
    Movies moviesForDelete;
    ActionBar actionBar;
    Button favorit;
    FavoriteHelper favoriteHelper;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        moviesforDetail = getIntent().getParcelableExtra("data");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(moviesforDetail.getTitle());
        }

        actionBar =getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        moviesForDelete = moviesforDetail;


        progressBar = findViewById(R.id.progressbarr_detail);
        ImageView posterDetail = findViewById(R.id.poster_detail);
        TextView titleDetail = findViewById(R.id.judul_detail);
        TextView overview = findViewById(R.id.deskripsi_detail);
        favorit = findViewById(R.id.buttonFavMovie);
        favorit.setOnClickListener(this);

        if (moviesforDetail != null){
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w342" + moviesforDetail.getPoster_path())
                    .into(posterDetail);
            Log.d("poster_path", moviesforDetail.getPoster_path());
            titleDetail.setText(moviesforDetail.getTitle());
            overview.setText(moviesforDetail.getOverview());
            progressBar.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
        }

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
        int id = view.getId();

        if (id == R.id.buttonFavMovie) {
            int idMovie = moviesforDetail.getId();
            String poster_path = moviesforDetail.getPoster_path();
            String title = moviesforDetail.getTitle();
            String overview = moviesforDetail.getOverview();
            Cursor cursor;
            Uri uri = Uri.parse(DatabaseContract.MovieColumns.CONTENT_URI + "/" + moviesforDetail.getId());
            cursor = getContentResolver().query(uri, null, null, null, null);

            assert cursor != null;
            if (cursor.getCount() > 0) {
                Snackbar.make(view,"Film Telah ada di Daftar Film Favorit", Snackbar.LENGTH_LONG).show();
                cursor.close();
            } else {
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.MovieColumns.ID_MOVIE, idMovie);
                values.put(DatabaseContract.MovieColumns.POSTER_ITEM, poster_path);
                values.put(DatabaseContract.MovieColumns.TITLE_ITEM, title);
                values.put(DatabaseContract.MovieColumns.DESCRIPTION_ITEM, overview);
                getContentResolver().insert(DatabaseContract.MovieColumns.CONTENT_URI, values);
                Toast.makeText(this, "Succes menambahkan favorit", Toast.LENGTH_SHORT).show();
                updateWidget(this);
            }

        }

    }


    public static void updateWidget(Context context){
        Intent updateIntent = new Intent(context, ImageBannerWidget.class);
        updateIntent.setAction(ImageBannerWidget.UPDATE_WIDGET);
        context.sendBroadcast(updateIntent);
    }
}
