
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




 