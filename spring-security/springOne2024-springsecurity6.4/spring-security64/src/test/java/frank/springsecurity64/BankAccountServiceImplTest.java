package frank.springsecurity64;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BankAccountServiceImplTest {

    BankAccountServiceImpl account = new BankAccountServiceImpl();

    void login(String user){
        Authentication auth = new TestingAuthenticationToken(user, "password", "ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    // clean up
    @AfterEach
    void cleanUp(){
        SecurityContextHolder.clearContext();
    }

    @Test
    void findByIdWhenGranted(){
        // since our account always belong to Frank, we want to log in as such
        login("Frank");
        this.account.findById(1);
    }

    @Test
    void getByIdWhenGranted(){
        login("Frank");
        this.account.getById(1);
    }

    // so Ben should not be able to access the findById & getById
    @Test
    void findByIdWhenDenied(){
        login("Ben");
        assertThatExceptionOfType(AuthorizationDeniedException.class)
                .isThrownBy(()->this.account.findById(1));
    }

    @Test
    void getByIdWhenDenied(){
        login("Ben");
        assertThatExceptionOfType(AuthorizationDeniedException.class)
                .isThrownBy(()->this.account.getById(1));
    }

}