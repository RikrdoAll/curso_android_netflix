package br.com.naotemigual.netflixremake.model;

import java.util.List;

public class Category {

    private String name;
    private List<Movie> movies;

    public Category(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}

