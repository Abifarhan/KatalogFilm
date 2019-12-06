package com.example.katalogfilm.Fragment;


import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.katalogfilm.Adapter.ItemAdapterFavorite;
import com.example.katalogfilm.Detail.DetailActivityFavoriteMovie;
import com.example.katalogfilm.Helper.MappingHelper;
import com.example.katalogfilm.Item.Movies;
import com.example.katalogfilm.R;
import com.example.katalogfilm.db.DatabaseContract;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritMovieFragment extends Fragment implements LoadCallback{
    private ProgressBar progressBar;
    private ItemAdapterFavorite itemAdapterFavorite;
    private static String EXTRA_STATE = "EXTRA_STATE";


    public FavoritMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_favorit_movie, container, false);
        RecyclerView recyclerView = mView.findViewById(R.id.rv_fav_movie);
        progressBar = mView.findViewById(R.id.progressbar_fav_movie);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        itemAdapterFavorite = new ItemAdapterFavorite();
        itemAdapterFavorite.notifyDataSetChanged();
        recyclerView.setAdapter(itemAdapterFavorite);

        itemAdapterFavorite.setItemClicked(new ItemAdapterFavorite.ItemClickCallback() {
            @Override
            public void ItemClicked(Movies movies) {
                Intent detailIntent = new Intent(getActivity(), DetailActivityFavoriteMovie.class);
                detailIntent.putExtra("data_favorite",movies);
                startActivity(detailIntent);
                Toast.makeText(getActivity(), "menampilkan detail" + movies.getTitle(), Toast.LENGTH_SHORT).show();
                Log.wtf("Movie ID", String.format("%s", movies.getId()));
            }
        });

        HandlerThread handlerThread = new HandlerThread("FavoriteMovie");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        observer observer = new observer(handler,getActivity());
        getContext().getContentResolver().registerContentObserver(DatabaseContract.MovieColumns.CONTENT_URI,true,observer);
        if (savedInstanceState != null){
            ArrayList<Movies> listMovies = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (listMovies != null){
                itemAdapterFavorite.setFavoriteMovie(listMovies);
            }
        }else {
            new LoadDataAsync(getContext(),this).execute();
        }
        // Inflate the layout for this fragment
        return mView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.delete_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public static class observer extends ContentObserver{
        final Context context;

        public observer(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }
    }

    private static class LoadDataAsync extends AsyncTask<Void,Void, ArrayList<Movies>>{

        private WeakReference<Context> weakReference;
        private WeakReference<LoadCallback> loadCallbackWeakReference;

        private LoadDataAsync(Context context, LoadCallback loadCallback){
            weakReference = new WeakReference<>(context);
            loadCallbackWeakReference = new WeakReference<>(loadCallback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadCallbackWeakReference.get().preExecute();
        }

        @Override
        protected ArrayList<Movies> doInBackground(Void... voids) {
            Context context = weakReference.get();
            Cursor cursor = context.getContentResolver().query(
                    DatabaseContract.MovieColumns.CONTENT_URI,null,null,null,null
            );
            return MappingHelper.moviesArrayList(Objects.requireNonNull(cursor));
        }

        @Override
        protected void onPostExecute(ArrayList<Movies> movies) {
            super.onPostExecute(movies);
            loadCallbackWeakReference.get().postExecute(movies);
        }
    }
    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(ArrayList<Movies> favoritMovieFragmentsInterface) {
        Log.d("data favorite",String.format("%s",favoritMovieFragmentsInterface.size()));
        if (favoritMovieFragmentsInterface.size() > 0){
            itemAdapterFavorite.setFavoriteMovie(favoritMovieFragmentsInterface);
        }else {
            itemAdapterFavorite.setFavoriteMovie(new ArrayList<Movies>());
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        new LoadDataAsync(getContext(),this).execute();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, itemAdapterFavorite.getMoviesArrayList());
    }
}
