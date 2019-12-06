package com.example.katalogfilm.Fragment;

import com.example.katalogfilm.Item.Movies;

import java.util.ArrayList;

interface LoadCallback {
    void preExecute();
    void postExecute(ArrayList<Movies> favoritMovieFragmentsInterface);
}
