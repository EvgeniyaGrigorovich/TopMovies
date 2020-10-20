package com.example.topmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.topmovies.database.Movie;
import com.example.topmovies.utils.JSONUtils;
import com.example.topmovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MyLogs: ";

    private RecyclerView recyclerViewPoster;
    private MovieAdapter movieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewPoster = findViewById(R.id.recyclerViewPosters);
        recyclerViewPoster.setLayoutManager(new GridLayoutManager(this, 2));
        movieAdapter = new MovieAdapter();
        JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(NetworkUtils.POPULARITY, 1);
        List<Movie> movies = JSONUtils.getMoviesFromJSON(jsonObject);
        movieAdapter.setMovies(movies);
        recyclerViewPoster.setAdapter(movieAdapter);

    }
}