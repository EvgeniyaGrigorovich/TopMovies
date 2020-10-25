package com.evgen.topmovies.database;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favourite_movies")
public class FavouriteMovie extends Movie {
    public FavouriteMovie(int uniqueId, int id, int voteCount, String title, String originalTitle,
                          String overview, String posterPath, String bigPosterPath,
                          String backDropPass, double voteAverage, String releaseDate) {
        super(uniqueId, id, voteCount, title, originalTitle, overview, posterPath, bigPosterPath,
                backDropPass, voteAverage, releaseDate);
    }

    @Ignore
    public FavouriteMovie(Movie movie){
        super(movie.getUniqueId(), movie.getId(), movie.getVoteCount(), movie.getTitle(), movie.getOriginalTitle(), movie.getOverview(), movie.getPosterPath(), movie.getBackDropPass(), movie.getBackDropPass(), movie.getVoteAverage(), movie.getReleaseDate());
    }
}
