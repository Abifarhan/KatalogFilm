package com.example.katalogfilm.Widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.example.katalogfilm.Helper.MappingHelper;
import com.example.katalogfilm.Item.Movies;
import com.example.katalogfilm.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.katalogfilm.db.DatabaseContract.MovieColumns.CONTENT_URI;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private final List<Bitmap> mWidgetItems = new ArrayList<>();
    private final Context mContext;
    private Cursor cursor;

    public StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null){
            cursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();

        cursor = mContext.getContentResolver().query(CONTENT_URI,null,null,null,null);
        ArrayList<Movies> movies = MappingHelper.moviesArrayList(cursor);
        Binder.restoreCallingIdentity(identityToken);
        for (Movies moviesWidget: movies){
            try {
                Bitmap bitmap = Glide.with(mContext)
                        .asBitmap()
                        .load("https://image.tmdb.org/t/p/w342"+ moviesWidget.getPoster_path())
                        .submit(180,180)
                        .get();
                Log.d("fail",moviesWidget.getPoster_path());
                mWidgetItems.add(bitmap);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setImageViewBitmap(R.id.imageView,mWidgetItems.get(i));

        Bundle extras = new Bundle();
        extras.putInt(ImageBannerWidget.EXTRA_ITEM,i);
        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView,fillIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
