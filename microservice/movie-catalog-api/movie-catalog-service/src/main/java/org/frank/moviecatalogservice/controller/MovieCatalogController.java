package org.frank.moviecatalogservice.controller;

import com.netflix.discovery.DiscoveryClient;
import lombok.RequiredArgsConstructor;
import org.frank.moviecatalogservice.model.CatalogItem;
import org.frank.moviecatalogservice.model.Movie;
import org.frank.moviecatalogservice.model.Rating;
import org.frank.moviecatalogservice.model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class MovieCatalogController {

    private final RestTemplate restTemplate;
    // private final WebClient.Builder webClientBuilder;



    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable String userId) {

        // TODO: get all rated movie IDs: we hard code this for  now

        UserRating userRating = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId, UserRating.class);

        List<Rating> ratings = userRating.getUserRatings(); // will definitely get it

        System.out.println("moving ahead");
        // TODO: for each movie id call the movie info service and get details
            // we use rest template - (just trying it out)

        return ratings.stream().map(rating -> {
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
            return CatalogItem.builder()
                    .name(movie.getName())
                    .desc("a sample description")
                    .rating(rating.getRating())
                    .build();
        }).collect(Collectors.toList());

        // TODO: put them all together

    }
}



// using webclient
        /*
        return ratings.stream().map(rating -> {
            Movie movie = webClientBuilder.build().get()
                    .uri("http://localhost:8082/movies/" + rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();  // we have to do this cos we are definite here List<CatalogItem> :: return type
            return CatalogItem.builder()
                    .name(movie.getName())
                    .desc("a sample description")
                    .rating(rating.getRating())
                    .build();
        }).collect(Collectors.toList());
        */







//        return Collections.singletonList( // it's cleaner and safer when you only need one item
//                CatalogItem.builder()
//                        .name("Transformers")
//                        .desc("Test")
//                        .rating(4)
//                        .build()
//        );



        /*
        List<Rating> ratings = Arrays.asList(
                Rating.builder().movieId("tt0111161").rating(5).build(),
                Rating.builder().movieId("tt0068646").rating(5).build(),
                Rating.builder().movieId("tt0468569").rating(5).build(),
                Rating.builder().movieId("tt1375666").rating(4).build()
//                Rating.builder().movieId("tt0133093").rating(4).build(),
//                Rating.builder().movieId("tt0109830").rating(4).build(),
//                Rating.builder().movieId("tt0120737").rating(5).build(),
//                Rating.builder().movieId("tt0167260").rating(5).build(),
//                Rating.builder().movieId("tt0080684").rating(4).build(),
//                Rating.builder().movieId("tt4154796").rating(4).build()
        );
        */
