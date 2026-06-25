API Design Principles

We'll follow these principles:

RESTful APIs
JSON request/response
UUID-based resource IDs
Consistent response format
Proper HTTP status codes
Pagination for collections
Validation at the API boundary
Base URL
/api/v1

Using /v1 allows future API versioning without breaking clients.

Standard Response Format

Every successful response follows the same structure.

{
  "success": true,
  "message": "File uploaded successfully",
  "data": {}
}

Errors:

{
  "success": false,
  "message": "Validation failed",
  "errors": [
    {
      "field": "email",
      "message": "Email is invalid"
    }
  ]
}

This consistency simplifies frontend handling.

Authentication APIs
Register
POST /api/v1/auth/register

Request

{
  "name": "Anushka",
  "email": "abc@gmail.com",
  "password": "Password@123"
}

Response

{
  "success": true,
  "message": "Registration successful"
}


Login
POST /api/v1/auth/login

Request

{
  "email": "abc@gmail.com",
  "password": "Password@123"
}

Response

{
  "accessToken": "...",
  "refreshToken": "...",
  "expiresIn": 900
}
Refresh Token
POST /api/v1/auth/refresh
Logout
POST /api/v1/auth/logout
User APIs
Get Profile
GET /api/v1/users/me

Returns authenticated user information.

Update Profile
PUT /api/v1/users/me
Folder APIs
Create Folder
POST /api/v1/folders

Request

{
  "name": "Projects",
  "parentFolderId": "uuid"
}
Get Folder Contents
GET /api/v1/folders/{folderId}

Returns:

child folders
files
metadata
Rename Folder
PATCH /api/v1/folders/{folderId}
Delete Folder
DELETE /api/v1/folders/{folderId}

Soft delete.

File APIs
Upload File
POST /api/v1/files

Multipart request.

Parameters

file
folderId

Response

{
  "fileId": "...",
  "filename": "resume.pdf",
  "size": 120030
}


Download File
GET /api/v1/files/{id}/download

Returns a pre-signed download URL.

Get File Details
GET /api/v1/files/{id}
Rename File
PATCH /api/v1/files/{id}
Delete File
DELETE /api/v1/files/{id}

Soft delete.

Versioning APIs
List Versions
GET /api/v1/files/{id}/versions
Restore Version
POST /api/v1/files/{id}/versions/{versionId}/restore
Sharing APIs
Share Resource
POST /api/v1/shares

Request

{
  "resourceType": "FILE",
  "resourceId": "uuid",
  "userId": "uuid",
  "permission": "VIEWER"
}
Remove Share
DELETE /api/v1/shares/{shareId}

List Shared Resources
GET /api/v1/shares

Search APIs
GET /api/v1/search?query=resume

Future filters:

extension
owner
date
size
Dashboard APIs
GET /api/v1/dashboard

Returns:

storage used
recent files
shared files
activity summary
Activity APIs
GET /api/v1/activity

Paginated audit history.

Notification APIs
GET /api/v1/notifications
PATCH /api/v1/notifications/{id}/read

Authentication Requirements
Endpoint	Authentication
Register	❌
Login	❌
Refresh	❌
Logout	✅
Files	✅
Folders	✅
Dashboard	✅
Sharing	✅

Validation Rules

Examples:

Register
Email must be unique.
Password minimum 8 characters.
Password must include uppercase, lowercase, number, and special character.
Upload
Maximum file size (configurable).
Allowed MIME types.
Virus scan triggered after upload.
Folder
Folder name cannot be empty.
Duplicate names within the same parent are not allowed.
API Versioning

All APIs are prefixed with:

/api/v1

If a breaking change is introduced later, we'll expose:

/api/v2

without affecting existing clients.

Engineering Decisions
Decision	Reason
/api/v1 prefix	Supports future API evolution
PATCH for rename/update	Partial updates are more appropriate than replacing the whole resource
Soft deletes	Enables recovery and audit trails
Multipart upload	Standard approach for binary file transfer
UUID resource IDs	Prevents predictable identifiers and scales well
Consistent response envelope	Simplifies frontend integration and error handling