package com.example.katalogfilm.MainView;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.katalogfilm.BuildConfig;
import com.example.katalogfilm.Item.Tv;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainViewModelTv extends ViewModel {

    private MutableLiveData<ArrayList<Tv>> listMutableTv = new MutableLiveData<>();

    public void setTv(String urlTv){
//        String urlTv = "https://api.themoviedb.org/3/discover/tv?api_key="+ BuildConfig.API_KEY+"&language=en-US" + "&page=1";

        final ArrayList<Tv> listTv = new ArrayList<>();
        AsyncHttpClient clientTv = new AsyncHttpClient();

        clientTv.get(urlTv, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String json = new String(responseBody);
                    JSONObject object = new JSONObject(json);
                    JSONArray array = object.getJSONArray("results");
                    for (int i = 0 ; i < array.length() ; i++){
                        JSONObject tv = array.getJSONObject(i);
                        Tv tv1 = new Tv();
                        tv1.setId(tv.getInt("id"));
                        tv1.setTitle(tv.getString("name"));
                        tv1.setPoster_path(tv.getString("poster_path"));
                        tv1.setOverview(tv.getString("overview"));
                        listTv.add(tv1);
                    }
                    listMutableTv.postValue(listTv);
                    Log.d("DAta Tv","data result" + json);
                }catch (JSONException e){
                    e.printStackTrace();
                    Log.d("DAta","data result" + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Failed","failed" + error.getMessage());
            }
        });
    }

    public LiveData<ArrayList<Tv>> getTvData(){
        return listMutableTv;
    }

    public void setDetailTv(int id){
        String urlTv = "https://api.themoviedb.org/3/tv/"+id+"?api_key="+BuildConfig.API_KEY +"&language=en-US";
        final MutableLiveData<Tv> tvMutableLiveData = new MutableLiveData<>();
        final ArrayList<Tv> listTv = new ArrayList<>();
        AsyncHttpClient clientTv = new AsyncHttpClient();

        clientTv.get(urlTv, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String json = new String(responseBody);
                    JSONObject object = new JSONObject(json);
//                    JSONArray array = object.getJSONArray("results");
//                    for (int i = 0 ; i < array.length() ; i++){
//                        JSONObject tv = array.getJSONObject(i);
//                        Tv tv1 = new Tv();
//                        tv1.setId(tv.getInt("id"));
//                        tv1.setTitle(tv.getString("name"));
//                        tv1.setPoster_path(tv.getString("poster_path"));
//                        tv1.setOverview(tv.getString("overview"));
//                        listTv.add(tv1);
//                    }
                    Tv detailTv = new Tv();
                    detailTv.setId(object.getInt("id"));
                    detailTv.setTitle(object.getString("title"));
                    detailTv.setPoster_path(object.getString("poster_path"));
                    detailTv.setOverview(object.getString("overwiew"));
                    tvMutableLiveData.postValue(detailTv);
                    Log.d("DAta Tv","data result" + json);
                }catch (JSONException e){
                    e.printStackTrace();
                    Log.d("DAta","data result" + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Failed","failed" + error.getMessage());
            }
        });

}}
