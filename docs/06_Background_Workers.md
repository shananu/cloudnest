1. Overview
# Background Workers

CloudNest uses asynchronous background workers to execute long-running operations outside the request-response lifecycle.

This improves API responsiveness and provides a better user experience.

Initially, workers will be implemented using Spring Events and @Async.

The architecture is designed to support RabbitMQ or Kafka in the future without significant changes.

2. Worker Architecture
User Uploads File
        │
        ▼
Spring Boot API
        │
 Save Metadata
        │
 Publish Event
        │
 Return Success (HTTP 201)
        │
        ▼
 Background Worker
        │
 ├── Metadata Extraction
 ├── Thumbnail Generation
 ├── Virus Scan
 ├── Search Indexing
 └── Notification


3. Worker Types

Metadata Worker

Trigger
File Uploaded

Responsibilities
Extract MIME type
Calculate checksum
Read image/video metadata

Output
Update Files Table


Thumbnail Worker

Trigger
Image Uploaded

Responsibilities
Generate preview image
Store thumbnail
Update database

Virus Scan Worker

Trigger
File Uploaded

Future
ClamAV Docker Container

If infected
Delete Object
Notify User
Log Event

Search Index Worker

Trigger
Upload
Rename
Restore
Updates search index.

Future
ElasticSearch
Notification Worker

Responsible for
Upload Complete
File Shared
Version Restored
Storage Warning


4. Event Flow
File Upload

↓

FileUploadedEvent

↓

Event Listener

↓

Background Service

↓

Update Database


5. Retry Strategy

Failures should not lose data.

Example

Retry

↓

3 Attempts

↓

Log Failure

↓

Future DLQ


6. Engineering Decisions
Decision	Reason
Spring Events	Lightweight and simple for MVP
@Async	Non-blocking execution
Event-driven design	Loose coupling
RabbitMQ later	Better scalability when needed