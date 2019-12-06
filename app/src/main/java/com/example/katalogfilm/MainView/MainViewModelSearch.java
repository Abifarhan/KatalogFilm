package com.example.katalogfilm.MainView;

import androidx.lifecycle.ViewModel;

import com.example.katalogfilm.BuildConfig;
import com.example.katalogfilm.Item.Movies;
import com.example.katalogfilm.Item.Tv;

import java.util.ArrayList;

public class MainViewModelSearch extends ViewModel {

    public void searchItem(String name, String languange, final String movie){


        String urlForMovie =  "https://api.themoviedb.org/3/search/movie?api_key="+ BuildConfig.API_KEY+ "&language=en-US&query=";
        String urlForTv;


//        final ArrayList<Movies>
    }
}
