package frank.product.model;

import jakarta.validation.constraints.NotNull;

public record ProductPurchaseRequest(
        @NotNull(message = "product id is mandatory") Integer productId,
        @NotNull(message = "quantity is mandatory") Double quantity
) {
}
