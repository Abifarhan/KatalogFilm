package com.example.katalogfilm.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.katalogfilm.db.DatabaseContract;

public class DatabaseHelper extends SQLiteOpenHelper{

    public DatabaseHelper(Context context){
        super(context,"katalogfilm",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_MOVIE);
        sqLiteDatabase.execSQL(TABLE_TV);
        sqLiteDatabase.execSQL(TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.MovieColumns.TABLE_MOVIE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TvColumns.TABLE_TV);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.FavoriteColumns.TABLE_FAVORITE);
    }

    private static final String TABLE_MOVIE = String.format(
            "CREATE TABLE %s" +
                    " (%s INTEGER NOT NULL PRIMARY KEY,"+
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL,"+
                    " %s TEXT NOT NULL)",
            DatabaseContract.MovieColumns.TABLE_MOVIE,
            DatabaseContract.MovieColumns.ID_MOVIE,
            DatabaseContract.MovieColumns.POSTER_ITEM,
            DatabaseContract.MovieColumns.TITLE_ITEM,
            DatabaseContract.MovieColumns.DESCRIPTION_ITEM
    );

    private static final String TABLE_TV = String.format(
            "CREATE TABLE %s" +
                    " (%s INTEGER NOT NULL PRIMARY KEY,"+
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL,"+
                    " %s TEXT NOT NULL)",
            DatabaseContract.TvColumns.TABLE_TV,
            DatabaseContract.TvColumns.ID_TV,
            DatabaseContract.TvColumns.POSTER_ITEM,
            DatabaseContract.TvColumns.TITLE_ITEM,
            DatabaseContract.TvColumns.DESCRIPTION_ITEM
    );

    private static final String TABLE_FAVORITE = String.format(
            "CREATE TABLE %s" +
                    " (%s INTEGER NOT NULL PRIMARY KEY,"+
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL,"+
                    " %s TEXT NOT NULL)",
            DatabaseContract.FavoriteColumns.TABLE_FAVORITE,
            DatabaseContract.FavoriteColumns.ID_FAVORITE,
            DatabaseContract.FavoriteColumns.POSTER_ITEM,
            DatabaseContract.FavoriteColumns.TITLE_ITEM,
            DatabaseContract.FavoriteColumns.DESCRIPTION_ITEM
    );
}
