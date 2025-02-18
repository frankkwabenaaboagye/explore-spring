Spring boot, Spring Cloud and Keycloak
[https://youtu.be/jdeSV0GRvwI](https://youtu.be/jdeSV0GRvwI)


Docker
```dockerfile
docker compose up -d  # for detached mode
docker compose down

docker-compose down --volumes --remove-orphans

```

Note
- check if running

```bash
# this option was for those without the intelliJ ide foe the db plugin
localhost:5050  # for the postgres in our docker file
# http://localhost:5050/browser/
> add a server
  name: general name
  host: the container name for the postgres itself (microservicePostgres)
  username: the username specified in the docker compse file
  password: the password specified in the docker compse file


```

- check if the mongo-express is running

```bash
localhost:8081
```


- spring initializr
```bash
- config server
  - dependencies
      - config server
```

- config server
```bash
@EnableConfigServer
port: 8888
config.server.native.search-locations=classpath:/configurations  (under the resources)


```

- discovery server
```bash
dependencies
Config client -> allows us to connect to the configuration (in the config server) and get them

eureka server -> our discovery server

@EnableEurekaServer

> we have to tell it where it should pick the configuration from 
> note that our config server is running on port  8888 


spring.application.name=discoveryserver  # name is very important

spring.config.import=optional:configserver:http://localhost:8888


```

- configuring the config server
```bash
- we made a dir called `configurations` in the config server
- so we go to the configurations folder and create a file 
  - with the same name as the (name of the discovery server application name of its app properties)
  
  
eureka:
  instance:
    hostname: localhost
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/  # the last '/' is very important
server:
  port: 8761

```

- test it
```bash
go to the url of the discovery server
 - in this case the http://localhost:8761   : to see the spring eureka UI

```


- now we create our microservices

- beginning with `customer`
```bash

- depedencies
  - config client, eureka client, web, validation, lombok, mongoDB e.t.c
  
create all the configurations for the customer microservice
- move it to the congfig server

```









