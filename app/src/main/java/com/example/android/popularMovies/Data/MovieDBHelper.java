package com.example.android.popularMovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MovieDBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 2;

    public MovieDBHelper(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE "+ MovieContract.MovieEntries.TABLE_NAME +
                "( "+ MovieContract.MovieEntries._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                MovieContract.MovieEntries.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.MovieEntries.COLUMN_MOVIE_TITLE+ " TEXT NOT NULL, "+

                MovieContract.MovieEntries.COLUMN_MOVIE_PLOT + " TEXT, "+
                        MovieContract.MovieEntries.COLUMN_USER_RATING + " INTEGER, "+
                        MovieContract.MovieEntries.COLUMN_RELEASE_DATE + " TEXT, " +
                        MovieContract.MovieEntries.COLUMN_POSTER_PATH + " TEXT "+
                 ");";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntries.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
