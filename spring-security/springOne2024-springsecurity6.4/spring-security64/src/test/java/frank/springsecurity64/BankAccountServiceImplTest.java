package frank.springsecurity64;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.authorization.AuthorizationProxyFactory;
import org.springframework.security.authorization.method.AuthorizationAdvisorProxyFactory;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class BankAccountServiceImplTest {

    @Autowired
    BankAccountService account;


    @Test
    @WithMockAccountant
    void findByIdWhenAccountant(){
        this.account.findById(1);
    }

    @Test
    @WithMockAccountant
    void getByIdWhenAccountant(){
        this.account.getById(1);
    }

    @Test
    @WithMockAccountant
    void getAccountNumberWhenAccountant(){
        BankAccount account = this.account.getById(1);
        assertThatExceptionOfType(
                AuthorizationDeniedException.class
        ).isThrownBy(() -> account.getAccountNumber());
    }


    @Test
    @WithMockFrank
    void findByIdWhenGranted(){
        this.account.findById(1);
    }

    @Test
    @WithMockFrank
    void getByIdWhenGranted(){
        this.account.getById(1);
    }

    @Test
    @WithMockBen
    void findByIdWhenDenied(){
        assertThatExceptionOfType(AuthorizationDeniedException.class)
                .isThrownBy(()->this.account.findById(1));
    }

    @Test
    @WithMockBen
    void getByIdWhenDenied(){
        assertThatExceptionOfType(AuthorizationDeniedException.class)
                .isThrownBy(()->this.account.getById(1));
    }
}