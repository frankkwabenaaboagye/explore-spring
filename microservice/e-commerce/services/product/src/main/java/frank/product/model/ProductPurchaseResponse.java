package frank.product.model;

import java.math.BigDecimal;

public record ProductPurchaseResponse(

        Integer productId,
        String name,
        String description,
        BigDecimal price,
        Double quantity

) {
}
