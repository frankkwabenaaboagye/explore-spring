package org.frank.ratingsdataservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRating {

    List<Rating> userRatings;

    public static final UserRating NULL = new UserRating(List.of(Rating.NULL));
}
