# 📡 Cloud Sync API Documentation

Complete REST API reference for the Cloud Sync Application backend server.

## 📋 Table of Contents

- [Overview](#overview)
- [Base URL](#base-url)
- [Authentication](#authentication)
- [Authentication Endpoints](#authentication-endpoints)
- [Sync Endpoints](#sync-endpoints)
- [Sync Configuration Endpoints](#sync-configuration-endpoints)
- [Error Handling](#error-responses)
- [Code Examples](#mobile-app-integration-example)
- [OAuth Setup](#setup-instructions-for-oauth)

## Overview

The Cloud Sync API provides a RESTful interface for mobile applications to:
- Authenticate users via Google and Microsoft OAuth 2.0
- Upload and download files to cloud storage (Google Drive, OneDrive)
- Manage folder synchronization configurations
- Browse cloud storage folders

### API Characteristics
- **Protocol**: HTTP/HTTPS
- **Format**: JSON
- **Authentication**: Session-based with OAuth 2.0
- **CORS**: Enabled for mobile app integration

## Base URL

**Development:**
```
http://localhost:3000
```

**Production:**
```
https://your-domain.com
```

### Endpoint Structure
```
[BASE_URL]/[route]/[resource]
```

**Examples:**
- `http://localhost:3000/auth/status`
- `http://localhost:3000/api/sync/list`
- `http://localhost:3000/api/sync-config`

## Authentication

### Session-Based Authentication

The API uses **session-based authentication** with secure cookies:

1. User initiates OAuth flow (`/auth/google` or `/auth/microsoft`)
2. After successful authentication, server creates a session
3. Session cookie (`connect.sid`) is set in response
4. Subsequent requests must include this cookie

### Including Session Cookie

**JavaScript/Fetch:**
```javascript
fetch('http://localhost:3000/api/sync/list', {
  credentials: 'include'  // Important!
})
```

**cURL:**
```bash
curl http://localhost:3000/api/sync/list \
  --cookie "connect.sid=your_session_cookie"
```

### Authentication Flow

```
Mobile App → /auth/google → Google Login → Callback → Session Created
          ↓
     Session Cookie Stored
          ↓
     Future API Requests (with cookie)
```

---

## Authentication Endpoints

### 1. Initiate Google OAuth

```http
GET /auth/google
```

**Description:** Initiates Google OAuth 2.0 flow. Redirects user to Google consent screen.

**Authentication Required:** No

**Parameters:** None

**Response:** HTTP 302 Redirect to Google OAuth page

**Usage:**
```javascript
// Open in browser or WebView
window.location.href = 'http://localhost:3000/auth/google';
```

---

### 2. Google OAuth Callback

```http
GET /auth/google/callback
```

**Description:** OAuth callback endpoint. Called automatically by Google after user authorization.

**Authentication Required:** No

**Query Parameters:**
- `code` (auto): Authorization code from Google
- `state` (auto): State parameter for CSRF protection

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Successfully authenticated with Google",
  "user": {
    "id": "12345678901234567890",
    "provider": "google",
    "displayName": "John Doe",
    "email": "john.doe@example.com",
    "picture": "https://lh3.googleusercontent.com/..."
  }
}
```

**Error Response (401 Unauthorized):**
```json
{
  "error": {
    "message": "Authentication failed",
    "status": 401
  }
}
```

---

### 3. Initiate Microsoft OAuth

```http
GET /auth/microsoft
```

**Description:** Initiates Microsoft OAuth 2.0 flow. Redirects user to Microsoft login page.

**Authentication Required:** No

**Parameters:** None

**Response:** HTTP 302 Redirect to Microsoft OAuth page

**Usage:**
```javascript
window.location.href = 'http://localhost:3000/auth/microsoft';
```

---

### 4. Microsoft OAuth Callback

```http
GET /auth/microsoft/callback
```

**Description:** OAuth callback endpoint. Called automatically by Microsoft after user authorization.

**Authentication Required:** No

**Query Parameters:**
- `code` (auto): Authorization code from Microsoft
- `state` (auto): State parameter for CSRF protection

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Successfully authenticated with Microsoft",
  "user": {
    "id": "12345678-1234-1234-1234-123456789012",
    "provider": "microsoft",
    "displayName": "Jane Doe",
    "email": "jane.doe@outlook.com"
  }
}
```

**Error Response (401 Unauthorized):**
```json
{
  "error": {
    "message": "Authentication failed",
    "status": 401
  }
}
```

---

### 5. Check Authentication Status

```http
GET /auth/status
```

**Description:** Checks if the current session is authenticated and returns user information.

**Authentication Required:** No

**Parameters:** None

**Success Response - Authenticated (200 OK):**
```json
{
  "authenticated": true,
  "user": {
    "id": "12345678901234567890",
    "provider": "google",
    "displayName": "John Doe",
    "email": "john.doe@example.com"
  }
}
```

**Success Response - Not Authenticated (200 OK):**
```json
{
  "authenticated": false
}
```

**Example Usage:**
```javascript
const checkAuth = async () => {
  const response = await fetch('http://localhost:3000/auth/status', {
    credentials: 'include'
  });
  const data = await response.json();
  
  if (data.authenticated) {
    console.log('User:', data.user.displayName);
  } else {
    console.log('Not authenticated');
  }
};
```

---

### 6. Logout

```http
GET /auth/logout
```

**Description:** Logs out the current user and destroys the session.

**Authentication Required:** Yes

**Parameters:** None

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Successfully logged out"
}
```

**Example Usage:**
```javascript
const logout = async () => {
  const response = await fetch('http://localhost:3000/auth/logout', {
    credentials: 'include'
  });
  const data = await response.json();
  console.log(data.message);
  // Redirect to login page
};
```

---

---

## Sync Endpoints

### 1. Upload File

```http
POST /api/sync/upload
```

**Description:** Uploads a file to the authenticated user's cloud storage (Google Drive or OneDrive).

**Authentication Required:** ✅ Yes

**Content-Type:** `application/json`

**Request Body:**
```json
{
  "filename": "app_data.json",
  "content": "{\"key\": \"value\"}"
}
```

**Request Body Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `filename` | string | Yes | Name of the file to upload |
| `content` | string | Yes | File content (can be text or JSON string) |

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "File uploaded successfully",
  "provider": "google",
  "file": {
    "id": "1a2b3c4d5e6f7g8h9i0j",
    "name": "app_data.json",
    "mimeType": "application/json",
    "createdTime": "2024-01-15T12:34:56Z",
    "modifiedTime": "2024-01-15T12:34:56Z",
    "size": "1024"
  }
}
```

**Error Responses:**

**401 Unauthorized:**
```json
{
  "error": {
    "message": "Not authenticated",
    "status": 401
  }
}
```

**400 Bad Request:**
```json
{
  "error": {
    "message": "Filename and content are required",
    "status": 400
  }
}
```

**Example Usage:**
```javascript
const uploadFile = async (filename, content) => {
  const response = await fetch('http://localhost:3000/api/sync/upload', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    credentials: 'include',
    body: JSON.stringify({ filename, content })
  });
  
  const result = await response.json();
  if (result.success) {
    console.log('File uploaded:', result.file.id);
  }
  return result;
};
```

---

### 2. Download File
**Endpoint:** `GET /api/sync/download`

**Description:** Download a file from cloud storage by file ID or filename.

**Authentication Required:** Yes

**Query Parameters:**
- `fileId` (optional): The ID of the file to download
- `filename` (optional): The name of the file to download

**Example:** `/api/sync/download?filename=app_data.json`

**Response:**
```json
{
  "success": true,
  "provider": "google",
  "content": "{\"key\": \"value\"}"
}
```

---

### 3. List Files
**Endpoint:** `GET /api/sync/list`

**Description:** List all files in the user's cloud storage.

**Authentication Required:** Yes

**Response:**
```json
{
  "success": true,
  "provider": "google",
  "files": [
    {
      "id": "file_id_1",
      "name": "app_data.json",
      "mimeType": "application/json",
      "createdTime": "2023-10-06T12:00:00Z",
      "modifiedTime": "2023-10-06T12:00:00Z",
      "size": "1024"
    },
    {
      "id": "file_id_2",
      "name": "backup.json",
      "mimeType": "application/json",
      "createdTime": "2023-10-05T10:00:00Z",
      "modifiedTime": "2023-10-05T10:00:00Z",
      "size": "2048"
    }
  ]
}
```

---

### 4. Delete File
**Endpoint:** `DELETE /api/sync/delete/:fileId`

**Description:** Delete a file from cloud storage.

**Authentication Required:** Yes

**URL Parameters:**
- `fileId`: The ID of the file to delete

**Example:** `/api/sync/delete/file_id_123`

**Response:**
```json
{
  "success": true,
  "provider": "google",
  "message": "File deleted successfully"
}
```

---

## Error Responses

All endpoints may return error responses in the following format:

```json
{
  "error": {
    "message": "Error description",
    "status": 400
  }
}
```

### Common Error Codes:
- `400`: Bad Request - Invalid parameters
- `401`: Unauthorized - Authentication required
- `404`: Not Found - Resource not found
- `500`: Internal Server Error - Server-side error

---

## Sync Configuration Endpoints

### 1. Get All Sync Configurations
**Endpoint:** `GET /api/sync-config`

**Description:** Get all sync configurations for the authenticated user.

**Authentication Required:** Yes

**Response:**
```json
{
  "success": true,
  "configs": [
    {
      "id": "sync_123456",
      "userId": "user_id",
      "localFolderPath": "/storage/emulated/0/Documents",
      "cloudFolderPath": "Documents",
      "provider": "google",
      "syncMode": "two_way",
      "deleteDelayDays": 7,
      "enabled": true,
      "lastSyncTime": "2023-10-06T12:00:00Z",
      "createdAt": "2023-10-01T10:00:00Z",
      "updatedAt": "2023-10-06T12:00:00Z"
    }
  ]
}
```

---

### 2. Create Sync Configuration
**Endpoint:** `POST /api/sync-config`

**Description:** Create a new sync configuration.

**Authentication Required:** Yes

**Request Body:**
```json
{
  "localFolderPath": "/storage/emulated/0/Documents",
  "cloudFolderPath": "Documents",
  "provider": "google",
  "syncMode": "two_way",
  "deleteDelayDays": 7,
  "enabled": true
}
```

**Sync Modes:**
- `upload_only`: Upload files from local to cloud only
- `upload_then_delete`: Upload files and delete from local after specified delay
- `download_only`: Download files from cloud to local only
- `download_then_delete`: Download files and delete from cloud after specified delay
- `two_way`: Synchronize files in both directions

**Response:**
```json
{
  "success": true,
  "message": "Sync configuration created successfully",
  "config": {
    "id": "sync_123456",
    "userId": "user_id",
    "localFolderPath": "/storage/emulated/0/Documents",
    "cloudFolderPath": "Documents",
    "provider": "google",
    "syncMode": "two_way",
    "deleteDelayDays": 7,
    "enabled": true,
    "lastSyncTime": null,
    "createdAt": "2023-10-06T12:00:00Z",
    "updatedAt": "2023-10-06T12:00:00Z"
  }
}
```

---

### 3. Update Sync Configuration
**Endpoint:** `PUT /api/sync-config/:configId`

**Description:** Update an existing sync configuration.

**Authentication Required:** Yes

**Request Body:** (all fields optional)
```json
{
  "localFolderPath": "/storage/emulated/0/NewFolder",
  "cloudFolderPath": "NewFolder",
  "syncMode": "upload_only",
  "deleteDelayDays": 14,
  "enabled": false
}
```

**Response:**
```json
{
  "success": true,
  "message": "Sync configuration updated successfully",
  "config": {
    "id": "sync_123456",
    "userId": "user_id",
    "localFolderPath": "/storage/emulated/0/NewFolder",
    "cloudFolderPath": "NewFolder",
    "provider": "google",
    "syncMode": "upload_only",
    "deleteDelayDays": 14,
    "enabled": false,
    "lastSyncTime": "2023-10-06T12:00:00Z",
    "createdAt": "2023-10-01T10:00:00Z",
    "updatedAt": "2023-10-06T13:00:00Z"
  }
}
```

---

### 4. Delete Sync Configuration
**Endpoint:** `DELETE /api/sync-config/:configId`

**Description:** Delete a sync configuration.

**Authentication Required:** Yes

**Response:**
```json
{
  "success": true,
  "message": "Sync configuration deleted successfully"
}
```

---

### 5. Execute Sync
**Endpoint:** `POST /api/sync/execute/:configId`

**Description:** Execute sync based on the configuration.

**Authentication Required:** Yes

**Request Body:** (for upload modes)
```json
{
  "files": [
    {
      "filename": "document1.txt",
      "content": "File content here"
    },
    {
      "filename": "document2.txt",
      "content": "Another file content"
    }
  ]
}
```

**Response:**
```json
{
  "success": true,
  "message": "Sync executed successfully",
  "syncMode": "two_way",
  "results": {
    "uploaded": [
      {
        "id": "file_id_1",
        "name": "document1.txt",
        "size": "1024"
      }
    ],
    "downloaded": [
      {
        "id": "file_id_2",
        "name": "remote_file.txt",
        "content": "Remote file content"
      }
    ],
    "deleted": [],
    "errors": []
  }
}
```

---

### 6. Upload File to Folder
**Endpoint:** `POST /api/sync/folder/upload`

**Description:** Upload a file to a specific cloud folder.

**Authentication Required:** Yes

**Request Body:**
```json
{
  "folderPath": "Documents",
  "filename": "my_file.txt",
  "content": "File content here"
}
```

**Response:**
```json
{
  "success": true,
  "message": "File uploaded to folder successfully",
  "provider": "google",
  "file": {
    "id": "file_id",
    "name": "my_file.txt",
    "size": "1024"
  }
}
```

---

### 7. List Files in Folder
**Endpoint:** `GET /api/sync/folder/list?folderPath=Documents`

**Description:** List all files in a specific cloud folder.

**Authentication Required:** Yes

**Query Parameters:**
- `folderPath`: The path to the folder

**Response:**
```json
{
  "success": true,
  "provider": "google",
  "files": [
    {
      "id": "file_id_1",
      "name": "document1.txt",
      "size": "1024"
    },
    {
      "id": "file_id_2",
      "name": "document2.txt",
      "size": "2048"
    }
  ]
}
```

---

## Mobile App Integration Example

### JavaScript/React Native Example

```javascript
// 1. Authenticate user (open in WebView or browser)
const authUrl = 'http://your-server.com/auth/google';
// User completes OAuth flow in browser/webview

// 2. Check authentication status
const checkAuth = async () => {
  const response = await fetch('http://your-server.com/auth/status', {
    credentials: 'include' // Important for session cookies
  });
  const data = await response.json();
  return data.authenticated;
};

// 3. Upload data
const uploadData = async (appData) => {
  const response = await fetch('http://your-server.com/api/sync/upload', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    credentials: 'include',
    body: JSON.stringify({
      filename: 'my_app_data.json',
      content: JSON.stringify(appData)
    })
  });
  return await response.json();
};

// 4. Download data
const downloadData = async () => {
  const response = await fetch(
    'http://your-server.com/api/sync/download?filename=my_app_data.json',
    { credentials: 'include' }
  );
  const data = await response.json();
  return JSON.parse(data.content);
};

// 5. List all files
const listFiles = async () => {
  const response = await fetch('http://your-server.com/api/sync/list', {
    credentials: 'include'
  });
  const data = await response.json();
  return data.files;
};

// 6. Delete a file
const deleteFile = async (fileId) => {
  const response = await fetch(`http://your-server.com/api/sync/delete/${fileId}`, {
    method: 'DELETE',
    credentials: 'include'
  });
  return await response.json();
};
```

---

## Setup Instructions for OAuth

### Google Cloud Console Setup

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Enable the Google Drive API
4. Go to "Credentials" → "Create Credentials" → "OAuth 2.0 Client ID"
5. Configure the OAuth consent screen
6. Add authorized redirect URI: `http://localhost:3000/auth/google/callback`
7. Copy the Client ID and Client Secret to your `.env` file

### Microsoft Azure Portal Setup

1. Go to [Azure Portal](https://portal.azure.com/)
2. Navigate to "Azure Active Directory" → "App registrations"
3. Click "New registration"
4. Add redirect URI: `http://localhost:3000/auth/microsoft/callback`
5. Go to "Certificates & secrets" → Create a new client secret
6. Go to "API permissions" → Add Microsoft Graph permissions (User.Read, Files.ReadWrite)
7. Copy the Application (client) ID and Client Secret to your `.env` file
