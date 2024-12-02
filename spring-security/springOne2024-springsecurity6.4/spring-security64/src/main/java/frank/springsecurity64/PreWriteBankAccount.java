package frank.springsecurity64;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("{theVariable}?.owner == authentication?.name")
public @interface PreWriteBankAccount {
    String theVariable();

    // String theVariable() default ""; // you can use this for default / if you have a default
    // String value(); // some uses "value", but it doesn't matter
}
 