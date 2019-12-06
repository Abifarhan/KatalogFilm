package com.example.katalogfilm.MainView;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.katalogfilm.BuildConfig;
import com.example.katalogfilm.Item.Movies;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainViewModelMovie extends ViewModel {

    private MutableLiveData<ArrayList<Movies>> listMovies = new MutableLiveData<>();

    public void setMovies(String urlMovie){

//        String urlMovie = "https://api.themoviedb.org/3/discover/movie?api_key="+ BuildConfig.API_KEY +"&language=en-US" + "&page=1";

        final ArrayList<Movies> listMovie = new ArrayList<>();
        AsyncHttpClient clientMovie = new AsyncHttpClient();

        clientMovie.get(urlMovie, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String json = new String(responseBody);
                    JSONObject object = new JSONObject(json);
                    JSONArray array = object.getJSONArray("results");
                    for (int i = 0 ; i < array.length(); i++){
                        JSONObject movie = array.getJSONObject(i);
                        Movies movies1 = new Movies();
                        movies1.setId(movie.getInt("id"));
                        movies1.setTitle(movie.getString("title"));
                        movies1.setPoster_path(movie.getString("poster_path"));
                        movies1.setOverview(movie.getString("overview"));
                        listMovie.add(movies1);
                    }
                    listMovies.postValue(listMovie);
                    Log.d("DAta","data result" + json);
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
    public LiveData<ArrayList<Movies>> getMovieData(){
        return listMovies;
    }


    public void ReleaseMovie(String judulRelease){
        final ArrayList<Movies> listMovie = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = dateFormat.format(new Date());


        String urlforRelease ="https://api.themoviedb.org/3/discover/movie?api_key="+BuildConfig.API_KEY + "&primary_release_date.gte=" +  today + "&primary_release_date.lte=" + today;


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlforRelease, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String rilis = new String(responseBody);
                    JSONObject object = new JSONObject(rilis);
                    JSONArray jsonArray = object.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject objectRelease = jsonArray.getJSONObject(i);
                        Movies  movies = new Movies();
                        movies.setId(objectRelease.getInt("id"));
                        movies.setTitle(objectRelease.getString("title"));
                        movies.setPoster_path(objectRelease.getString("poster_path"));
                        movies.setOverview(objectRelease.getString("overview"));
                        listMovie.add(movies);


                    }
                } catch (JSONException e) {
                    Log.d("gagal rilis",e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("FAilure","gagal merilis");
            }
        });
    }
}
