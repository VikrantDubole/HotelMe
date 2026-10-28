# HotelMe architecture

HotelMe follows database-per-service ownership. Services communicate through authenticated APIs and Eureka discovery; no service reads another service's database.

| Service | Responsibility | Store |
| --- | --- | --- |
| API Gateway | OAuth2 login, authentication and routing | None |
| User Service | Guest profiles | MySQL |
| Hotel Service | Properties, room types and physical rooms | PostgreSQL |
| Booking Service | Nightly inventory, availability and reservations | PostgreSQL |
| Payment Service | Payment attempts and refunds | PostgreSQL |
| Rating Service | Guest reviews | MongoDB |

## Reservation consistency

Inventory is stored per hotel, room type and date. Booking locks every nightly row in one transaction, checks complete date coverage, decrements all nights and saves the reservation. Idempotency keys make retries safe. Cancellation restores the same inventory.

## Security boundaries

The gateway and business services validate JWTs. Guest ownership comes from the token subject, never request JSON. Administrative inventory operations require an admin authority. Credentials are supplied through environment variables.

## Modernization

The tutorial began on Spring Boot 2.7. Business slices remain buildable while services are migrated individually to Spring Boot 4.1 and Spring Cloud 2025.1. Remaining platform work includes OpenTelemetry, centralized metrics/logging, container images, CI and contract tests.
