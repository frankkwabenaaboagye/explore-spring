package frank.product.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Category {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE) // mapped by = the name should match the field name in the Product class;  cascade.remove = when a category is deleted it deleted the products associated with it
    private List<Product> products;

}
