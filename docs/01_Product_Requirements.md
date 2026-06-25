# Product Requirements Document (PRD)

## 1. Document Information

| Field | Value |
|-------|-------|
| Project | CloudNest |
| Version | 1.0 |
| Status | Draft |
| Owner | Anushka Shanker |
| Last Updated | June 2026 |

---

# 2. Executive Summary

CloudNest is a production-grade cloud storage and collaboration platform built to demonstrate modern backend engineering practices.

Unlike a simple Google Drive clone, CloudNest is designed as a developer-first storage platform that emphasizes scalable architecture, secure file management, object storage, caching, asynchronous processing, and production-ready deployment.

The project serves both as a portfolio project and as a practical exploration of distributed backend systems.

---

# 3. Vision

To build a secure, scalable, and extensible cloud storage platform that follows the engineering practices of modern SaaS companies while remaining understandable, maintainable, and production-ready.

---

# 4. Problem Statement

Many student projects focus only on CRUD operations and basic authentication.

Real cloud storage systems solve significantly more challenging problems:

- Secure authentication
- Efficient storage
- File versioning
- Collaboration
- Permission management
- Large file uploads
- Background processing
- Search
- Scalability
- Monitoring

CloudNest aims to bridge this gap by implementing these concepts in a production-oriented manner.

---

# 5. Project Goals

### Primary Goals

- Build a scalable REST API.
- Implement secure authentication.
- Support object storage.
- Design a normalized relational database.
- Implement file versioning.
- Support collaboration and sharing.
- Introduce asynchronous background workers.
- Implement caching with Redis.
- Deploy using Docker.

### Secondary Goals

- Demonstrate clean architecture.
- Showcase backend engineering skills.
- Learn production deployment.
- Improve system design knowledge.

---

# 6. Target Users

- Individual Users
- Small Teams
- Organizations
- Developers using CloudNest APIs

---

# 7. User Personas

### Personal User

Needs secure cloud storage.

### Team Member

Shares files with colleagues.

### Organization Admin

Manages permissions and audit logs.

### Developer

Uses CloudNest APIs for storing application assets.

---

# 8. Functional Requirements

## Authentication

- Register
- Login
- Logout
- Refresh Tokens
- Password Reset
- Email Verification

## File Management

- Upload
- Download
- Rename
- Delete
- Move
- Copy

## Folder Management

- Nested folders
- Folder hierarchy
- Restore deleted folders

## Collaboration

- Share files
- Permission management
- Public links

## Versioning

- Maintain history
- Restore versions

## Search

- Filename search
- Extension search
- Owner search

## Notifications

- Upload complete
- Shared files
- Processing completed

---

# 9. Non-Functional Requirements

## Performance

- Low API latency
- Pagination
- Optimized queries

## Scalability

- Stateless backend
- Horizontal scaling
- Redis cache

## Security

- JWT
- BCrypt
- Signed URLs
- File validation
- Rate limiting

## Reliability

- Logging
- Retry failed jobs
- Health endpoints

## Maintainability

- Clean Architecture
- SOLID Principles
- Documentation-first

---

# 10. User Stories

### Authentication

As a user,

I want to securely log in,

so that my files remain protected.

---

### Upload

As a user,

I want to upload files,

so that I can access them later.

---

### Sharing

As a user,

I want to share files,

so that others can collaborate.

---

### Versioning

As a user,

I want previous versions,

so that I can restore accidental changes.

---

# 11. Success Metrics

- Secure authentication
- Reliable uploads
- Correct file versioning
- Fast search
- Responsive UI
- Production deployment
- Comprehensive documentation

---

# 12. Out of Scope (MVP)

The following are intentionally excluded from the initial release:

- Google Docs editing
- Real-time collaborative editing
- Mobile applications
- Offline synchronization
- AI-powered file organization

---

# 13. Future Enhancements

- OCR search
- AI tagging
- Team workspaces
- Storage analytics
- Public APIs
- Webhooks
- CDN integration

---

# 14. Assumptions & Constraints

- Internet connectivity is required.
- PostgreSQL is the primary database.
- Object storage uses MinIO during development.
- Redis is used only for caching.

---

# 15. Risks

- Large file uploads
- Storage costs
- Security vulnerabilities
- Scaling background jobs
- Object storage availability