package com.example.basefavorit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.basefavorit.Item.Movies;
import com.example.basefavorit.adapter.ItemAdapterFavorite;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static String TABLE_MOVIE = "table_movie";
    public static final String CONTENT_AUTHORITY = "com.example.katalogfilm";
    Cursor getCont;
    ArrayList<Movies> consumerMovies;
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_MOVIE)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCont = getContentResolver().query(CONTENT_URI,null,null,null,null);
        consumerMovies = new ArrayList<>();

        while (getCont.moveToNext()) {
            int id = getCont.getInt(getCont.getColumnIndexOrThrow("id_movie"));
            String poster_path = getCont.getString(getCont.getColumnIndexOrThrow("poster_path"));
            String title = getCont.getString(getCont.getColumnIndexOrThrow("title"));
            String overview = getCont.getString(getCont.getColumnIndexOrThrow("overview"));

            consumerMovies.add(new Movies(id,title,poster_path,overview));
        }

        ItemAdapterFavorite itemAdapterFavorite = new ItemAdapterFavorite();
        itemAdapterFavorite.setFavoriteMovie(consumerMovies);

        RecyclerView recyclerView = findViewById(R.id.rvMovie);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapterFavorite);
        recyclerView.setHasFixedSize(true);

        ProgressBar progressBar = findViewById(R.id.progressbar_movie);
        progressBar.setVisibility(View.GONE);
    }
}
