package frank.springsecurity64;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.AuthorizationDeniedException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class BankAccountServiceImplTest {

    BankAccountService account = new BankAccountServiceProxy(new BankAccountServiceImpl());


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