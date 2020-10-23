package com.example.topmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topmovies.adapters.ReviewAdapter;
import com.example.topmovies.adapters.TrailerAdapter;
import com.example.topmovies.database.FavouriteMovie;
import com.example.topmovies.database.MainViewModel;
import com.example.topmovies.database.Movie;
import com.example.topmovies.database.Review;
import com.example.topmovies.database.Trailer;
import com.example.topmovies.utils.JSONUtils;
import com.example.topmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private ImageView imageViewBigPoster;
    private TextView textViewTitle;
    private TextView textViewOriginalTitle;
    private TextView textViewRating;
    private TextView textViewReleaseDate;
    private TextView textViewOverview;
    private int id;
    private Movie movie;
    private ImageView imageViewStar;
    private FavouriteMovie favouriteMovie;
    private MainViewModel mainViewModel;

    private RecyclerView recyclerViewTrailers;
    private RecyclerView recyclerViewReviews;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    List<Trailer> trailers;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();
        getMovieId();
        setViewValue();
        setFavourite();
        setReviews();
        setTrailers();
    }

    private void setReviews(){
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter();
        recyclerViewReviews.setAdapter(reviewAdapter);
        JSONObject jsonObject = NetworkUtils.getJSONForReviews(movie.getId());
        List<Review> reviews = JSONUtils.getReviewsFromJSON(jsonObject);
        reviewAdapter.setReviews(reviews);
    }

    private void setTrailers() {
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
        trailerAdapter = new TrailerAdapter();
        recyclerViewTrailers.setAdapter(trailerAdapter);
        JSONObject jsonObject = NetworkUtils.getJSONForVideos(movie.getId());
        trailers = JSONUtils.getTrailerFromJSON(jsonObject);
        trailerAdapter.setTrailers(trailers);
        trailerAdapter.setOnTrailerClickListener(url -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
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
        switch (id){
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

    private void initView() {
        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        textViewRating = findViewById(R.id.textViewRating);
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
        textViewOverview = findViewById(R.id.textViewOverView);
        imageViewStar = findViewById(R.id.imageViewAddToFavo);
        recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
    }

    public void getMovieId() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            id = intent.getIntExtra("id", 1);
        } else {
            finish();
        }
    }

    private void setViewValue() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        movie = mainViewModel.getMovieById(id);
        Picasso.get().load(movie.getBigPosterPath()).into(imageViewBigPoster);
        textViewTitle.setText(movie.getTitle());
        textViewOriginalTitle.setText(movie.getOriginalTitle());
        textViewRating.setText(Double.toString(movie.getVoteAverage()));
        textViewReleaseDate.setText(movie.getReleaseDate());
        textViewOverview.setText(movie.getOverview());
    }

    public void onClickChangeFavourite(View view) {
        if (favouriteMovie == null) {
            mainViewModel.insertFavouriteMovie(new FavouriteMovie(movie));
            Toast.makeText(this, R.string.add_to_favourite, Toast.LENGTH_SHORT).show();
        } else {
            mainViewModel.deleteFavouriteMovie(favouriteMovie);
            Toast.makeText(this, R.string.delete_from_favourite, Toast.LENGTH_SHORT).show();
        }
        setFavourite();
    }

    private void setFavourite(){
        favouriteMovie = mainViewModel.getFavouriteMovieById(id);
        if (favouriteMovie == null){
            imageViewStar.setImageResource(R.drawable.stargrey);
        } else {
            imageViewStar.setImageResource(R.drawable.star);
        }
    }
}