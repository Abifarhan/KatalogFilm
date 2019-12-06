package com.example.katalogfilm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.katalogfilm.Helper.DatabaseHelper;

public class FavoriteHelper {
    private static final String TABLE_MOVIE = DatabaseContract.MovieColumns.TABLE_MOVIE;
    private static final String TABLE_TV = DatabaseContract.TvColumns.TABLE_TV;
    private static DatabaseHelper helper;

    private static FavoriteHelper INSTANCE;

    private static SQLiteDatabase database;

    private FavoriteHelper(Context context){
        helper = new DatabaseHelper(context);
    }

    public static FavoriteHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new FavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException{
        database = helper.getWritableDatabase();
    }

    public Cursor queryFavoriteMovie(){
        return database.query(TABLE_MOVIE,null,null,null,null,null, DatabaseContract.MovieColumns.ID_MOVIE + " ASC");
    }
    public Cursor queryFavoriteTv(){
        return database.query(TABLE_TV,null,null,null,null,null, DatabaseContract.TvColumns.ID_TV + " ASC");
    }

    public Cursor queryFavoriteMovieByTitle(String title){
        return database.query(TABLE_MOVIE,null,DatabaseContract.MovieColumns.ID_MOVIE + " = ?", new String[]{title},null,null,null,null);
    }

    public Cursor queryFavoriteTvByTitle(String title){
        return database.query(TABLE_TV,null,DatabaseContract.TvColumns.ID_TV + " = ?", new String[]{title},null,null,null,null);
    }

    public long insertFavoriteMovie(ContentValues values){
        return database.insert(TABLE_MOVIE,null,values);
    }
    public long insertFavoriteTv(ContentValues values){
        return database.insert(TABLE_TV,null,values);
    }

    public int deleteFavoriteMovieByTitle(String id){
        return database.delete(TABLE_MOVIE,DatabaseContract.MovieColumns.ID_MOVIE + " = ?", new String[]{id});
    }
    public int deleteFavoriteTvByTitle(String id){
        return database.delete(TABLE_TV,DatabaseContract.TvColumns.ID_TV + " = ?", new String[]{id});
    }
}
