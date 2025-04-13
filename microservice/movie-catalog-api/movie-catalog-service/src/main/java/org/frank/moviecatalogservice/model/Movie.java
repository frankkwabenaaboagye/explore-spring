package org.frank.moviecatalogservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    private String movieId;
    private String name;

    public static final Movie NULL =  new Movie("0", "N/A");

}
