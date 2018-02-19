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

                Long movieId;
                String voteAverage;
                String originalTitle;
                String posterPath;
                String overview;
                String releaseDate;

                ArrayList<String> innerArray = new ArrayList<>();

                movieId = results.getJSONObject(i).getLong("id");
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
                    innerArray.add(1, voteAverage+"/10");
                    innerArray.add(2, originalTitle);
                    innerArray.add(3, posterPath);
                    innerArray.add(4, overview);
                    innerArray.add(5, releaseDate);
                pmData.add(innerArray);
            }

        }
        return pmData;
    }

    public static List<List<ArrayList>> getReviewVideoList(String jsonVideos,String jsonReview) throws JSONException{

        List<List<ArrayList>> ls = new ArrayList<>();
        List<ArrayList> listVideo= new ArrayList<>();
        List<ArrayList> listReview= new ArrayList<>();


        JSONObject jsonObject = new JSONObject(jsonVideos);

        JSONArray videoList = jsonObject.getJSONArray("results");
        for (int i =0; i<videoList.length();i++){

            if(!videoList.isNull(i)){

                ArrayList innerArray = new ArrayList();

                String id;
                String iso_639_1;
                String iso_3166_1;
                String key;
                String name;
                String site;
                int size;
                String type;

                id = videoList.getJSONObject(i).getString("id");
                iso_639_1 = videoList.getJSONObject(i).getString("iso_639_1");
                iso_3166_1 = videoList.getJSONObject(i).getString("iso_3166_1");
                key = videoList.getJSONObject(i).getString("key");
                name = videoList.getJSONObject(i).getString("name");
                site = videoList.getJSONObject(i).getString("site");
                size = videoList.getJSONObject(i).getInt("size");
                type = videoList.getJSONObject(i).getString("type");

                innerArray.add(0,id);
                innerArray.add(1,iso_639_1);
                innerArray.add(2,iso_3166_1);
                innerArray.add(3,key);
                innerArray.add(4,name);
                innerArray.add(5,site);
                innerArray.add(6,size);
                innerArray.add(7,type);

                listVideo.add(innerArray);

            }
        }

        JSONObject jsonObjectReview  = new JSONObject(jsonReview);

        JSONArray resultsReview = jsonObjectReview.getJSONArray("results");
        for(int i=0;i<resultsReview.length();i++){

            if(!resultsReview.isNull(i)){
                String id;
                String author;
                String content;
                ArrayList innerArray = new ArrayList();


                id = resultsReview.getJSONObject(i).getString("id");
                author = resultsReview.getJSONObject(i).getString("author");
                content = resultsReview.getJSONObject(i).getString("content");

                innerArray.add(0,id);
                innerArray.add(1,author);
                innerArray.add(2,content);

                listReview.add(innerArray);
            }
        }
        ls.add(0,listVideo);
        ls.add(1,listReview);
        return ls;

    }

}
