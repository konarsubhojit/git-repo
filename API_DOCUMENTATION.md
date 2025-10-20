# API Documentation

## Base URL
```
http://localhost:3000
```

## Authentication Endpoints

### 1. Initiate Google OAuth
**Endpoint:** `GET /auth/google`

**Description:** Redirects user to Google OAuth consent screen.

**Response:** Redirects to Google OAuth page

---

### 2. Google OAuth Callback
**Endpoint:** `GET /auth/google/callback`

**Description:** Callback URL for Google OAuth. Called automatically by Google after user grants permission.

**Response:**
```json
{
  "success": true,
  "message": "Successfully authenticated with Google",
  "user": {
    "id": "user_id",
    "provider": "google",
    "displayName": "John Doe",
    "email": "john@example.com"
  }
}
```

---

### 3. Initiate Microsoft OAuth
**Endpoint:** `GET /auth/microsoft`

**Description:** Redirects user to Microsoft OAuth consent screen.

**Response:** Redirects to Microsoft OAuth page

---

### 4. Microsoft OAuth Callback
**Endpoint:** `GET /auth/microsoft/callback`

**Description:** Callback URL for Microsoft OAuth. Called automatically by Microsoft after user grants permission.

**Response:**
```json
{
  "success": true,
  "message": "Successfully authenticated with Microsoft",
  "user": {
    "id": "user_id",
    "provider": "microsoft",
    "displayName": "Jane Doe",
    "email": "jane@example.com"
  }
}
```

---

### 5. Check Authentication Status
**Endpoint:** `GET /auth/status`

**Description:** Check if user is currently authenticated.

**Response (Authenticated):**
```json
{
  "authenticated": true,
  "user": {
    "id": "user_id",
    "provider": "google",
    "displayName": "John Doe",
    "email": "john@example.com"
  }
}
```

**Response (Not Authenticated):**
```json
{
  "authenticated": false
}
```

---

### 6. Logout
**Endpoint:** `GET /auth/logout`

**Description:** Logout current user and clear session.

**Response:**
```json
{
  "success": true,
  "message": "Successfully logged out"
}
```

---

## Sync Endpoints

### 1. Upload File
**Endpoint:** `POST /api/sync/upload`

**Description:** Upload a file to the authenticated user's cloud storage (Google Drive or OneDrive).

**Authentication Required:** Yes

**Request Body:**
```json
{
  "filename": "app_data.json",
  "content": "{\"key\": \"value\"}"
}
```

**Response:**
```json
{
  "success": true,
  "message": "File uploaded successfully",
  "provider": "google",
  "file": {
    "id": "file_id",
    "name": "app_data.json",
    "mimeType": "application/json",
    "createdTime": "2023-10-06T12:00:00Z",
    "modifiedTime": "2023-10-06T12:00:00Z",
    "size": "1024"
  }
}
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
