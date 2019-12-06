package com.example.katalogfilm.Fragment;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.katalogfilm.Adapter.ItemAdapterMovie;
import com.example.katalogfilm.BuildConfig;
import com.example.katalogfilm.Detail.DetailActivityMovie;
import com.example.katalogfilm.Item.Movies;
import com.example.katalogfilm.MainActivity;
import com.example.katalogfilm.MainView.MainViewModelMovie;
import com.example.katalogfilm.R;

import java.util.ArrayList;
import java.util.Observable;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    MainViewModelMovie mainViewModelMovie;
    private RecyclerView recyclerView;
    ItemAdapterMovie itemAdapterMovie;
    ProgressBar progressBar;


    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.option_menu,menu);

        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null){
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    String urlSearchMovie = " https://api.themoviedb.org/3/search/movie?api_key="+ BuildConfig.API_KEY +"&language=en-US&query=" + query;
                    progressBar.setVisibility(View.VISIBLE);
                    mainViewModelMovie.setMovies(urlSearchMovie);
                    Toast.makeText(getContext(), "submit ditekan" + query , Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainViewModelMovie = ViewModelProviders.of(this).get(MainViewModelMovie.class);
        View mView = inflater.inflate(R.layout.fragment_movie, container, false);
        // Inflate the layout for this fragment
        mainViewModelMovie.getMovieData().observe(this, observer);
        itemAdapterMovie = new ItemAdapterMovie();
        itemAdapterMovie.notifyDataSetChanged();
        recyclerView = mView.findViewById(R.id.rvMovie);
        progressBar = mView.findViewById(R.id.progressbar_movie);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(itemAdapterMovie);
        itemAdapterMovie.setOnItemClickMovie(new ItemAdapterMovie.onItemClickMovie() {
            @Override
            public void onItemClicked(Movies data) {
                Intent detailIntent = new Intent(getActivity(), DetailActivityMovie.class);
                detailIntent.putExtra("data",data);
                startActivity(detailIntent);
                Toast.makeText(getActivity(), "terpanggil" +data.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        mainViewModelMovie.setMovies("https://api.themoviedb.org/3/discover/movie?api_key="+ BuildConfig.API_KEY +"&language=en-US" + "&page=1");
        progressBar.setVisibility(View.VISIBLE);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);


    }

    private Observer<ArrayList<Movies>> observer = new Observer<ArrayList<Movies>>() {
        @Override
        public void onChanged(ArrayList<Movies> movies) {
            if (movies != null){
                itemAdapterMovie.setData(movies);
                progressBar.setVisibility(View.GONE);
            }
        }
    };


}
