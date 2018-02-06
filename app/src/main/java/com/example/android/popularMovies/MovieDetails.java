package com.example.android.popularMovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularMovies.Data.MovieContract;
import com.example.android.popularMovies.Utilities.NetworkUtils;
import com.example.android.popularMovies.Utilities.PopularMoviesJsonUtil;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MovieDetails extends AppCompatActivity implements  AdapterVideo.MovieAdapterVideoOnClickHandler,LoaderManager.LoaderCallbacks<List<List<ArrayList>>> {

    private Long movieId = null;
    private String mTitle = null;
    private String mRating = null;
    private String mPosterPath = null;
    private String mOverview = null;
    private String mReleaseDate = null;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private final static int REVIEW_LOADER = 30;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewVideo;

    private AdapterReview mAdapter;
    private AdapterVideo mVideoAdapter;


    private static final String VIDEO = "video";
    private static final String REVIEWS = "review";
    //public static final String SEARCH_TYPE = "searchType";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

         TextView title;
         TextView plot;
         TextView mVote;
         ImageView poster;
         TextView releaseDate;


        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (!bundle.isEmpty()) {

                movieId = Long.parseLong(bundle.getString("movieId",null));
                mTitle = bundle.getString("title", null);
                mRating = bundle.getString("voteAverage", null)+"/10";
                mPosterPath = bundle.getString("posterPath", null);
                mOverview = bundle.getString("overview", null);
                mReleaseDate = bundle.getString("releaseDate", null);

                mVote = (TextView) findViewById(R.id.vote_data);
                title = (TextView) findViewById(R.id.movie_title);
                plot = (TextView) findViewById(R.id.plot_data);
                releaseDate = (TextView) findViewById(R.id.release_date_data);
                poster = (ImageView) findViewById(R.id.poster);

                mVote.setText(mRating);
                title.setText(mTitle);
                plot.setText(mOverview);
                releaseDate.setText(mReleaseDate);

                Uri link = NetworkUtils.buildUri(mPosterPath);
                Picasso.with(this).load(link).into(poster);

                favoriteMovie();


                mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display2);
                mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator2);


                recyclerView = (RecyclerView) findViewById(R.id.rv_review);
                LinearLayoutManager layoutManager;
                layoutManager = new LinearLayoutManager(this);
                layoutManager.setOrientation(OrientationHelper.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);


                recyclerViewVideo = (RecyclerView) findViewById(R.id.rv_video_trailers);
                LinearLayoutManager layoutManager2;
                layoutManager2 = new LinearLayoutManager(this);
                layoutManager2.setOrientation(OrientationHelper.VERTICAL);
                recyclerViewVideo.setLayoutManager(layoutManager2);
                recyclerViewVideo.setHasFixedSize(true);

                mAdapter = new AdapterReview(this);
                recyclerView.setAdapter(mAdapter);

                mVideoAdapter = new AdapterVideo(this);
                recyclerViewVideo.setAdapter(mVideoAdapter);


                Bundle bundle2 = new Bundle();
                bundle2.putString(REVIEWS,REVIEWS);
                bundle2.putString(VIDEO,VIDEO);

                LoaderManager lm = getSupportLoaderManager();
                lm.initLoader(REVIEW_LOADER,bundle2,this);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerViewVideo.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void favoriteMovie() {
        ImageButton favStar = (ImageButton) findViewById(R.id.favoriteStar);
        TextView favText = (TextView) findViewById(R.id.favText);
        //MovieDBHelper mdbHelper = new MovieDBHelper(this);
        //sqlDb = mdbHelper.getReadableDatabase();
        String whereClause = MovieContract.MovieEntries.COLUMN_MOVIE_ID + " = " + movieId;
        //Cursor cursor =    sqlDb.query(MovieContract.MovieEntries.TABLE_NAME, null, whereClause, null,null,null,null);
        Cursor cursor = getContentResolver().query(MovieContract.MovieEntries.CONTENT_URI, null, whereClause, null, null);
if(cursor!=null){
        if (!cursor.moveToPosition(0)) {
            favStar.setBackgroundResource(R.drawable.mark_as_favorite);
            favText.setText(R.string.mark_fav);
            return;
        } else {
            favStar.setBackgroundResource(R.drawable.marked_favorite);
            favText.setText(R.string.marked_fav);
        }
    cursor.close();

}

}
public void favoriteMovieSelected(View view){

   // ImageButton favStar = (ImageButton) findViewById(R.id.favoriteStar);
    ImageButton favStar = (ImageButton)view;
    TextView favText = (TextView)findViewById(R.id.favText);

    String[] columnNames = {MovieContract.MovieEntries.COLUMN_MOVIE_ID};

    String whereClause = MovieContract.MovieEntries.COLUMN_MOVIE_ID+" = "+movieId.toString();

    try {
        Cursor cursor = getContentResolver().query(MovieContract.MovieEntries.CONTENT_URI, columnNames, whereClause, null, null);
        if(cursor!=null){
        if (!cursor.moveToPosition(0)) {

            ContentValues cv = new ContentValues();
            cv.put(MovieContract.MovieEntries.COLUMN_MOVIE_ID, movieId);
            cv.put(MovieContract.MovieEntries.COLUMN_MOVIE_TITLE, mTitle);
            cv.put(MovieContract.MovieEntries.COLUMN_MOVIE_PLOT, mOverview);
            cv.put(MovieContract.MovieEntries.COLUMN_POSTER_PATH, mPosterPath);
            cv.put(MovieContract.MovieEntries.COLUMN_RELEASE_DATE, mReleaseDate);
            cv.put(MovieContract.MovieEntries.COLUMN_USER_RATING, mRating);

            getContentResolver().insert(MovieContract.MovieEntries.CONTENT_URI, cv);
            favStar.setBackgroundResource(R.drawable.marked_favorite);
            favText.setText(R.string.marked_fav);
            return;
        } else {

            Uri deleteUri = MovieContract.MovieEntries.CONTENT_URI;
            deleteUri = deleteUri.buildUpon().appendPath(movieId.toString()).build();
            getContentResolver().delete(deleteUri, whereClause, null);
            favStar.setBackgroundResource(R.drawable.mark_as_favorite);
            favText.setText(R.string.mark_fav);
        }
        cursor.close();
    }
    }catch(Exception e){

        System.out.println("Error is " + e.toString());
    }

}


    @Override
    public Loader<List<List<ArrayList>>> onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<List<List<ArrayList>>>(this){
            private List<List<ArrayList>> jSonList;

            @Override
            public void onStartLoading(){
                super.onStartLoading();
                if (args==null)
                    return;

                mErrorMessageDisplay.setVisibility(View.INVISIBLE);
                mLoadingIndicator.setVisibility(View.VISIBLE);


                //caching
                if(jSonList!=null){
                    //on pressing back button loading indicator needs to be disabled since we already have a data set ready.
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                    deliverResult(jSonList);
                }else{
                    forceLoad();
                }
            }


            @Override
            public List<List<ArrayList>> loadInBackground() {

                String jSONReviewResponse;
                String jSONVideoResponse;
                String urlReviewString = "movie/"+movieId+"/reviews";
                String urlVideoString = "movie/"+movieId+"/videos";


                URL reviewUrl = NetworkUtils.popularMovieBuildUrl(urlReviewString);
                URL videoUrl = NetworkUtils.popularMovieBuildUrl(urlVideoString);

                try{
                    jSONReviewResponse = NetworkUtils.getResponseFromHttpUrl(reviewUrl);
                    jSONVideoResponse = NetworkUtils.getResponseFromHttpUrl(videoUrl);
                    return PopularMoviesJsonUtil.getReviewVideoList(jSONVideoResponse,jSONReviewResponse);

                }catch (Exception e){
                    return null;
                }
            }
            @Override
            public void deliverResult(List<List<ArrayList>> ls){
                jSonList = ls;
                super.deliverResult(ls);
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<List<List<ArrayList>>> loader, List<List<ArrayList>> data) {

            List<List<ArrayList>> pmDataSet;
            pmDataSet = data;
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (pmDataSet.get(1) != null){
                mAdapter.setPMData(pmDataSet.get(1));
            }
            if ( pmDataSet.get(0) != null) {
                mVideoAdapter.setPMData(pmDataSet.get(0));
            }
        if (pmDataSet.get(1) == null && pmDataSet.get(0) == null){

                showErrorMessage();
            }


    }

    @Override
    public void onLoaderReset(Loader<List<List<ArrayList>>> loader) {

    }



    private void openWebPage(Uri webPage) {


        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
}

    @Override
    public void onClick(List movieDetails) {

        String videoKey = movieDetails.get(3).toString();
        Uri videoLink  = NetworkUtils.uTubeUrl(videoKey);
        openWebPage(videoLink);
    }
}
