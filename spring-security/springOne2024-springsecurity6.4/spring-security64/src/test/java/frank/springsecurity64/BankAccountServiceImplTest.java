package frank.springsecurity64;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.authorization.AuthorizationProxyFactory;
import org.springframework.security.authorization.method.AuthorizationAdvisorProxyFactory;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class BankAccountServiceImplTest {

    // create an instance of the APF
    AuthorizationProxyFactory factory = AuthorizationAdvisorProxyFactory.withDefaults();

    BankAccountService account = (BankAccountService) factory.proxy (new BankAccountServiceImpl());


    @Test
    @WithMockAccountant
    void findByIdWhenAccountant(){
        this.account.findById(1);
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