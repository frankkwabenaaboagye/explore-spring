package frank.springsecurity64;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

public class BankAccountServiceProxy extends BankAccountServiceImpl{

    @Override
    public BankAccount findById(long id) {
        BankAccount account = super.findById(id);

        // we put the logic in here
            // Note that spring security will do this for you - generate the proxy automatically - classed based proxy - CGLIB
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        if(!principal.getName().equals(account.getOwner())){
            throw new AuthorizationDeniedException("Denied", new AuthorizationDecision(false));
        }

        return account;
    }
}
