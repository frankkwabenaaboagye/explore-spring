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


```