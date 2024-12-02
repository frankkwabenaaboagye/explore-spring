package frank.springsecurity64;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.AuthorizationDeniedException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        assertThat(account.getAccountNumber()).isEqualTo("**-**");
    }


    @Test
    @WithMockFrank
    void getAccountNumberWhenFrank(){
        BankAccount account = this.account.getById(1);
        assertThat(account.getAccountNumber()).isEqualTo("4990028101"); // 4990028101 is the account number we hard-coded
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