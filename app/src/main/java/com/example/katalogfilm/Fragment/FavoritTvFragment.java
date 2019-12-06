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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.katalogfilm.Adapter.ItemAdapterFavoriteTv;
import com.example.katalogfilm.Detail.DetailActivityFavoriteTv;
import com.example.katalogfilm.Detail.DetailActivityTv;
import com.example.katalogfilm.Helper.MappingHelper;
import com.example.katalogfilm.Item.Tv;
import com.example.katalogfilm.R;
import com.example.katalogfilm.db.DatabaseContract;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritTvFragment extends Fragment implements LoadCallbackTv{
    ProgressBar progressBar;
    ItemAdapterFavoriteTv itemAdapterFavoriteTv;
    RecyclerView recyclerView;
    private static String EXTRA_STATE = "EXTRA_STATE";


    public FavoritTvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_favorit_tv,container,false);
        recyclerView = mView.findViewById(R.id.rv_fav_tv);
        progressBar = mView.findViewById(R.id.progressbar_fav_tv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        itemAdapterFavoriteTv = new ItemAdapterFavoriteTv();
        itemAdapterFavoriteTv.notifyDataSetChanged();
        recyclerView.setAdapter(itemAdapterFavoriteTv);

        itemAdapterFavoriteTv.setItemClicked(new ItemAdapterFavoriteTv.ItemClickCallbackTv() {
            @Override
            public void ItemClicked(Tv tv) {
                Intent detailIntent = new Intent(getActivity(), DetailActivityFavoriteTv.class);
                detailIntent.putExtra("data",tv);
                startActivity(detailIntent);
                Toast.makeText(getActivity(), "Menampilkan Detail", Toast.LENGTH_SHORT).show();
            }
        });
        HandlerThread handlerThread = new HandlerThread("FavoriteTv");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        observer observer = new observer(handler,getActivity());
        getContext().getContentResolver().registerContentObserver(DatabaseContract.TvColumns.CONTENT_URI,true,observer);
        if (savedInstanceState != null){
            ArrayList<Tv> listTv = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (listTv != null){
                itemAdapterFavoriteTv.setFavoriteTv(listTv);
            }
        }
        else {
            new LoadDataAsync(getContext(),this).execute();
        }
        // Inflate the layout for this fragment
        return mView;
    }

    public static class observer extends ContentObserver{
        private Context context;

        public observer(Handler handler, Context context1) {
            super(handler);
            context = context1;
        }
    }

    private static class LoadDataAsync extends AsyncTask<Void,Void, ArrayList<Tv>>{

        private WeakReference<Context> weakReference;
        private WeakReference<LoadCallbackTv> loadCallbackTvWeakReference;

        private LoadDataAsync(Context context, LoadCallbackTv loadCallbackTv){
            weakReference = new WeakReference<>(context);
            loadCallbackTvWeakReference = new WeakReference<>(loadCallbackTv);
        }
        @Override
        protected ArrayList<Tv> doInBackground(Void... voids) {
            Context context = weakReference.get();
            Cursor cursor = context.getContentResolver().query(
                    DatabaseContract.TvColumns.CONTENT_URI,null,null,null,null
            );
            return MappingHelper.tvArrayList(cursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Tv> tvs) {
            super.onPostExecute(tvs);
            loadCallbackTvWeakReference.get().postExecute(tvs);
        }
    }
    @Override
    public void preExecute(){

    }

    @Override
    public void postExecute(ArrayList<Tv> favoritTvFragmentsInterface) {
        Log.d("data favorit",String.format("%s",favoritTvFragmentsInterface.size()));
        if (favoritTvFragmentsInterface.size() > 0){
            itemAdapterFavoriteTv.setFavoriteTv(favoritTvFragmentsInterface);
        }else {
            itemAdapterFavoriteTv.setFavoriteTv(new ArrayList<Tv>());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadDataAsync(getContext(),this).execute();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE,itemAdapterFavoriteTv.getTvArrayList());
    }
}
