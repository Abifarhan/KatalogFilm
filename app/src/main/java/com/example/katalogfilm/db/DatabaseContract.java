package com.example.katalogfilm.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String CONTENT_AUTHORITY = "com.example.katalogfilm";

    public static final class FavoriteColumns implements BaseColumns{
        public static final String TABLE_FAVORITE = "table_favorite";
        public static final String ID_FAVORITE = "id_favorite";
        public static final String POSTER_ITEM = "poster_path";
        public static final String TITLE_ITEM = "title";
        public static final String DESCRIPTION_ITEM = "overview";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
                .authority(CONTENT_AUTHORITY)
                .appendPath(TABLE_FAVORITE)
                .build();
    }

    public static final class MovieColumns implements  BaseColumns {
        public static final String TABLE_MOVIE = "table_movie";
        public static final String ID_MOVIE = "id_movie";
        public static final String POSTER_ITEM = "poster_path";
        public static final String TITLE_ITEM = "title";
        public static final String DESCRIPTION_ITEM = "overview";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
                .authority(CONTENT_AUTHORITY)
                .appendPath(TABLE_MOVIE)
                .build();
    }

    public static final class TvColumns implements BaseColumns{
        public static final String TABLE_TV = "table_tv";
        public static final String ID_TV = "id_tv";
        public static final String POSTER_ITEM = "poster_path";
        public static final String TITLE_ITEM = "title";
        public static final String DESCRIPTION_ITEM = "overview";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
                .authority(CONTENT_AUTHORITY)
                .appendPath(TABLE_TV)
                .build();
    }


}
