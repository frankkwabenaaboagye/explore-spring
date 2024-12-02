package frank.springsecurity64;

import org.springframework.security.access.prepost.PostAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@PostAuthorize("returnObject?.owner == authentication?.name or hasRole('ACCOUNTANT')")
@Retention(RetentionPolicy.RUNTIME)
public @interface PostReadBankAccount {
}
