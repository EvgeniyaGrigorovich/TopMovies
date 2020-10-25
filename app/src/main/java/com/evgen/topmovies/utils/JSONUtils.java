package com.evgen.topmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.evgen.topmovies.database.Movie;
import com.evgen.topmovies.database.Review;
import com.evgen.topmovies.database.Trailer;

public class JSONUtils {
    public static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    public static final String SMALL_POSTER_SIZE = "w185";
    public static final String BIG_POSTER_SIZE = "w780";

    private static final String KEY_RESULTS = "results";

    //ключи для трейлеров
    private static final String KEY_KEY_OF_VIDEO = "key";
    private static final String KEY_VIDEO_NAME = "name";
    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    //ключи для отзывов
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CONTENT = "content";

    //ключи для фильмов
    private static final String KEY_VOTE = "vote_count";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";

    public static List<Trailer> getTrailerFromJSON(JSONObject jsonObject) {
        List<Trailer> result = new ArrayList<>();
        if (jsonObject == null) {
            return result;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objectTrailer = jsonArray.getJSONObject(i);
                String key = BASE_YOUTUBE_URL + objectTrailer.getString(KEY_KEY_OF_VIDEO);
                String name = objectTrailer.getString(KEY_VIDEO_NAME);
                Trailer trailer = new Trailer(key, name);
                result.add(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Review> getReviewsFromJSON(JSONObject jsonObject) {
        List<Review> result = new ArrayList<>();
        if (jsonObject == null) {
            return result;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objectReview = jsonArray.getJSONObject(i);
                String author = objectReview.getString(KEY_AUTHOR);
                String content = objectReview.getString(KEY_CONTENT);
                Review review = new Review(author, content);
                result.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Movie> getMoviesFromJSON(JSONObject jsonObject) {
        List<Movie> result = new ArrayList<>();
        if (jsonObject == null) {
            return result;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objectMovie = jsonArray.getJSONObject(i);
                int id = objectMovie.getInt(KEY_ID);
                int vote_count = objectMovie.getInt(KEY_VOTE);
                String title = objectMovie.getString(KEY_TITLE);
                String original_title = objectMovie.getString(KEY_ORIGINAL_TITLE);
                String overview = objectMovie.getString(KEY_OVERVIEW);
                String poster_path = BASE_POSTER_URL + SMALL_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH);
                String big_poster_path = BASE_POSTER_URL + BIG_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH);
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
