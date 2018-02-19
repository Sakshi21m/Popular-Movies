package com.example.android.popularMovies.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static android.R.attr.id;


public class MovieProvider extends ContentProvider {


    private static final int ALL_FAVORITE_MOVIES = 100;
    private static final int FAVORITE_MOVIE_WITH_ID = 101;
    private static final UriMatcher staticUriMatcher = buildUriMatcher();
    private MovieDBHelper dbHelper;


    private static UriMatcher buildUriMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_FAVORITE_MOVIE,ALL_FAVORITE_MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_FAVORITE_MOVIE + "/#",FAVORITE_MOVIE_WITH_ID);
        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        Context context=getContext();
        dbHelper = new MovieDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] columnNames, @Nullable String whereClause , @Nullable String[] strings1, @Nullable String s1) {

       final SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        int match = staticUriMatcher.match(uri);

        Cursor cursor;

        switch (match){

            case ALL_FAVORITE_MOVIES:
                cursor = sqLiteDatabase.query( MovieContract.MovieEntries.TABLE_NAME,
                        columnNames,
                        whereClause,
                        null,
                        null,
                        null,
                        null);
                break;

            default: throw new UnsupportedOperationException("throws error"+uri);
        }
        try {
            if (cursor != null)
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }catch (Exception e){
        System.out.println("error is "+e.toString());
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        int match = staticUriMatcher.match(uri);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        Uri returnedUri;

        switch(match){
            case ALL_FAVORITE_MOVIES:
                long id = sqLiteDatabase.insert(MovieContract.MovieEntries.TABLE_NAME, null, contentValues);

                if (id>0)

                    returnedUri = ContentUris.withAppendedId(MovieContract.MovieEntries.CONTENT_URI,id);

                else
                    throw new android.database.SQLException("Failed to insert row into "+uri);
                break;

            default:
                throw new UnsupportedOperationException("error "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnedUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String whereClause, @Nullable String[] strings) {

        int match = staticUriMatcher.match(uri);
        int tasksChangedCount;
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
          switch (match){
              case FAVORITE_MOVIE_WITH_ID :
                  //to get the id of the movie which is supposed to be deleted
                  tasksChangedCount = sqLiteDatabase.delete(MovieContract.MovieEntries.TABLE_NAME, whereClause ,null);
                  break;
              case ALL_FAVORITE_MOVIES:
                  tasksChangedCount = sqLiteDatabase.delete(MovieContract.MovieEntries.TABLE_NAME,null ,null);
                  break;

          default:

              throw new UnsupportedOperationException("error "+uri);
          }
          if (tasksChangedCount!=0)
          getContext().getContentResolver().notifyChange(uri,null);
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
