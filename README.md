# Polliste

# Changelog
## 0.4.0
* Rewritten application to use:
   * Spring-boot 2.3.4
   * Flyway 6.4.4
   * Kotlin 1.4.10
   * Java 14
   * Jackson 2.11.3
  
## 0.6.0
* Refactor API
* Java 15

# Running locally
Currently a local Postgresql database is required.

## Posgresql and Docker
```
docker run --rm --name test-instance -e POSTGRES_PASSWORD=password -p 5432:5432 postgres
ctrl-c
docker exec -it test-instance /bin/bash

psql postgres postgres

CREATE ROLE polet LOGIN PASSWORD 'polet';
CREATE DATABASE polet;
GRANT ALL PRIVILEGES ON DATABASE polet to polet;
```

## Frontend development
Easiest way to do frontend development is to run vue by itself.
`frontend> npm run serve`
and run backend with IntelliJ. Application is then available at http://localhost:8001

## API documentation
OpenAPI 3.0 documentation availablie at http://localhost:8000/v3/api-docs
Redoc: http://localhost:8000/redoc.html
Swagger-ui: http://localhost:8000/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config
