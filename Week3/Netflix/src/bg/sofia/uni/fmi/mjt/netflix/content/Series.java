package bg.sofia.uni.fmi.mjt.netflix.content;

import bg.sofia.uni.fmi.mjt.netflix.content.enums.*;

public class Series implements Streamable {
    private String name;
    private Genre genre;
    private PgRating pgrating;
    private Episode[] episodes;

    public Series(String name, Genre genre, PgRating rating, Episode[] episodes){
        this.name = name;
        this.genre = genre;
        this.pgrating = rating;
        this.episodes = episodes;
    }

    @Override
    public int getDuration() {
        int sum = 0;
        for (int i = 0; i < episodes.length; i++){
            sum += episodes[i].getDuration();
        }
        return sum;
    }

    @Override
    public PgRating getRating() {
        return pgrating;
    }

    @Override
    public String getTitle() {
        return name;
    }
}
