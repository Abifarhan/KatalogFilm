package com.example.katalogfilm.Fragment;



import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.katalogfilm.Adapter.ItemAdapterTv;
import com.example.katalogfilm.BuildConfig;
import com.example.katalogfilm.Detail.DetailActivityMovie;
import com.example.katalogfilm.Detail.DetailActivityTv;
import com.example.katalogfilm.Item.Tv;
import com.example.katalogfilm.MainView.MainViewModelTv;
import com.example.katalogfilm.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TvFragment extends Fragment {

    MainViewModelTv mainViewModelTv;
    private RecyclerView recyclerView;
    ItemAdapterTv itemAdapterTv;
    ProgressBar progressBar;

    public TvFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu,menu);

        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null){
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint_tv));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    String urlSearchTv = "https://api.themoviedb.org/3/search/tv?api_key=" + BuildConfig.API_KEY + "&language=en-US&query=" + query;
                    progressBar.setVisibility(View.VISIBLE);
                    mainViewModelTv.setTv(urlSearchTv);
                    Toast.makeText(getContext(), "submit ditekan" + query, Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainViewModelTv = ViewModelProviders.of(this).get(MainViewModelTv.class);
        View mView = inflater.inflate(R.layout.fragment_tv,container,false);
        mainViewModelTv.getTvData().observe(this,observer);
        itemAdapterTv = new ItemAdapterTv();
        itemAdapterTv.notifyDataSetChanged();
        recyclerView = mView.findViewById(R.id.rv_tv);
        progressBar = mView.findViewById(R.id.progressbar_tv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(itemAdapterTv);
        itemAdapterTv.setOnItemClickTv(new ItemAdapterTv.onItemClickTv() {
            @Override
            public void onItemClicked(Tv data) {
                Intent detailIntent = new Intent(getActivity(), DetailActivityTv.class);
                detailIntent.putExtra("data",data);
                startActivity(detailIntent);
                Toast.makeText(getActivity(), "terpanggil" +data.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        mainViewModelTv.setTv("https://api.themoviedb.org/3/discover/tv?api_key="+ BuildConfig.API_KEY+"&language=en-US" + "&page=1");
        progressBar.setVisibility(View.VISIBLE);
        return mView;
    }


    private Observer<ArrayList<Tv>> observer = new Observer<ArrayList<Tv>>() {
        @Override
        public void onChanged(ArrayList<Tv> tvs) {
            if (tvs != null){
                itemAdapterTv.setData(tvs);
                progressBar.setVisibility(View.GONE);
            }
        }
    };
}
