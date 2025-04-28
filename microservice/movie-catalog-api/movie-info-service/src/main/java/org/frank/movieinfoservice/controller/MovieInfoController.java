package org.frank.movieinfoservice.controller;

import org.frank.movieinfoservice.model.Movie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/movies")
public class MovieInfoController {

    // movie id and the info
    Map<String, String> movieMap = Map.of(
            "tt0111161", "The Shaw shank Redemption",
            "tt0068646", "The Godfather",
            "tt0468569", "The Dark Knight",
            "tt1375666", "Inception",
            "tt0133093", "The Matrix",
            "tt0109830", "Forrest Gump",
            "tt0120737", "The Lord of the Rings: The Fellowship of the Ring",
            "tt0167260", "The Lord of the Rings: The Return of the King",
            "tt0080684", "Star Wars: Episode V - The Empire Strikes Back",
            "tt4154796", "Avengers: Endgame"
    );


    @GetMapping("/{movieId}")
    public Movie getMovieInfo(@PathVariable String movieId) {

        return movieMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(movieId))
                .findFirst()
                .map(entry -> Movie.builder()
                        .movieId(entry.getKey())
                        .name(entry.getValue())
                        .build()
                )
                .orElse(Movie.NULL);


    }
}
