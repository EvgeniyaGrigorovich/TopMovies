package com.example.topmovies.utils;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {

    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";

    private static final String PARAMS_API_KEY = "api_key";
    private static final String API_KEY = "d1ec372463ecca247b447ee62705382a";

    private static final String PARAMS_LANGUAGE = "language";
    private static final String LANGUAGE_VALUE = "ru-RU";

    private static final String PARAMS_SORT_BY = "sort_by";
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_TO_RATED = "vote_average.desc";

    private static final String PARAMS_PAGE = "page";

    public static final int POPULARITY = 0;
    public static final int TOP_RATED = 1;


    //метод формирует запрос
    private static URL buildUR(int sortBy, int page) {
        URL result = null;
        String methodOfSort;
        if (sortBy == POPULARITY) {
            methodOfSort = SORT_BY_POPULARITY;
        } else {
            methodOfSort = SORT_BY_TO_RATED;
        }
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .appendQueryParameter(PARAMS_SORT_BY, methodOfSort)
                .appendQueryParameter(PARAMS_PAGE, Integer.toString(page))
                .build();
        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class JSONLoadTask extends AsyncTask<URL, Void, JSONObject> {
        StringBuilder stringBuilder = new StringBuilder();

        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject result = null;
            if (urls == null || urls.length == 0) {
                return null;
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) urls[0].openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line);
                    line = reader.readLine();
                }
                result = new JSONObject(stringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }
    }

    public static JSONObject getJSONFromNetwork(int sortBy, int page) {
        JSONObject result = null;
        URL uRl = buildUR(sortBy, page);
        try {
            result = new JSONLoadTask().execute(uRl).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
