package frank.springsecurity64;

import org.springframework.security.access.prepost.PostAuthorize;

public interface BankAccountService {

    @PostAuthorize("returnObject?.owner == authentication?.name")
    BankAccount findById(long id);

    @PostAuthorize("returnObject?.owner == authentication?.name")
    BankAccount getById(long id);
}
