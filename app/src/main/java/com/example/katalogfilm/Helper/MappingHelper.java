package com.example.katalogfilm.Helper;

import android.database.Cursor;

import com.example.katalogfilm.Item.Movies;
import com.example.katalogfilm.Item.Tv;
import com.example.katalogfilm.db.DatabaseContract;

import java.util.ArrayList;

public class MappingHelper {
    public static ArrayList<Movies> moviesArrayList(Cursor cursor){
        ArrayList<Movies> movies = new ArrayList<>();

        while (cursor.moveToNext()){
//            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns._ID));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.ID_MOVIE));
            String posterPath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.POSTER_ITEM));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.TITLE_ITEM));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.DESCRIPTION_ITEM));
            movies.add(new Movies(id,title,posterPath,overview));
        }
        return movies;
    }

    public static ArrayList<Tv> tvArrayList(Cursor cursor){
        ArrayList<Tv> tvs = new ArrayList<>();

        while (cursor.moveToNext()){
//            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TvColumns._ID));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.ID_TV));
            String posterPath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.POSTER_ITEM));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.TITLE_ITEM));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.DESCRIPTION_ITEM));
            tvs.add(new Tv(id,posterPath,title,overview));
        }
        return tvs;
    }
}