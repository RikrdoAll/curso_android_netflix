package br.com.naotemigual.netflixremake.model;

import java.util.ArrayList;
import java.util.List;

/***
 * 20/06/2021 
 *
 * @author Ricardo Pires
 ***/
public class MovieDetail {

    private final Movie movie;
    private final List<Movie> moviesSimilar;

    public MovieDetail() {
        movie = new Movie();
        moviesSimilar = new ArrayList<>();
    }

    public Movie getMovie() {
        return movie;
    }

    public List<Movie> getMoviesSimilar() {
        return moviesSimilar;
    }
}
