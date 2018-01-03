package com.example.android.popularMovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularMovies.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;


public class MovieDetails extends AppCompatActivity {




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
            SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.android.popularMovies", Context.MODE_PRIVATE);
            String mTitle = sharedPreferences.getString("title", "abc");
            String voteAverage = sharedPreferences.getString("voteAverage", null);
            String mPosterPath = sharedPreferences.getString("posterPath", null);
            String mOverview = sharedPreferences.getString("overview",null);
            String mReleaseDate = sharedPreferences.getString("releaseDate",null);
            System.out.println(mTitle + "   " + voteAverage);

            mVote = (TextView) findViewById(R.id.vote_data);
            title = (TextView) findViewById(R.id.movie_title);
            plot =(TextView)findViewById(R.id.plot_data);
            releaseDate = (TextView)findViewById(R.id.release_date_data);
            poster = (ImageView)findViewById(R.id.poster);

            mVote.setText(voteAverage);
            title.setText(mTitle);
            plot.setText(mOverview);
            releaseDate.setText(mReleaseDate);

            Uri link  = NetworkUtils.buildUri(mPosterPath);
            Picasso.with(this).load(link).into(poster);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
