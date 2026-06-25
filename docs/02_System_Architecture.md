# System Architecture

1. Overview
CloudNest follows a layered architecture with clear separation of responsibilities.

The frontend communicates with the backend through REST APIs.

The backend manages authentication, authorization, business logic, metadata, and communication with external services.

Actual file contents are stored in object storage (MinIO during development and AWS S3 in production).

Metadata is stored in PostgreSQL while Redis accelerates frequently accessed data.

Long-running tasks such as thumbnail generation and virus scanning are executed asynchronously by background workers.

2. High-Level Architecture
This is our first architecture diagram.

                        +----------------------+
                        |   React Frontend     |
                        +----------+-----------+
                                   |
                              HTTPS / REST
                                   |
                        +----------v-----------+
                        | Spring Boot Backend  |
                        +----------+-----------+
                                   |
         +-----------+-------------+---------------+--------------+
         |           |                             |              |
         |           |                             |              |
+--------v-----+ +---v---------+           +-------v------+ +-----v------+
| PostgreSQL   | | Redis Cache |           |   MinIO      | | Email SMTP |
| Metadata DB  | | Sessions    |           | Object Store | | Notifications|
+--------------+ +-------------+           +--------------+ +------------+
                                   |
                                   |
                        +----------v-----------+
                        | Background Workers   |
                        +----------------------+

Why this architecture?
Each service has a single responsibility.

Component	| Responsibility
React	    | UI
Spring Boot	| Business logic
PostgreSQL	| Metadata
MinIO	    | Actual files
Redis	    | Cache
Workers	    | Heavy tasks

This separation allows every component to scale independently.

3. Component Responsibilities

Frontend
Responsible for
~ Authentication
~ Upload UI
~ Folder explorer
~ Dashboard
~ Search
~ Sharing
It never directly accesses the database.

Backend
Responsible for
~ Business logic
~ Authentication
~ Authorization
~ Validation
~ Metadata
~ Signed URLs
~ Storage integration

PostgreSQL
Stores only metadata.
Never actual files.

Example:

File Name
Size
Owner
Upload Date
Storage Path
Version
Permissions

MinIO
stores
PDFs
Images
Videos
ZIP files
Documents
No relational data.

Redis
caches
User Session
Frequently Accessed Metadata
Recent Files
Storage Statistics
Refresh Token Blacklist (optional)

Background Workers
responsible for
Thumbnail
OCR
Virus Scan
Metadata Extraction
Notifications
Analytics

4. Request Flow
Normal API request:

Browser

↓

Spring Security

↓

Authentication

↓

Controller

↓

Service

↓

Repository

↓

PostgreSQL

↓

DTO

↓

JSON Response

Notice:
Controllers never directly query the database.

5. Authentication Flow
User

↓

POST /login

↓

Authentication Manager

↓

Verify Password

↓

Generate JWT

↓

Generate Refresh Token

↓

Return Tokens

↓

Frontend Stores

↓

Future Requests

↓

Authorization Filter

↓

Access Granted

6. File Upload Flow
User Selects File

↓

Frontend Validation

↓

POST Upload Request

↓

Backend Authentication

↓

Validate File

↓

Upload to MinIO

↓

Store Metadata

↓

Trigger Background Worker

↓

Return Success

Why upload to MinIO instead of PostgreSQL?
Because databases are optimized for structured data.
Object storage is optimized for large files.

7. File Download Flow
User Clicks Download

↓

Backend Checks Permission

↓

Generate Signed URL

↓

Return URL

↓

Browser Downloads File

↓

MinIO Serves File

Notice:

The backend does not stream every file.
Instead, it generates a pre-signed URL.
This reduces backend load significantly.

Why Pre-Signed URLs?
Imagine:
10,000 users downloading 500MB videos.
Without pre-signed URLs

Browser

↓

Backend

↓

Storage

↓

Backend

↓

Browser

Backend becomes the bottleneck.
With pre-signed URLs

Browser

↓

Backend (Authorization)

↓

Signed URL

↓

Browser

↓

MinIO

Backend only authorizes.
Storage serves the file directly.
This is how AWS S3 is commonly used.

8. Sharing Flow
Owner Shares File

↓

Permission Created

↓

Recipient Requests File

↓

Permission Checked

↓

Generate Signed URL

↓

Download Allowed

9. Background Processing
Immediately after upload

Upload Complete

↓

Publish Event

↓

Background Worker

↓

Generate Thumbnail

↓

Extract Metadata

↓

Virus Scan

↓

Update Database

↓

Notify User

10. Caching Strategy
Redis stores
Recent Files
Storage Dashboard
Folder Metadata
Frequently Downloaded Files
Session Data

Benefits:

Faster API responses
Reduced database load
Better scalability

11. Scalability

CloudNest is designed to scale horizontally.

                 Load Balancer
                     |
      +--------------+--------------+
      |                             |
+-----v------+               +------v------+
| Spring App |               | Spring App  |
+------------+               +-------------+
       |                           |
       +-------------+-------------+
                     |
                PostgreSQL
                     |
                 Redis Cache
                     |
                  MinIO Cluster

Adding more backend instances does not require code changes.

12. Failure Handling

Examples:

MinIO unavailable
Retry upload
Return meaningful error
Preserve transaction integrity
Database unavailable
Abort request
Log error
Return HTTP 500
Worker failure
Retry
Dead-letter queue (future)
Error logging

13. Future Architecture
Future improvements
RabbitMQ
Kafka
ElasticSearch
CDN
Prometheus
Grafana
Kubernetes
AWS ECS
CloudFront

These are intentionally left out of the MVP but the architecture allows for their integration.

14. Engineering Decisions

Decision	            Why
Spring Boot	       Mature ecosystem, enterprise standard, excellent security and dependency injection
PostgreSQL	            Strong ACID guarantees, relational data fits metadata and permissions well
MinIO	                S3-compatible, easy local development, seamless migration to AWS S3
Redis	                High-speed caching to reduce database load
JWT + Refresh Tokens	Stateless authentication with secure session renewal
REST APIs	            Simpler for CRUD-heavy applications and widely adopted
Layered Architecture	Clear separation of concerns and easier testing/maintenance
Background Workers	    Keeps uploads responsive by offloading long-running tasksx