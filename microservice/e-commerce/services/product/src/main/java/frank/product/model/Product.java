package frank.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String description;
    private BigDecimal price;
    private Double availableQuantity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
