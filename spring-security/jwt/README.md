[Spring Boot 3 + Spring Security 6 - JWT Authentication and Authorisation ](https://youtu.be/KxqlJblhzfI)

![alt text](image.png)

### JWT Validation Mechanism
- client sends a request to the backend system
- hits the filter (the first thing that gets executed in the application)
    - internal check to check whether we have the jwt token or not
    - if no token is present
        - send a 403 response to the client
    - once it checks, it extracts the subject ( username or email (well depending))
    - the internal excution of the filter, makes a call using the UDS (user details service), to try and get  user information
    from the database. it ddoes this using the say, email it has extracted froom the token
        - if the user does not exist, send a 403 response
    - a validation process starts once the user exists
- so the validate jwt will call the jwt service
    - if the token is invalid (exipred, not for that user)
        - send a 403 back
    - if everything is fine, then you will update the SCH (security context holder)
- once this happens the request will go t the dospactcher servlet -> then to the controller -> send the response successfully

### Implementing
- notice that we needed a UserDetailsService
- so for a user in the application, implement the UserDetailsService
