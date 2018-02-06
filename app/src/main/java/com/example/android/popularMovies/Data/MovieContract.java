package com.example.android.popularMovies.Data;

import android.net.Uri;
import android.provider.BaseColumns;


public class MovieContract {

    public static final String AUTHORITY = "com.example.android.popularMovies";
    public static final String PATH_FAVORITE_MOVIE = "favoritemovie";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    public static final class MovieEntries implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIE).build();
        public static final String TABLE_NAME = "favoritemovie";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_PLOT = "plot";
        public static final String COLUMN_USER_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "releasedate";
        public static final String COLUMN_POSTER_PATH = "path";
    }
}
