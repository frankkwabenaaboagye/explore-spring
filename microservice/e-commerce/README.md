Spring boot, Spring Cloud and Keycloak
[https://youtu.be/jdeSV0GRvwI](https://youtu.be/jdeSV0GRvwI)

- structure

```text

+----------------+                           +-----------------------------+
|  config server |                           |   discovery / eureka server |
|    (8888)      |                           |    (8761)                   |
+----------------+                           +-----------------------------+
    
    +----------------+               +----------------+              +----------------+
    |   customer msc |               |   product msc  |              |   service E    |
    |    (8090)      |               |    (8050)      |              |    (8085)      |
    |   mongodb      |               | postgres-flyway|              |   mongodb      |
    +----------------+               +----------------+              +----------------+

```


- spring initializr
```bash
- config server
  - dependencies
      - config server
- discovery server
  - dependencies
    - config client
    - eureka server
- microservices
  - customer
  - product
```

## The main Dockerfile
- [docker-compose.yml](/docker-compose.yml)
```dockerfile
docker compose up -d  # for detached mode
docker compose down
docker-compose down --volumes --remove-orphans

## Note
- check if running
    # this option was for those without the intelliJ ide foe the db plugin
    localhost:5050  # for the postgres in our docker file
    # http://localhost:5050/browser/
    on your local pgAdmin4
    > add a server
      name: general name
      host: the container name for the postgres itself (microservicePostgres)
      username: the username specified in the docker compse file
      password: the password specified in the docker compse file


- check if the mongo-express is running
    http://localhost:8081/
   
```


## Starting off with the Config server
- Enable Config server `@EnableConfigServer`
```bash
spring.application.name=configserver
spring.profiles.active=native
spring.cloud.config.server.native.search-locations=classpath:/configurations
server.port=8888


## we specify the properties 

main/
 |__resources/
    |__applications.properties
    |__configuations/
        |__discoveryserver.yml (we can use yml or properties) -> the properties for the discovery server (eureka server)
        |__customer.properties (we can use yml or properties)  -> the customer mcs configuration
  

## discoveryserver.yml (the eureka server) (same name as stated in the discoveryserver msc app name)
eureka:
  instance:
    hostname: localhost
  client:
    register-with-eureka: false (the eureka server should not register itself with itself)
    fetch-registry: false
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
server:
  port: 8761
  
 ## customer.properties
server.port=8090
spring.data.mongodb.username=root
spring.data.mongodb.password=example
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=customer
spring.data.mongodb.authentication-database=admin
```

## Discovery server (eureka)
- `Config client` -> allows us to connect to the configuration (in the config server) and get them
- Enable the server `@EnableEurekaServer`
```bash
spring.application.name=discoveryserver  # name is very important
spring.config.import=optional:configserver:http://localhost:8888  # telling it where it should pick the configurations from


# we start the config server @ port 8888
# we start the eureka server @ port 8761
  - it gets it configurations from the `config server`

```

## Microservices 

-  `customer`
```bash

- depedencies
  - config client, eureka client, web, validation, lombok, mongoDB e.t.c
  
create all the configurations for the customer microservice
- move it to the congfig server

```

- Note:
- we create the [./services/configserver/src/main/resources/configurations/application.yml](./services/configserver/src/main/resources/configurations/application.yml)
```bash
- configuration files inherit from each other
- there was a bit of configuration we needed to do 
   | resources/
        - application.properties
        - configurations /
            - application.yml (this is what we are creating for all other configurations to inherit from it)
            - customer.properties
            - discoveryserver.yml
            - product.yml
            
so what is in the applications.yml file
(this is what all other mcs will be using)
eureka:
  instance:
    hostname: localhost
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka

spring:
  cloud:
    config:
      override-system-properties: false 
      ( it is true by default ) 
      ( but we set to false ) 
      ( because we dont want to override any of the ) 
      ( properties that set in the respective mcs )

```

- `product`
```bash

one of the properties used here  is 
- spring.jpa.hibernate.ddl-auto=validate (we want to rely of flyway to create it for us)


```









