package com.example.android.popularMovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.android.popularMovies.Utilities.NetworkUtils;
import com.example.android.popularMovies.Utilities.PopularMoviesJsonUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements Adapter.MovieAdapterOnClickHandler {

    private RecyclerView recyclerView;
    private Adapter mAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private final  static String popularMovie = "movie/popular";
    private final  static String topRatedMovie = "movie/top_rated";


    private final FetchPopularMovies fpm =  new FetchPopularMovies();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        recyclerView = (RecyclerView) findViewById(R.id.rv_popularMovie);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        layoutManager.setSpanCount(2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mAdapter = new Adapter(this);
        recyclerView.setAdapter(mAdapter);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        fpm.execute(popularMovie);

    }
    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(List movieDetails) {

        try {

             /*Fetching the data from ArrayList with key value pair
                * key   value
                * 1     vote average
                * 2     title
                * 3     poster path
                * 4     plot synopsis
                * 5     release date
                * */
            String voteAverage =movieDetails.get(1).toString();
            String originalTitle = movieDetails.get(2).toString();
            String posterPath = movieDetails.get(3).toString();
            String overview = movieDetails.get(4).toString();
            String releaseDate = movieDetails.get(5).toString();

            SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.android.popularMovies", Context.MODE_PRIVATE);
            sharedPreferences.edit().putString("voteAverage", voteAverage).apply();
            sharedPreferences.edit().putString("title", originalTitle).apply();
            sharedPreferences.edit().putString("posterPath", posterPath).apply();
            sharedPreferences.edit().putString("overview", overview).apply();
            sharedPreferences.edit().putString("releaseDate", releaseDate).apply();
            Intent intent = new Intent(this, MovieDetails.class);
            startActivity(intent);
        }catch(Exception e ){
            e.printStackTrace();
        }

    }


     class FetchPopularMovies extends AsyncTask<String, Void, List<ArrayList>> {


        List<ArrayList> pmDataSet=null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<ArrayList> doInBackground(String... sortParameter) {

            String sortParam = sortParameter[0];

            URL popularMovieURL = NetworkUtils.popularMovieBuildUrl(sortParam);

            try {
                String jsonPMResponse = NetworkUtils
                        .getResponseFromHttpUrl(popularMovieURL);


               return PopularMoviesJsonUtil
                        .getSimplePMStringsFromJson(jsonPMResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }


        @Override
        protected void onPostExecute(List<ArrayList> data){


            pmDataSet = data;
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (pmDataSet != null) {
                mAdapter.setPMData(pmDataSet);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void setDataForMenu(){

        List<ArrayList> as = new ArrayList<>();
        recyclerView.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mAdapter.setPMData(as);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            setDataForMenu();
            new FetchPopularMovies().execute(popularMovie);
            return true;
        }

        if(id == R.id.most_popular){
            setDataForMenu();
            new FetchPopularMovies().execute(popularMovie);
            return true;

        }
            if(id == R.id.top_rated){
                setDataForMenu();
                new FetchPopularMovies().execute(topRatedMovie);
                return true;
            }
        return super.onOptionsItemSelected(item);
    }


}
