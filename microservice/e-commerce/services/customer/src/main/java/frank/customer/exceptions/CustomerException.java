package frank.customer.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true) // will call the super call
@Data
public class CustomerException extends RuntimeException{
    private final String message;
}
