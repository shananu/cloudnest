| Decision             | Why We Chose It                                      |
| -------------------- | ---------------------------------------------------- |
| Spring Boot          | Mature ecosystem, enterprise standard                |
| Java                 | Excellent performance and strong typing              |
| PostgreSQL           | ACID compliance, relational metadata fits the domain |
| MinIO                | S3-compatible and ideal for local development        |
| Redis                | Fast caching and reduced database load               |
| JWT + Refresh Tokens | Stateless authentication with secure session renewal |
| REST                 | Well suited to CRUD-heavy APIs                       |
| Layered Architecture | Separation of concerns and easier testing            |
| UUIDs                | Harder to guess and better for distributed systems   |
| Object Storage       | Databases are inefficient for large binary objects   |
| Background Workers   | Keeps user-facing APIs responsive                    |
| Docker               | Consistent development and deployment environments   |
