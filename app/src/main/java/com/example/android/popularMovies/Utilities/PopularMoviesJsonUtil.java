package com.example.android.popularMovies.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PopularMoviesJsonUtil {

    public  static List<ArrayList> getSimplePMStringsFromJson(String pmJsonStr)
            throws JSONException {


        List<ArrayList> pmData = new ArrayList<>();

        JSONObject pms = new JSONObject(pmJsonStr);

        JSONArray results = pms.getJSONArray("results");

        int pmCount = results.length();

        for (int i = 0; i <pmCount; i++){

            if(!results.isNull(i)) {

                int movieId;
                String voteAverage;
                String originalTitle;
                String posterPath;
                String overview;
                String releaseDate;

                ArrayList<String> innerArray = new ArrayList<>();

                movieId = results.getJSONObject(i).getInt("id");
                voteAverage = results.getJSONObject(i).getString("vote_average");
                originalTitle = results.getJSONObject(i).getString("original_title");
                posterPath = results.getJSONObject(i).getString("poster_path");
                overview = results.getJSONObject(i).getString("overview");
                releaseDate = results.getJSONObject(i).getString("release_date");

                /*Adding the data from JSON into a ArrayList with key value pair
                * key   value
                * 0     movie id
                * 1     vote average
                * 2     title
                * 3     poster path
                * 4     plot synopsis
                * 5     release date
                * */
                    innerArray.add(0, String.valueOf(movieId));
                    innerArray.add(1, voteAverage);
                    innerArray.add(2, originalTitle);
                    innerArray.add(3, posterPath);
                    innerArray.add(4, overview);
                    innerArray.add(5, releaseDate);
                pmData.add(innerArray);
            }

        }
        return pmData;
    }
}
