package com.evgen.topmovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.evgen.topmovies.adapters.MovieAdapter;
import com.evgen.topmovies.database.MainViewModel;
import com.evgen.topmovies.database.Movie;
import com.evgen.topmovies.utils.JSONUtils;
import com.evgen.topmovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private static final String LOG_TAG = "MyLogs: ";

    private RecyclerView recyclerViewPoster;
    private MovieAdapter movieAdapter;
    private Switch switchSort;
    private TextView textViewTopRated;
    private TextView textViewPopularity;
    private MainViewModel mainViewModel;
    private ProgressBar progressBarLoading;

    private static final int LOADER_ID = 8817;
    private LoaderManager loaderManager;

    private static int page = 1;
    private static boolean isLoading = false;
    private int methodOfSort;
    private static String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBarLoading = findViewById(R.id.progressBarLoading);
        lang = Locale.getDefault().getLanguage();

        initPosters();
        openDescriptionOfMovie();
        updateMovies();
        initSwitch();
    }

    private int getColumnCount() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return width / 185 > 2 ? width / 185 : 2;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itemMain:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.itemFavourite:
                Intent intent1ToFavourite = new Intent(this, FavouriteActivity.class);
                startActivity(intent1ToFavourite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        NetworkUtils.JSONLoader jsonLoader = new NetworkUtils.JSONLoader(this, args);
        jsonLoader.setOnStartLoadingListener(new NetworkUtils.JSONLoader.OnStartLoadingListener() {
            @Override
            public void onStartLoading() {
                isLoading = true;
                progressBarLoading.setVisibility(View.VISIBLE);
            }
        });
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        List<Movie> movies = JSONUtils.getMoviesFromJSON(data);
        if (movies != null && !movies.isEmpty()) {
            if (page == 1) {
                mainViewModel.deleteAllMovies();
                movieAdapter.clear();
            }
            for (Movie movie : movies) {
                mainViewModel.insertMovie(movie);
            }
            movieAdapter.addMovies(movies);
        }
        progressBarLoading.setVisibility(View.INVISIBLE);
        loaderManager.destroyLoader(LOADER_ID);
        page++;
        isLoading = false;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }

    private void downloadData(int methodOfSort, int page) {
        loaderManager = LoaderManager.getInstance(this);
        URL url = NetworkUtils.buildUR(methodOfSort, page, lang);
        Bundle bundle = new Bundle();
        bundle.putString("url", url.toString());
        loaderManager.restartLoader(LOADER_ID, bundle, this);

    }

    public void updateMovies() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        LiveData<List<Movie>> moviesFromLiveData = mainViewModel.getMovies();
        moviesFromLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (page == 1) {
                    movieAdapter.setMovies(movies);
                }
            }
        });
    }

    public void initPosters() {
        recyclerViewPoster = findViewById(R.id.recyclerViewPosters);
        recyclerViewPoster.setLayoutManager(new GridLayoutManager(this, getColumnCount()));
        movieAdapter = new MovieAdapter();
        recyclerViewPoster.setAdapter(movieAdapter);
        movieAdapter.setOnReachEndListener(new MovieAdapter.OnReachEndListener() {
            @Override
            public void onReachEnd() {
                if (!isLoading) {
                    downloadData(methodOfSort, page);
                }
            }
        });
    }

    private void initSwitch() {
        textViewPopularity = findViewById(R.id.textViewPopularity);
        textViewTopRated = findViewById(R.id.textViewTopRated);

        switchSort = findViewById(R.id.switchSort);
        switchSort.setChecked(true);
        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                page = 1;
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

    private void setMethodOfSort(boolean isTopRated) {
        if (isTopRated) {
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

    public void openDescriptionOfMovie() {
        movieAdapter.setOnPosterClickListener(position -> {
            Movie movie = movieAdapter.getMovies().get(position);
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("id", movie.getId());
            startActivity(intent);
        });
    }
}