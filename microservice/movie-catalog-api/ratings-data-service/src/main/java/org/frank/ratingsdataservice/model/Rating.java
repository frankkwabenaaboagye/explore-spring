package org.frank.ratingsdataservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    private String movieId;
    private int rating;

    // as a null rating
    public static final Rating NULL = new Rating("Nil", 0);
}
