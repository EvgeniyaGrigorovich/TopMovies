package com.evgen.topmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT* FROM movies")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM movies WHERE id ==:movieId")
    Movie getMovieById(int movieId);

    @Query("DELETE FROM movies")
    void deleteAllMovies();

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);



    @Query("SELECT * FROM favourite_movies WHERE id ==:movieId")
    FavouriteMovie getFavouriteMovieById(int movieId);

    @Query("SELECT* FROM favourite_movies")
    LiveData<List<FavouriteMovie>> getAllFavouriteMovies();

    @Insert
    void insertFavouriteMovie(FavouriteMovie favouriteMovie);

    @Delete
    void deleteFavouriteMovie(FavouriteMovie favouriteMovie);
}
