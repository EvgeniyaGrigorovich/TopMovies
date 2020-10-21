package com.example.topmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topmovies.database.MainViewModel;
import com.example.topmovies.database.Movie;
import com.example.topmovies.utils.JSONUtils;
import com.example.topmovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MyLogs: ";

    private RecyclerView recyclerViewPoster;
    private MovieAdapter movieAdapter;
    private Switch switchSort;
    private TextView textViewTopRated;
    private TextView textViewPopularity;

    private MainViewModel mainViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPopularity = findViewById(R.id.textViewPopularity);
        textViewTopRated = findViewById(R.id.textViewTopRated);



        initPosters();
        updateMovies();
        initSwitch();

    }

    public void updateMovies(){
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        LiveData<List<Movie>> moviesFromLiveData = mainViewModel.getMovies();
        moviesFromLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                movieAdapter.setMovies(movies);
            }
        });

    }

    public void initPosters(){
        recyclerViewPoster = findViewById(R.id.recyclerViewPosters);
        recyclerViewPoster.setLayoutManager(new GridLayoutManager(this, 2));
        movieAdapter = new MovieAdapter();
        recyclerViewPoster.setAdapter(movieAdapter);
        movieAdapter.setOnPosterClickListener(position -> {
          Movie movie = movieAdapter.getMovies().get(position);
          Intent intent = new Intent(MainActivity.this, DetailActivity.class);
          intent.putExtra("id", movie.getId());
          startActivity(intent);

                });

        movieAdapter.setOnReachEndListener(new MovieAdapter.OnReachEndListener() {
            @Override
            public void onReachEnd() {
                Toast.makeText(MainActivity.this, "Конец списка", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initSwitch(){
        switchSort = findViewById(R.id.switchSort);
        switchSort.setChecked(true);
        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setMethodOfSort(isChecked);
            }
        });
        switchSort.setChecked(false);
    }

    public void onClickSetPopularity(View view) {
        setMethodOfSort(false);
        switchSort.setChecked(false);
    }

    public void omClickSetTopRated(View view) {
        setMethodOfSort(true);
        switchSort.setChecked(true);
    }

    private void setMethodOfSort(boolean isTopRated){
        int methodOfSort;
        if (isTopRated){
            textViewTopRated.setTextColor(getResources().getColor(R.color.colorAccent));
            textViewPopularity.setTextColor(getResources().getColor(R.color.white_color));
            methodOfSort = NetworkUtils.TOP_RATED;

        } else {
            textViewTopRated.setTextColor(getResources().getColor(R.color.white_color));
            textViewPopularity.setTextColor(getResources().getColor(R.color.colorAccent));
            methodOfSort = NetworkUtils.POPULARITY;
        }
      downloadData(methodOfSort, 1);
    }

    private void downloadData(int methodOfSort, int page){
        JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(methodOfSort, 1);
        List<Movie> movies = JSONUtils.getMoviesFromJSON(jsonObject);
        if (movies != null && !movies.isEmpty()){
            mainViewModel.deleteAllMovies();
            for (Movie movie:movies){
                mainViewModel.insertMovie(movie);
            }
        }
    }
}