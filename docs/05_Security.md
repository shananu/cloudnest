1. Security Overview
# Security Design

CloudNest follows a defense-in-depth security model where security is implemented at multiple layers of the application rather than relying on a single mechanism.

Security measures include:

- JWT Authentication
- Refresh Tokens
- Password Hashing
- Role-Based Access Control (RBAC)
- Signed URLs
- File Validation
- MIME Type Verification
- Rate Limiting
- Input Validation
- Secure HTTP Headers
- Audit Logging

2. Authentication Strategy

We'll use:

Access Token
+
Refresh Token
Why not just JWT?

JWTs are stateless, but if one is stolen, it remains valid until it expires.

We'll solve this by using:

Short-lived Access Token
Long-lived Refresh Token

Access Token

Lifetime:

15 minutes

Contains

User ID

Email

Role

Used for

Every authenticated API
Refresh Token

Lifetime

7 days

Stored inside PostgreSQL.

Benefits

Logout
Token revocation
Multiple devices
Session management
Authentication Flow
Login

↓

Verify Password

↓

Generate Access Token

↓

Generate Refresh Token

↓

Return Both

↓

Access Token Expires

↓

Frontend Calls Refresh API

↓

New Access Token
3. Password Security

Passwords are never stored.

Instead

Password

↓

BCrypt

↓

Hash Stored

Example

$2a$10$...

Why BCrypt?

Salt built in
Slow by design
Resistant to brute-force attacks
4. Authorization (RBAC)

Roles

USER

ADMIN

Later

TEAM_ADMIN

ORGANIZATION_ADMIN

Permissions

Owner

Editor

Viewer

These are separate from application roles.

Example

Admin

↓

Can manage users

----------------

Viewer

↓

Can only download shared file
5. File Validation

Before upload

Backend verifies

Maximum Size

Allowed MIME Type

Filename

Extension

Allowed

PDF

PNG

JPEG

DOCX

ZIP

Blocked

.exe

.bat

.sh

.dll
6. MIME Type Verification

Attack

Rename

virus.exe

↓

virus.pdf

Backend checks

Content-Type

AND

Actual File Signature (Magic Bytes)

Never trust filename alone.

7. Virus Scanning

Immediately after upload

Upload Complete

↓

Background Worker

↓

ClamAV

↓

Safe?

↓

Yes → Available

↓

No → Delete + Notify User

For MVP, we can initially simulate the virus scan and later integrate ClamAV via Docker.

8. Signed URLs

Instead of exposing MinIO directly

Backend

↓

Permission Check

↓

Generate Signed URL

↓

Browser Downloads File

URL expires in

5 minutes

Benefits

No permanent public links
Temporary access
Reduced backend load
9. CORS

Allowed Origins

http://localhost:5173

https://cloudnest.app

Allowed Methods

GET

POST

PUT

PATCH

DELETE

Never use

*

in production.

10. CSRF

Because we use JWT Authorization headers instead of cookie-based authentication,

CSRF risk is significantly reduced.

If we later move refresh tokens to HttpOnly cookies, we'll add CSRF protection using Spring Security.

11. Rate Limiting

Prevent

Login brute force

Spam uploads

API abuse

Example

POST /login

Maximum

5 requests/minute/IP

Possible implementations:

Bucket4j
Redis-based rate limiting
12. Input Validation

Validate

Email

UUID

Folder Name

Filename

Passwords

Query Parameters

Never trust frontend validation.

13. SQL Injection Protection

We'll use:

Spring Data JPA

Prepared Statements

Parameterized Queries

Never concatenate SQL strings manually.

14. XSS Protection

Escape user-generated content before rendering.

React helps by escaping values by default, but avoid using dangerouslySetInnerHTML unless absolutely necessary.

15. Security Headers

Configure:

X-Content-Type-Options

X-Frame-Options

Content-Security-Policy

Referrer-Policy

Strict-Transport-Security

Spring Security provides support for these headers.

16. Audit Logging

Every important action is recorded.

Examples:

LOGIN

LOGOUT

UPLOAD

DELETE

DOWNLOAD

SHARE

RESTORE

PASSWORD_CHANGE

This supports compliance, debugging, and security investigations.

17. Secrets Management

Never commit:

JWT Secret

Database Password

SMTP Password

AWS Keys

Use:

.env

Environment Variables

Docker Secrets (future)


18. Engineering Decisions
Decision	Reason
JWT + Refresh Tokens	Stateless authentication with secure session renewal
BCrypt	Industry-standard password hashing
Signed URLs	Secure, temporary file access
MIME + Magic Byte Validation	Prevent spoofed file uploads
Rate Limiting	Mitigate brute-force and abuse
Audit Logs	Security and traceability
Environment Variables	Keep secrets out of source control

⭐ One Improvement to Make CloudNest Stand Out

Most student projects store the JWT in localStorage.

We're going to do something more production-oriented.

Recommended approach
Access Token: Stored in memory (React state/context).
Refresh Token: Stored in an HttpOnly, Secure cookie.
Why?

If an attacker injects JavaScript (XSS):

❌ localStorage tokens can be stolen.
✅ HttpOnly cookies cannot be read by JavaScript.

This is the pattern used by many production applications because it significantly reduces the impact of XSS attacks.