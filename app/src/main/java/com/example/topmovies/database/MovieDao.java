package com.example.topmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT* FROM MOVIES")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM MOVIES WHERE id ==:movieId")
    Movie getMovieById(int movieId);

    @Query("DELETE FROM MOVIES")
    void deleteAllMovies();

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);
}
