package com.example.android.popularMovies.Utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class NetworkUtils {
    private final static String theMovieDBBaseURL = "http://image.tmdb.org/t/p/";
    //private final static String size = "w780";
    private final static String size = "w342";
   // private final static String size = "w500";

    private final static String baseMovieUrl = "http://api.themoviedb.org/3";
    private final static String askKey = "api_key";
    private static Uri builtUri = null;
    private final static String uTube = "https://www.youtube.com";
    private final static String watch = "watch";
    private final static String v = "v";


    //Insert your API Key in variable 'apiKey'
    private final static String apiKey = "";



    public static Uri uTubeUrl(String videoKey) {
        builtUri = Uri.parse(uTube).buildUpon()
                .appendEncodedPath(watch)
                .appendQueryParameter(v,videoKey)
                .build();


        return builtUri;
    }


    public static Uri buildUri(String movieDBSearchQuery) {
         builtUri = Uri.parse(theMovieDBBaseURL).buildUpon()
                .appendPath(size)
                .appendEncodedPath(movieDBSearchQuery)
                .build();
        return builtUri;
    }

    public static URL popularMovieBuildUrl(String sortParam) {
         builtUri = Uri.parse(baseMovieUrl).buildUpon()
                .appendEncodedPath(sortParam)
                .appendQueryParameter(askKey,apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
System.out.println("url is "+url);
        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
