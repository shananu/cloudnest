Goal
Deploy CloudNest exactly like a production application.

Deployment Stack
React

↓

Nginx

↓

Spring Boot

↓

PostgreSQL

↓

Redis

↓

MinIO

Docker Services
Spring Boot
React
PostgreSQL
Redis
MinIO

Each service runs in its own container.

CI/CD
GitHub Actions

Pipeline
Push

↓

Build

↓

Run Tests

↓

Docker Build

↓

Deploy

Environment Variables
DB_URL
DB_USER
DB_PASSWORD
JWT_SECRET
MINIO_URL
MINIO_ACCESS_KEY
MINIO_SECRET_KEY
Never hardcode secrets.

Monitoring
Health Endpoint
/actuator/health

Future
Prometheus
Grafana
Logging

We'll use:
SLF4J
Logback

Structured logs.