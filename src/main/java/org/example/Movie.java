package org.example;

import java.util.Calendar;

public class Movie {
    private String title;
    private String genre;
    private Calendar releaseDate;
    private int likes;
    private String description;
    private String director;
    private String movieStudio;

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public Calendar getReleaseDate() {
        return releaseDate;
    }

    public int getLikes() {
        return likes;
    }
    public void setLikes(int likes) {
        this.likes = likes;
    }
    public Movie() {

    }

    public Movie(String title, String genre, Calendar releaseDate, String description, String director, String movieStudio,int likes) {
        this.title = title;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.likes = likes;
        this.description = description;
        this.director = director;
        this.movieStudio = movieStudio;
    }

    public void printMovieInfo() {
        System.out.println("Title: " + title);
        System.out.println("Genre: " + genre);
        System.out.println("Release date: " + (releaseDate.get(Calendar.MONTH)+1) + " " + releaseDate.get(Calendar.DAY_OF_MONTH) + " " + releaseDate.get(Calendar.YEAR));
        System.out.println("Likes: " + likes);

        System.out.println("Description: " + description);

        System.out.println("Director: " + director);
        System.out.println("Movie Studio: " + movieStudio);
    }
}