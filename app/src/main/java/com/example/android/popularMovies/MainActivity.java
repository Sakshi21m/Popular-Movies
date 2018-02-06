package com.example.android.popularMovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularMovies.Data.MovieContract;
import com.example.android.popularMovies.Utilities.NetworkUtils;
import com.example.android.popularMovies.Utilities.PopularMoviesJsonUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements Adapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<ArrayList>> {

    private RecyclerView recyclerView;
    private Adapter mAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private final  static String POPULAR_MOVIE = "movie/popular";
    private final  static String TOP_RATED_MOVIE = "movie/top_rated";
    private final  static String FAVORITE_MOVIE = "favorite";
    private final static int MOVIE_LOADER = 20;
    private final  static String SEARCH_TYPE = "searchType";
    //preferenceStatus == 1 for most_popular, ==>2 for top_rated, ==>3 for favorite
    //this is implemented to solve the back button navigation, if fav movies page is selected,
    //and we go on movie details page, the back button should bring the fav movies rather than
    //default sort criteria. Hence we need preferenceStatus and shared preferences.
    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int preferenceStatus;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv_popularMovie);
        GridLayoutManager gridLayoutManager;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(this,2);
        }else{
            gridLayoutManager = new GridLayoutManager(this,4);

        }
        recyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new Adapter(this);
        recyclerView.setAdapter(mAdapter);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        Bundle bundle = new Bundle();
        sharedPreferences = this.getSharedPreferences("com.example.android.popularMovies", Context.MODE_PRIVATE);

        preferenceStatus = sharedPreferences.getInt("preferenceStatus",1);
        if(preferenceStatus==1)
            bundle.putString(SEARCH_TYPE,POPULAR_MOVIE );
        else if (preferenceStatus==2)
            bundle.putString(SEARCH_TYPE,TOP_RATED_MOVIE );
        else if (preferenceStatus==3)
            bundle.putString(SEARCH_TYPE,FAVORITE_MOVIE );

        LoaderManager lm = getSupportLoaderManager();
            lm.initLoader(MOVIE_LOADER,bundle,this);

    }
    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(List movieDetails) {

        try {

             /*Fetching the data from ArrayList with key value pair
                * key   value
                * 0     movie id
                * 1     vote average
                * 2     title
                * 3     poster path
                * 4     plot synopsis
                * 5     release date
                * */
            String movieId = movieDetails.get(0).toString();
            String voteAverage =movieDetails.get(1).toString();
            String originalTitle = movieDetails.get(2).toString();
            String posterPath = movieDetails.get(3).toString();
            String overview = movieDetails.get(4).toString();
            String releaseDate = movieDetails.get(5).toString();

            Bundle bundle = new Bundle();
            bundle.putString("movieId",movieId);
            bundle.putString("voteAverage", voteAverage);
            bundle.putString("title", originalTitle);
            bundle.putString("posterPath", posterPath);
            bundle.putString("overview", overview);
            bundle.putString("releaseDate", releaseDate);


            Intent intent = new Intent(this, MovieDetails.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }catch(Exception e ){
            e.printStackTrace();
        }

    }

    @Override
    public Loader<List<ArrayList>> onCreateLoader(int id,final  Bundle args) {
        return new AsyncTaskLoader<List<ArrayList>>(this) {
            private List<ArrayList> jSonResult;
            @Override
            public void onStartLoading() {
                super.onStartLoading();
                if (args == null) {
                    return;
                }
                mErrorMessageDisplay.setVisibility(View.INVISIBLE);
                mLoadingIndicator.setVisibility(View.VISIBLE);
                //caching the Loader
                if(jSonResult!=null){
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                    deliverResult(jSonResult);
                }else{
                    forceLoad();
                }

            }

            @Override
            public List<ArrayList> loadInBackground() {
                String sortParam = args.getString(SEARCH_TYPE);

                if (!sortParam.isEmpty()){
                    if (sortParam.equals(FAVORITE_MOVIE)) {

                        List<ArrayList> outerArray = new ArrayList();

                        try {
                            Cursor cursor;

                            cursor = getContentResolver().query(MovieContract.MovieEntries.CONTENT_URI,
                                    null,
                                    null,
                                    null,
                                    null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                while (!cursor.isAfterLast()) {
                                    ArrayList<String> innerArray = new ArrayList<>();
                                    innerArray.add(0, cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntries.COLUMN_MOVIE_ID)));
                                    innerArray.add(1, cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntries.COLUMN_USER_RATING)));
                                    innerArray.add(2, cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntries.COLUMN_MOVIE_TITLE)));
                                    innerArray.add(3, cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntries.COLUMN_POSTER_PATH)));
                                    innerArray.add(4, cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntries.COLUMN_MOVIE_PLOT)));
                                    innerArray.add(5, cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntries.COLUMN_RELEASE_DATE)));
                                    outerArray.add(innerArray);
                                    cursor.moveToNext();
                                }
                                cursor.close();

                            }


                        } catch (Exception e) {
                            System.out.println("error is " + e);

                        }

                        return outerArray;
                    } else {

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
                    }}else{return null;}
            }
            //This method is used to make caching of loader
            @Override
            public void deliverResult(List<ArrayList> ls){
                jSonResult = ls;
                super.deliverResult(ls);
            }

        };
    }



    @Override
    public void onLoadFinished(Loader<List<ArrayList>> loader, List<ArrayList> data) {

        List<ArrayList> pmDataSet;
        pmDataSet = data;
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (pmDataSet != null) {
            mAdapter.setPMData(pmDataSet);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<ArrayList>> loader) {

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
            Bundle bundle = new Bundle();
            bundle.putString(SEARCH_TYPE,POPULAR_MOVIE );
            LoaderManager lm = getSupportLoaderManager();
            Loader<List<ArrayList>> loader= lm.getLoader(MOVIE_LOADER);
            if(loader==null){
                lm.initLoader(MOVIE_LOADER,bundle,this);
            }else{
                lm.restartLoader(MOVIE_LOADER,bundle,this);
            }            return true;
        }

       else if(id == R.id.most_popular){
            setDataForMenu();
            Bundle bundle = new Bundle();
            bundle.putString(SEARCH_TYPE,POPULAR_MOVIE );
            LoaderManager lm = getSupportLoaderManager();
            Loader<List<ArrayList>> loader= lm.getLoader(MOVIE_LOADER);
            if(loader==null){
                lm.initLoader(MOVIE_LOADER,bundle,this);
            }else{
                lm.restartLoader(MOVIE_LOADER,bundle,this);
            }
            sharedPreferences.edit().putInt("preferenceStatus", 1).apply();
            return true;

        }
           else if(id == R.id.top_rated){
                setDataForMenu();
                Bundle bundle = new Bundle();
                bundle.putString(SEARCH_TYPE,TOP_RATED_MOVIE );
                LoaderManager lm = getSupportLoaderManager();
                Loader<List<ArrayList>> loader= lm.getLoader(MOVIE_LOADER);
                if(loader==null){
                    lm.initLoader(MOVIE_LOADER,bundle,this);
                }else{
                    lm.restartLoader(MOVIE_LOADER,bundle,this);
                }
            sharedPreferences.edit().putInt("preferenceStatus", 2).apply();
            return true;
            }

           else if(id==R.id.favorite){
            setDataForMenu();
            Bundle bundle = new Bundle();
            bundle.putString(SEARCH_TYPE,FAVORITE_MOVIE);
            LoaderManager lm = getSupportLoaderManager();
            Loader<List<ArrayList>> loader= lm.getLoader(MOVIE_LOADER);
            if(loader==null){
                lm.initLoader(MOVIE_LOADER,bundle,this);
            }else{
                lm.restartLoader(MOVIE_LOADER,bundle,this);
            }
            sharedPreferences.edit().putInt("preferenceStatus", 3).apply();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




}
