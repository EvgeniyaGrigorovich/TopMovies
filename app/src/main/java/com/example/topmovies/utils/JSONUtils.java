package com.example.topmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.topmovies.database.Movie;

public class JSONUtils {
    public static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    public static final String SMALL_POSTER_SIZE = "w185";
    public static final String BIG_POSTER_SIZE = "w780";

    private static final String KEY_RESULTS = "results";
    private static final String KEY_VOTE = "vote_count";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";

    public static List<Movie> getMoviesFromJSON(JSONObject jsonObject){
        List<Movie> result = new ArrayList<>();
        if (jsonObject == null){
            return result;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i <jsonArray.length(); i++){
                JSONObject objectMovie = jsonArray.getJSONObject(i);
                int id = objectMovie.getInt(KEY_ID);
                int vote_count = objectMovie.getInt(KEY_VOTE);
                String title = objectMovie.getString(KEY_TITLE);
                String original_title = objectMovie.getString(KEY_ORIGINAL_TITLE);
                String overview = objectMovie.getString(KEY_OVERVIEW);
                String poster_path = BASE_POSTER_URL+SMALL_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH);
                String big_poster_path = BASE_POSTER_URL+BIG_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH);
                String backdrop_path = objectMovie.getString(KEY_BACKDROP_PATH);
                double vote_average = objectMovie.getDouble(KEY_VOTE_AVERAGE);
                String realise_date = objectMovie.getString(KEY_RELEASE_DATE);
                Movie movie = new Movie(id, vote_count, title, original_title, overview, poster_path, big_poster_path, backdrop_path, vote_average, realise_date);
                result.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
