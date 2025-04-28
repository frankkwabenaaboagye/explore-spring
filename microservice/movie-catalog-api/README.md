
# Movie Catalog Service API
- 3 microservices

# Inter-service Communication
- `RestTemplate`
  - old but still widely used
  - Blocking (synchronous)
  - Verbose error handling
- `WebClient`
  - new way
  - requires reactive programming

```java

// approach - 1

@Autowired
private RestTemplate restTemplate;

@GetMapping("/catalog/{userId}")
public List<CatalogItem> getCatalog(@PathVariable String userId) {
    Rating rating = restTemplate
            .getForObject("http://ratings-data-service/ratings/" + userId, Rating.class);
    // build and return response
}


// approach - 2

private final WebClient webClient = WebClient.builder().baseUrl("http://ratings-data-service").build();

public Mono<Rating> getRating(String userId) {
    return webClient.get()
            .uri("/ratings/{userId}", userId)
            .retrieve()
            .bodyToMono(Rating.class);
}

// Or if using it synchronously in a blocking system

Rating rating = webClient.get()
        .uri("/ratings/{userId}", userId)
        .retrieve()
        .bodyToMono(Rating.class)
        .block(); // blocks to get result



// Cloud-native	
- WebClient + Eureka + @LoadBalanced

Note:
 - WebClient is in the reactive programming space


```

- using webclient

```java
<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

---

- will be using rest template  - although a traditional way


---

- currently

![](./images/so-far.png)
![](./images/url-hardcode.png)


### Service Discovery


#### Client Side discovery

  - Each of the services registers with the discovery server

   ![](./images/service-register.png)

  - now the discovery server knows where each service is

  - a client look for a service 
    - communuicatin happens btn the client and the discovery servebr

    ![](./images/comm.png)

  - now the client knows where the service is and makes the call

   ![](./images/makescall.png)

  - this way of communication is very chatty


####  Server side discovery


  ![](./images/serversidedis.png)



#### Note
-  Spring uses client side discovery
- Technology to implement service discovery - `Eureka`

![](./images/eurekacns.png)

- making it work
![](./images/makingitwork.png)

- Eureka server
  - spring boot application + 
  - eureka server dependency
  - `@EnableEurekaServer`
  - using port: 8761

- to prevent the eureka server from registering with itself:

```java
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```
- note: we can also have mutiple instances of eureka servers just as we have multiple instances of eureka clients
- every eureka server is also a eureka client


### add the eureka client dependency to the other micsroservices
```java
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>

```

- if you are running eureka server not on the default port
 - the you have to specify that in the clients property file

```java
# Tell the client where the Eureka Server is
eureka.client.service-url.defaultZone=http://localhost:PORT/eureka

# if username and password
http://user:password@localhost:9090/eureka

#  multiple Eureka servers
eureka.client.service-url.defaultZone=http://server1:9090/eureka,http://server2:9091/eureka
```

### how do we consume it?
- we can say
 - we will be using rest template to make the call to service discovery, to get the service location
 - then use the same rest template to make another api call -> using that service location

- BUT rest template has the ability to hide that from us - GOOD!

- we can tell rest template - "I want you to call the service discovery everytime" 
 - we are doing to give the service name

- use `@LoadBalanced`
 - it does service discovery in a load balanced way

```java
@Bean
@LoadBalanced
public RestTemplate restTemplate() {
        return new RestTemplate();
}

// so now we move from
restTemplate.getForObject("http://localhost:8083/ratingsdata/users/" + userId, UserRating.class);

// to this
restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId, UserRating.class);

// so the rest template is not making the call directly, it is going to the eureka instead


// same for this
restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
// to
restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);

```

- so standardize / agree on te applicaiton name


### case for loadbalanced

![](./images/caseforloadbal.png)

- one of the reasosns for the load balancing
- the way to see this in action
- take the jar and run the same application on a different port

- say the movie info service

```java
it is running on port 8082

// i have it jar file too started on in a different directory, different port

java -Dserver.port=8201 -jar movie-info-service-0.0.1-SNAPSHOT.jar

```

![](./images/testingloadba.png)

- so what happens when a erquest is made to the info-service?


- more programmatic control

```java

// just for programmatic control
    @Autowired
    private DiscoveryClient discoveryClient;
```


### fault tolerance
- what if service 2 say, goes down?

![](./images/servicedown.png)
 

- the solution is 
-  `heart beat`
  - send heart beat to the service registry

- what eureka client does by default
 - send ping the eureka server regularly to tell it that 'it is still there'

- what if the eureka server goes down?
 - it checks the cache

![](./images/serverdown.png)


- the above is being handled for us...

---
---



# Fault tolerance, Resilience

- Fault tolerance
  - if there is a fault what is the impact of the fault
- Resilience
  - how many faults can a system tolerate
  - can it bounce back?


- what are some of the problems with microservices
  - the instance going down
    - then "have multiple instances"
  - the instance being slow (cos of threads)
    - use timeouts


## timeouts
- first way - simpler
- the second way is the preferred way

- you can apply it to any service which calls another service

```java

 // rest template
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(3000);
        return new RestTemplate(clientHttpRequestFactory);
    }

```

- does this solve the problem?
  - PARTLY
 - threads still exists on the webserver
  - how?
   - if the number of request that are coming in (if that frequeny is > the time it takes to remove / end the thread)

- We move to the idle solution
    - when you relaise that there is something wrong with a particular microservice, dont even call it!!!
    - wait for a bit -- try agin -- if it is okay -- fine -- if not -- wait a bit --

- this approach is very common in fault tolerant systems - that is the circuit breaker pattern

![](./images/circuitbreaker.png)


- it can be rest (manually or automatically)

- how can we apply it to our microservices
    - so when should a circuit 'in this sense' break?
       - you need the logic - what are the parameters for the circuit to break?

- look at this scenario?
    - one might say, okay -  the last 3 request timed out
- what if you have request alternating btn succcess and error / timeout?
  
![](./images/timeout.png)


#### circuit breaker parameters

![](./images/cirsuitbealerparameters.png)


- [https://github.com/Netflix/Hystrix/wiki/Configuration](https://github.com/Netflix/Hystrix/wiki/Configuration)


#### What to do when the circuit breaks

![](./images/whattodo.png)

 - the beset option is probably - if you had a cache mechanism

 ![](./images/fallback.png)