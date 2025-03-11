package frank.product.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;


public record ProductRequest(
        Integer id,
        @NotNull(message = "name is required") String name,
        @NotNull(message = "description is required") String description,
        @Positive(message = "price should be positive") BigDecimal price,
        @Positive(message = "quantity should be positive") Double availableQuantity,
        @NotNull(message = "category is required") Integer categoryId
) {
}
