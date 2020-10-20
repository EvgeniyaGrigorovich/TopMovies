package com.example.topmovies.database;

public class Movie {
    private int id ;
    //количество голосов
    private int voteCount;
    //название
    private String title;
    //оригинальное название
    private String originalTitle;
    //описание
    private String overview;
    //путь к постеру
    private String posterPath;

    private String bigPosterPath;

    //фоновое изображение
    private String backDropPass;
    //средний рейтинг
    private double voteAverage;
    //дата релиза
    private String releaseDate;

    public Movie(int id, int voteCount, String title, String originalTitle, String overview, String posterPath, String bigPosterPath, String backDropPass, double voteAverage, String releaseDate) {
        this.id = id;
        this.voteCount = voteCount;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.posterPath = posterPath;
        this.bigPosterPath = bigPosterPath;
        this.backDropPass = backDropPass;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }



    public void setBigPosterPath(String bigPosterPath) {
        this.bigPosterPath = bigPosterPath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setBackDropPass(String backDropPass) {
        this.backDropPass = backDropPass;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackDropPass() {
        return backDropPass;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
    public String getBigPosterPath() {
        return bigPosterPath;
    }
}
