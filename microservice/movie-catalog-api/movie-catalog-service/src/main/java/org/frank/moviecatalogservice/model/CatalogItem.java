package org.frank.moviecatalogservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CatalogItem {

    private String name;
    private String desc;
    private int rating;


}
