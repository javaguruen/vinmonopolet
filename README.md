# Polliste

# Changelog
## 0.4.0
* Rewritten application to use:
   * Spring-boot 2.3.4
   * Flyway 6.4.4
   * Kotlin 1.4.10
   * Java 14
   * Jackson 2.11.3
   
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
