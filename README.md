# Destinations API

A Java Spring Boot REST API for managing travel-related data such as destinations, hotels, and planned routes. The project is a backend learning exercise using Spring MVC, Spring Data JPA, and an H2 database.

## Technology stack

- Java 21
- Spring Boot 3.5.7
- Spring Web
- Spring Data JPA
- H2 in-memory database
- Maven
- Dockerfile included in the repository

## Main domain areas

- Destinations
- Hotels
- Planned routes and route days

The application includes controller, service, repository, and model packages, plus JSON files used to load sample destination, hotel, and route data.

## Run locally

### Requirements

- JDK 21
- Docker (optional)

Run with the Maven wrapper:

```bash
git clone https://github.com/Manishr767/destinationsApi.git
cd destinationsApi
./mvnw spring-boot:run
```

The application is configured to use port **8099**. The H2 database is in-memory, so its contents are temporary and may be recreated when the application restarts.

## Database and sample data

The application uses H2 and has JSON data files under `src/main/resources/data/` for destinations, hotels, and planned routes. Hibernate is configured with schema update for development.

## Current scope

This repository is a development project. Before production use, it would need stronger request validation, consistent error handling, persistent database configuration, endpoint documentation, and broader automated tests.

## Possible improvements

- Add request/response DTOs and validation
- Use `BigDecimal` for monetary values
- Document endpoint paths and example requests
- Add service and controller tests
- Add PostgreSQL configuration for persistent environments

## Author

**Manish Rauniyar** · [GitHub](https://github.com/Manishr767)
