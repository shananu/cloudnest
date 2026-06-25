1. Database Overview
CloudNest uses PostgreSQL as its primary relational database.

The database stores metadata about users, files, folders, permissions, versions, and activities.

Actual file contents are stored in object storage (MinIO during development and AWS S3 in production).

The schema follows Third Normal Form (3NF) to reduce redundancy while maintaining performance.

2. Entity Relationship Diagram (ERD)

                 Users
                   │
        ┌──────────┴──────────┐
        │                     │
     Folders              Files
        │                     │
        │                     │
        │              FileVersions
        │
        ├──────── Permissions ───────┐
        │                            │
        └──────── ActivityLogs ◄─────┘

Users
 │
 ├── RefreshTokens
 └── Notifications

 3. Database Tables

We'll have 7 core tables.

Table	          Purpose
users	          Authentication & profile
folders	          Folder hierarchy
files	          File metadata
file_versions	  Version history
permissions	      Sharing & RBAC
refresh_tokens	  Login sessions
activity_logs	  Audit trail

Later we'll add:

notifications
webhooks
storage_statistics

Table 1 — Users
users
Stores account information.

Columns
Column	        Type
id	            UUID
name	        VARCHAR
email	        VARCHAR UNIQUE
password_hash	TEXT
role	        ENUM
storage_used	BIGINT
storage_limit	BIGINT
email_verified	BOOLEAN
created_at	    TIMESTAMP
updated_at	    TIMESTAMP

Why UUID?
Instead of:

1
2
3
4

We'll use

4b18f7b4-....

Benefits:

Harder to guess
Better for distributed systems
Safer public APIs


Table 2 — Folders
Folders support nesting.

Column	            Type
id	                UUID
parent_folder_id	UUID
owner_id	        UUID
name	            VARCHAR
path	            TEXT
created_at	        TIMESTAMP

Why parent_folder_id?
Supports

Root

↓

Projects

↓

CloudNest

↓

Images

This is called an Adjacency List Model.

Table 3 — Files
Stores metadata only.

Column	Type
id	UUID
folder_id	UUID
owner_id	UUID
current_version_id	UUID
filename	VARCHAR
extension	VARCHAR
mime_type	VARCHAR
size	BIGINT
storage_key	TEXT
checksum	TEXT
created_at	TIMESTAMP
storage_key

Example

users/42/files/a83sd9f.pdf

This is the object name inside MinIO.

checksum

Stores SHA-256 hash.

Useful for

Integrity verification
Duplicate detection (future)
Table 4 — File Versions

One of the most important tables.

Every upload creates a version.

Column	Type
id	UUID
file_id	UUID
version_number	INTEGER
storage_key	TEXT
size	BIGINT
checksum	TEXT
uploaded_by	UUID
uploaded_at	TIMESTAMP

Instead of

Resume.pdf

becoming overwritten,

we get

Resume.pdf

v1

v2

v3

v4
Table 5 — Permissions

Supports sharing.

Column	Type
id	UUID
resource_type   (FILE | FOLDER)
resource_id	UUID
user_id	UUID
permission	ENUM
granted_by	UUID
created_at	TIMESTAMP

Permission values

OWNER

EDITOR

VIEWER
Table 6 — Refresh Tokens

Stores login sessions.

Column	Type
id	UUID
user_id	UUID
token	TEXT
expires_at	TIMESTAMP
revoked	BOOLEAN

Why store refresh tokens?

Allows

Logout
Token revocation
Multiple devices
Table 7 — Activity Logs

Audit trail.

Column	Type
id	UUID
user_id	UUID
action	VARCHAR
resource_type	VARCHAR
resource_id	UUID
timestamp	TIMESTAMP

Examples

UPLOAD

DELETE

DOWNLOAD

RESTORE

SHARE

LOGIN

LOGOUT


4. Relationships
User

1 ---- N Files

1 ---- N Folders

1 ---- N RefreshTokens

1 ---- N ActivityLogs

Folder

1 ---- N Files

Folder

1 ---- N Subfolders

File

1 ---- N FileVersions

File

1 ---- N Permissions


5. Indexing Strategy

Indexes improve query performance.

Users
email

Unique index.

Files
owner_id

folder_id

filename
File Versions
file_id
Permissions
user_id

file_id
Activity Logs
user_id

timestamp


6. Constraints

Examples

email UNIQUE

size > 0

storage_used >= 0

storage_limit >= storage_used


7. Soft Deletes

Instead of permanently deleting files:

DELETE

We'll mark them as:

deleted_at

deleted_by

Benefits:

Restore deleted files
Prevent accidental loss
Maintain audit history


8. Engineering Decisions
Decision	Reason
UUID primary keys	Better for distributed systems and public APIs
Separate file_versions table	Preserves complete history
Metadata only in PostgreSQL	Databases aren't optimized for binary file storage
Adjacency list for folders	Simple and efficient hierarchical model
Soft deletes	Recovery and auditability
Checksums	Detect corruption and enable future deduplication
Separate permissions table	Flexible sharing without changing file ownership

Why?

Suppose later we want to share:

Files
Folders
Team workspaces
API keys
Future resource types

With resource_type + resource_id, the permission system becomes generic and reusable. It's a pattern used in many large applications because it avoids creating separate permission tables for every resource.