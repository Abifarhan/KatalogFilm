package com.example.katalogfilm.Provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.katalogfilm.Item.Tv;
import com.example.katalogfilm.db.DatabaseContract;
import com.example.katalogfilm.db.FavoriteHelper;

import java.net.URI;

public class FavoriteProvider extends ContentProvider {
    private static final int MOVIE = 1;
    private static final int MOVIE_TITLE = 2;
    private static final int TV = 3;
    private static final int TV_TITLE = 4;
    private FavoriteHelper favoriteHelper;

    public FavoriteProvider(){

    }
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.MovieColumns.TABLE_MOVIE, MOVIE);
        URI_MATCHER.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.MovieColumns.TABLE_MOVIE + "/#", MOVIE_TITLE);
        URI_MATCHER.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.TvColumns.TABLE_TV, TV);
        URI_MATCHER.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.TvColumns.TABLE_TV+ "/#", TV_TITLE);

    }
    @Override
    public boolean onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(getContext());
        favoriteHelper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        Cursor cursor;
        switch (URI_MATCHER.match(uri)){
            case MOVIE:
                cursor = favoriteHelper.queryFavoriteMovie();
                break;
            case MOVIE_TITLE:
                cursor = favoriteHelper.queryFavoriteMovieByTitle(uri.getLastPathSegment());
                break;
            case TV:
                cursor = favoriteHelper.queryFavoriteTv();
                break;
            case TV_TITLE:
                cursor = favoriteHelper.queryFavoriteTvByTitle(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long insert;
        switch (URI_MATCHER.match(uri)){
            case MOVIE:
                insert = favoriteHelper.insertFavoriteMovie(contentValues);
                break;
            case TV:
                insert = favoriteHelper.insertFavoriteTv(contentValues);
                break;
            default:
                insert = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse(uri+ "/" +insert);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int delete;
        switch (URI_MATCHER.match(uri)){
            case MOVIE_TITLE:
                delete = favoriteHelper.deleteFavoriteMovieByTitle(uri.getLastPathSegment());
                break;
            case TV_TITLE :
                delete = favoriteHelper.deleteFavoriteTvByTitle(uri.getLastPathSegment());
                break;
            default:
                delete = 0;
                break;
        }
        if (getContext() != null){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return delete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
