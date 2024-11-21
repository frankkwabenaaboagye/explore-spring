- Speaker: Rob Winch, Spring Security Project Lead, Broadcom

- [Let's Explore Spring Security 6.4 (SpringOne 2024)](https://youtu.be/9eoi1TViceM?list=PLgGXSWYM2FpPDrv8zmf3oN6SX1prqmESN)

---

- Authorization focused

--

#### Note
 
- Authentication: Who is trying to access a particular resource
- Authorization: [Given the Authentication] Are they allowed to do so?
    - Method security focused


- bank account object, create a bank account service  (domain logic is not really important)
- create test for the bank account service & test

```java

public class BankAccountServiceImpl {

    public BankAccount findById(long id) {
        return new BankAccount(id, "Frank", "4990028101", 10000);
    }

    public BankAccount getById(long id) {
        return findById(id);
    }
}

//---

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


```
- starting the implementation
    - with this new implementation, our test passes
    

```java

public class BankAccountServiceImpl {

    public BankAccount findById(long id) {
        BankAccount account = new BankAccount(id, "Frank", "4990028101", 10000);

        // the security context holder contains the current authentication
            // so say, doing security manually
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        if(!principal.getName().equals(account.getOwner())){
            throw new AuthorizationDeniedException("Denied", new AuthorizationDecision(false));
        }
        return account;
    }

    public BankAccount getById(long id) {
        return findById(id);
    }
}

```

- But there are couple of things
    - there is a lot of work done to log the user in. - a lot done manually
    - also we are mixing the domain logic with the `BankAccountServiceImpl` with the authorization
    - we utilise spring security here.
        - lets do it by manually creating a proxy [a thing that acts on behalf of another]
            - creating a proxy object manually - `BankAccountServiceProxy`
            - so the `BankAccountServiceProxy` is an instance of the `BankAccountServiceImpl`  ::: A class based proxy
            - we are going to do the authorization in the proxy


```java

public class BankAccountServiceImpl {

    public BankAccount findById(long id) {
        BankAccount account = new BankAccount(id, "Frank", "4990028101", 10000);

        return account;
    }

    public BankAccount getById(long id) {
        return findById(id);
    }
}


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

// tests remain the same

// Note that we only overode the findById
```

- From the above the 2 tests passes and two fails just like before
- For all the tests to pass

```java

class BankAccountServiceImplTest {

    BankAccountServiceImpl account = new BankAccountServiceProxy();  // moved from BankAccountServiceImpl();

    //... rest of the test remain the same
    // TESTs passed now
}

// we we test the getByID, it uses the proxy; since that was overriden

```

- Now!! interface based proxy

- If the `BankAccountServiceImpl` was to have the `final` keyword
    - in the proxy, we cannot extend it anymore
    - and spring security too will have the same limitation, it can't extend the final class
    - what you can do is to extract out an interface

```java

public interface BankAccountService {
    BankAccount findById(long id);

    BankAccount getById(long id);
}

//---

public class BankAccountServiceImpl implements BankAccountService {

    @Override
    public BankAccount findById(long id) {
        BankAccount account = new BankAccount(id, "Frank", "4990028101", 10000);

        return account;
    }

    @Override
    public BankAccount getById(long id) {
        return findById(id);
    }
}


// --


public class BankAccountServiceProxy implements BankAccountService{

    final BankAccountService delegate;

    public BankAccountServiceProxy(BankAccountService delegate) {
        this.delegate = delegate;
    }

    @Override
    public BankAccount getById(long id) {

        BankAccount account  = delegate.getById(id);

        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        if(!principal.getName().equals(account.getOwner())){
            throw new AuthorizationDeniedException("Denied", new AuthorizationDecision(false));
        }

        return account;
    }

    @Override
    public BankAccount findById(long id) {
        BankAccount account = delegate.findById(id);

        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        if(!principal.getName().equals(account.getOwner())){
            throw new AuthorizationDeniedException("Denied", new AuthorizationDecision(false));
        }

        return account;
    }

}

//--

class BankAccountServiceImplTest {

    BankAccountService account = new BankAccountServiceProxy(new BankAccountServiceImpl());

    // this fails because, now the proxy is an instance of the interface and not the class ::: BankAccountServiceImpl account = new BankAccountServiceProxy(new BankAccountServiceImpl());

    //... rest of the test code remains the same
}
```

- Tests passed!!

---

- But lets clean up test as well as use some build in features of spring security
    -  with some best pratices

- We take off the maual login  and the clean up
- add `@SpringBootTest`
- add the `@WithMockerUser()`
- Note
    - this is all we have to do to login and clean up the context afterward

```java



@SpringBootTest
class BankAccountServiceImplTest {

    BankAccountService account = new BankAccountServiceProxy(new BankAccountServiceImpl());


    @Test
    @WithMockUser("Frank")
    void findByIdWhenGranted(){
        this.account.findById(1);
    }

    //.... continue


}


```