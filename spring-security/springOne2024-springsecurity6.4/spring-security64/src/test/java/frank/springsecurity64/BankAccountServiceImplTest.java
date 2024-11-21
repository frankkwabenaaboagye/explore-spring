package frank.springsecurity64;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class BankAccountServiceImplTest {

    BankAccountService account = new BankAccountServiceProxy(new BankAccountServiceImpl());


    @Test
    @WithMockUser("Frank")
    void findByIdWhenGranted(){
        this.account.findById(1);
    }

    @Test
    @WithMockUser("Frank")
    void getByIdWhenGranted(){
        this.account.getById(1);
    }

    @Test
    @WithMockUser("Ben")
    void findByIdWhenDenied(){
        assertThatExceptionOfType(AuthorizationDeniedException.class)
                .isThrownBy(()->this.account.findById(1));
    }

    @Test
    @WithMockUser("Ben")
    void getByIdWhenDenied(){
        assertThatExceptionOfType(AuthorizationDeniedException.class)
                .isThrownBy(()->this.account.getById(1));
    }
}