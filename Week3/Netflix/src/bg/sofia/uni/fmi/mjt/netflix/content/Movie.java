package bg.sofia.uni.fmi.mjt.netflix.content;

import bg.sofia.uni.fmi.mjt.netflix.content.enums.PgRating;
import bg.sofia.uni.fmi.mjt.netflix.content.enums.Genre;

public class Movie implements Streamable {
    private Genre genre;
    private PgRating pgrating;
    private String name;
    private int duration;
    public Movie(String name, Genre genre, PgRating rating, int duration){
        this.name = name;
        this.genre = genre;
        this.pgrating = rating;
        this.duration = duration;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public PgRating getRating() {
        return pgrating;
    }

    @Override
    public int getDuration() {
        return duration;
    }

}
