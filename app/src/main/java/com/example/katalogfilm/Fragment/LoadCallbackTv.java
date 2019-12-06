package com.example.katalogfilm.Fragment;

import com.example.katalogfilm.Item.Tv;

import java.util.ArrayList;

interface LoadCallbackTv {
    void preExecute();
    void postExecute(ArrayList<Tv> favoritTvFragmentsInterface);
}
