package frank.product.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductPurchaseException extends RuntimeException {
    private final String message;
}
