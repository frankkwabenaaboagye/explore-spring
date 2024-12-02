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

// TEST passed!!!

```


---

- but you will also note that we are duplicating
    - "Frank" and "Ben"

- using another approach


```java

// we need the annotation to show up at runtime
@Retention(RetentionPolicy.RUNTIME)
// we take off the annotation from the test class itself and bring it here
@WithMockUser("Frank")
public @interface WithMockFrank {
}

//---


@Retention(RetentionPolicy.RUNTIME)
@WithMockUser("Ben")
public @interface WithMockBen {
}


// note that, these users do not exist in your database - we are mocking


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

// TEST passed
```

- Advantages of this is that:
    - if makes it really easy to change something about say ,"Frank", because it is centralised

---

- how can we still clean up the authorization logic?
- note that we are creating the proxies manually - but we dont have to do that 
- lets use spring security support

- `@PostAuthorize` : after the method is invoked, i want to do some authorisation logic

```java

/*
you will notice that in the hardcoded proxy, after we get the account object we do some
checks right?

// we follow that pattern here


// **returnObject** is a keyword for the BankAccount Object
// we add "?" for null check


*/

public interface BankAccountService {

    @PostAuthorize("returnObject?.owner == authentication?.name")
    BankAccount findById(long id);

    @PostAuthorize("returnObject?.owner == authentication?.name")
    BankAccount getById(long id);
}



```

- working on Enable method security

- lets use spring security
    - manually creating it first - spring security can do this authomatically

```java

// moving from 
// BankAccountService account = new BankAccountServiceProxy(new BankAccountServiceImpl());
// to

// this is how enable method security does 
// create an instance of the APF
    AuthorizationProxyFactory factory = AuthorizationAdvisorProxyFactory.withDefaults();
    BankAccountService account = (BankAccountService) factory.proxy (new BankAccountServiceImpl());


```

- we can takeout the duplicates using the same anotation approach

```java

@PostAuthorize("returnObject?.owner == authentication?.name")
@Retention(RetentionPolicy.RUNTIME)
public @interface PostReadBankAccount {
}

```

---

- more interesting
    - adding a new test to allow an accountant to access the bank account


```java

// the test
@Test
@WithMockAccountant
void findByIdWhenAccountant(){
    this.account.findById(1);
}

// now lets give the permissions
// check the SecurityExpressionRoot to follow on the stuffs like the hasRole, authentication and others
@PostAuthorize("returnObject?.owner == authentication?.name or hasRole('ACCOUNTANT')")
@Retention(RetentionPolicy.RUNTIME)
public @interface PostReadBankAccount {
}


// note that we already have this
@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(roles = "ACCOUNTANT")
public @interface WithMockAccountant {
}


```

---

- what if we don't want the accountant to get the account number
- they should only be able to see the balance


```java

@Test
@WithMockAccountant
void getAccountNumberWhenAccountant(){
    BankAccount account = this.account.getById(1);
    assertThatExceptionOfType(
            AuthorizationDeniedException.class
    ).isThrownBy(() -> account.getAccountNumber());
}


```

- but can I go into the `getAccountNumber` and do this?

```java

@PreAuthorize("this.owner == authentication.name")
public String getAccountNumber() {
    return accountNumber;
}

// this is going to work
```

- the above is not going to work
    - cos object is not being proxied

- we have to change this ... look at the `getById`, it infers from `findById` and it uses
    - `new BankAccount(....)`

```java

@Override
public BankAccount findById(long id) {
    BankAccount account = new BankAccount(id, "Frank", "4990028101", 10000);

    return account;
}

@Override
public BankAccount getById(long id) {
    return findById(id);
}

```

- so to resolve this, we can go with the  how we did this

```java

// create an instance of the APF
    AuthorizationProxyFactory factory = AuthorizationAdvisorProxyFactory.withDefaults();
    BankAccountService account = (BankAccountService) factory.proxy (new BankAccountServiceImpl());

// we do the above for the BankAccount7
// but that will be mixixng business logic with authorisation logic

```

- we can address it
- since our service looks like

```java

@PreAuthorize("this.owner == authentication.name") // we add this in the BankAccount class
public String getAccountNumber() {
    return accountNumber;
}


@PostReadBankAccount
BankAccount getById(long id);


// view the PostReadBankAccount we created
@PostAuthorize("returnObject?.owner == authentication?.name or hasRole('ACCOUNTANT')")
@Retention(RetentionPolicy.RUNTIME)
public @interface PostReadBankAccount {
}



// we can tweak it a little
@PostAuthorize("returnObject?.owner == authentication?.name or hasRole('ACCOUNTANT')")
@AuthorizeReturnObject  // this is what we added
@Retention(RetentionPolicy.RUNTIME)
public @interface PostReadBankAccount {
}


// spring security is using that factory automatically on the BankAccountService, 
// to the make the BankAccount (the return object), a proxy

```

- so this is important to note
    - A spring bean can have method security
    - Objects returned from the Spring Bean can also have method security

- now lets clean it up
    - since we are manually using proxy here

```java

    AuthorizationProxyFactory factory = AuthorizationAdvisorProxyFactory.withDefaults();

    BankAccountService account = (BankAccountService) factory.proxy (new BankAccountServiceImpl());

```

- cleaned up version

```java

// Autowired

@SpringBootTest
class BankAccountServiceImplTest {

    @Autowired
    BankAccountService account;

    ...
    ...

}


// make the service a spring bean with @Service annotation

@Service
public class BankAccountServiceImpl implements BankAccountService {

    ...
    ...
}


//enable method security
@SpringBootApplication
@EnableMethodSecurity
public class SpringSecurity64Application {
    ...
    ...
}
```

- what is we don't want the ` assertThatExceptionOfType(AuthorizationDeniedException.class)` all over in the test
- we can do something about it, even customise it
    - the way to go about it say, using  a mask


```java

@Test
@WithMockAccountant
void getAccountNumberWhenAccountant(){
    BankAccount account = this.account.getById(1);
    assertThat(account.getAccountNumber()).isEqualTo("**-**");
     // this is saying that If I am invoking the getAccountNumber and I am not allowed to do it, I want it to be masked
}


@Test
@WithMockFrank
void getAccountNumberWhenFrank(){
    BankAccount account = this.account.getById(1);
    assertThat(account.getAccountNumber()).isEqualTo("4990028101"); // 4990028101 is the account number we hard-coded
}


// we add a new class, we make it a spring bean, it implements MethosAuthorizationDeniedHandler

@Component
public class MaskAuthorizationDeniedHandler implements MethodAuthorizationDeniedHandler {
    @Override
    public Object handleDeniedInvocation(MethodInvocation methodInvocation, AuthorizationResult authorizationResult) {
        return "**-**";
    }
}

// now modify the BankAccount Object
@PreAuthorize("this.owner == authentication.name")
@HandleAuthorizationDenied(handlerClass = MaskAuthorizationDeniedHandler.class) // we add this
public String getAccountNumber() {
    return accountNumber;
}


// when we run the test it should work

```
