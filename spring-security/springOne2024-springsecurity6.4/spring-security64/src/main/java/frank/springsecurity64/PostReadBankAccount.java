package frank.springsecurity64;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authorization.method.AuthorizeReturnObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@PostAuthorize("returnObject?.owner == authentication?.name or hasRole('ACCOUNTANT')")
@AuthorizeReturnObject
@Retention(RetentionPolicy.RUNTIME)
public @interface PostReadBankAccount {
}
