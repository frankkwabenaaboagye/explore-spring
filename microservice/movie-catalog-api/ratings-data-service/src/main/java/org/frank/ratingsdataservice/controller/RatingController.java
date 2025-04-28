package org.frank.ratingsdataservice.controller;

import org.frank.ratingsdataservice.model.Rating;
import org.frank.ratingsdataservice.model.UserRating;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ratingsdata")
public class RatingController {

    /*
        ⭐⭐⭐⭐⭐ → Masterpiece
        ⭐⭐⭐⭐ → Great but not perfect
        ⭐⭐⭐ → Decent or divisive
        ⭐⭐ → Flawed
        ⭐ → Avoid
     */

    Map<String, List<Rating>> userData = Map.of(
            "frank", List.of(
                    Rating.builder().movieId("tt0111161").rating(5).build(),
                    Rating.builder().movieId("tt0068646").rating(5).build(),
                    Rating.builder().movieId("tt0468569").rating(5).build(),
                    Rating.builder().movieId("tt1375666").rating(4).build()
            ),
            "ben", List.of(
                    Rating.builder().movieId("tt0111161").rating(5).build(),
                    Rating.builder().movieId("tt0068646").rating(5).build(),
                    Rating.builder().movieId("tt0468569").rating(5).build(),
                    Rating.builder().movieId("tt1375666").rating(4).build()
            )
    );

    @GetMapping("/{movieId}")
    public Rating getRating(@PathVariable String movieId) {
        return Rating.builder()
                .movieId(movieId)
                .rating(4)
                .build();
    }

    @GetMapping("users/{userId}")
    public UserRating getUserRating(@PathVariable(required = false) String userId) {
        return userData.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(userId))
                .map(Map.Entry::getValue)
                .findFirst()
                .map(UserRating::new)
                .orElse(UserRating.NULL);

    }
}
