# Folder Sync Feature Documentation

## Overview

The Folder Sync feature enables automatic synchronization between local device folders and cloud storage (Google Drive or OneDrive) with multiple configurable sync modes and deletion delays.

## Features

### Sync Modes

The application supports five distinct sync modes:

#### 1. Upload Only
- **Description:** Upload files from local folder to cloud folder only
- **Use Case:** Backing up local files to cloud without downloading anything
- **Behavior:** New files in local folder are uploaded to cloud
- **Deletion:** No automatic deletion

#### 2. Upload then Delete
- **Description:** Upload files and delete them from local storage after a configurable delay
- **Use Case:** Archiving files to cloud and freeing up local storage
- **Behavior:** 
  - Files are uploaded to cloud
  - After the specified delay (0-30 days), files are deleted from local storage
- **Deletion:** Configurable delay (0-30 days)

#### 3. Download Only
- **Description:** Download files from cloud folder to local folder only
- **Use Case:** Syncing cloud files to local device
- **Behavior:** New files in cloud folder are downloaded to local storage
- **Deletion:** No automatic deletion

#### 4. Download then Delete
- **Description:** Download files and delete them from cloud after a configurable delay
- **Use Case:** Moving files from cloud to local storage
- **Behavior:**
  - Files are downloaded from cloud to local
  - After the specified delay (0-30 days), files are deleted from cloud
- **Deletion:** Configurable delay (0-30 days)

#### 5. Two-Way Sync
- **Description:** Bidirectional synchronization between local and cloud
- **Use Case:** Keeping folders in sync across devices
- **Behavior:**
  - New files in local folder are uploaded to cloud
  - New files in cloud folder are downloaded to local
  - Changes are synchronized in both directions
- **Deletion:** Deletions are mirrored in both locations

### Delete Delay Configuration

For sync modes that support automatic deletion (Upload then Delete, Download then Delete):

- **Range:** 0-30 days
- **Immediate (0 days):** Files are deleted immediately after successful upload/download
- **Delayed (1-30 days):** Files are kept for the specified number of days before deletion
- **Use Case Examples:**
  - 0 days: Move files immediately
  - 7 days: Weekly archive with grace period
  - 30 days: Monthly backup with extended retention

## Backend API

### Sync Configuration Endpoints

#### Create Configuration
```http
POST /api/sync-config
Content-Type: application/json

{
  "localFolderPath": "/storage/emulated/0/Documents",
  "cloudFolderPath": "Documents",
  "provider": "google",
  "syncMode": "two_way",
  "deleteDelayDays": 7,
  "enabled": true
}
```

#### List Configurations
```http
GET /api/sync-config
```

#### Update Configuration
```http
PUT /api/sync-config/{configId}
Content-Type: application/json

{
  "syncMode": "upload_only",
  "deleteDelayDays": 14,
  "enabled": false
}
```

#### Delete Configuration
```http
DELETE /api/sync-config/{configId}
```

#### Execute Sync
```http
POST /api/sync/execute/{configId}
Content-Type: application/json

{
  "files": [
    {
      "filename": "document.txt",
      "content": "File content"
    }
  ]
}
```

### Folder Operations

#### Upload to Folder
```http
POST /api/sync/folder/upload
Content-Type: application/json

{
  "folderPath": "Documents",
  "filename": "my_file.txt",
  "content": "File content"
}
```

#### List Folder Contents
```http
GET /api/sync/folder/list?folderPath=Documents
```

## Android Frontend

### User Interface

#### Main Activity
- **Manage Folder Sync** button opens the Sync Configuration List
- Shows account connection status (Google Drive / OneDrive)

#### Sync Configuration List Activity
- Displays all configured sync folders
- Shows:
  - Cloud provider icon (Google Drive / OneDrive)
  - Sync mode
  - Local folder path
  - Cloud folder path
  - Status (Active / Disabled)
- Actions:
  - Tap to view details or edit
  - Long press to delete
  - FAB button to add new configuration

#### Folder Sync Configuration Activity
- **Folder Configuration Section:**
  - Local folder path input
  - Browse folder button (for future SAF integration)
  - Cloud folder path input
  
- **Sync Settings Section:**
  - Cloud provider dropdown (Google Drive / OneDrive)
  - Sync mode dropdown (5 modes)
  - Delete delay slider (0-30 days)
    - Only visible for modes that support deletion
    - Shows "Immediate" for 0 days
    - Shows "X day(s)" for other values

### Material Design Elements

- **Cards:** Used for grouping related settings
- **Dropdowns:** Exposed dropdown menus for provider and sync mode
- **Slider:** Material slider for delete delay with visual feedback
- **FAB:** Floating action button for adding configurations
- **Snackbars:** For user feedback and notifications
- **Icons:** Material icons for visual clarity

## Usage Examples

### Example 1: Photo Backup
**Scenario:** Automatically backup photos to Google Drive and keep local copies

**Configuration:**
- Local Folder: `/storage/emulated/0/DCIM/Camera`
- Cloud Folder: `Photos/Backup`
- Provider: Google Drive
- Sync Mode: Upload Only
- Delete Delay: N/A

### Example 2: Archive Old Files
**Scenario:** Move documents to OneDrive and delete from device after 7 days

**Configuration:**
- Local Folder: `/storage/emulated/0/Documents/Archive`
- Cloud Folder: `Archive`
- Provider: OneDrive
- Sync Mode: Upload then Delete
- Delete Delay: 7 days

### Example 3: Two-Way Sync
**Scenario:** Keep work documents synchronized between phone and cloud

**Configuration:**
- Local Folder: `/storage/emulated/0/Documents/Work`
- Cloud Folder: `Work`
- Provider: Google Drive
- Sync Mode: Two-Way Sync
- Delete Delay: N/A

### Example 4: Download Reports
**Scenario:** Download monthly reports from cloud and remove from cloud after 14 days

**Configuration:**
- Local Folder: `/storage/emulated/0/Documents/Reports`
- Cloud Folder: `Reports/Monthly`
- Provider: OneDrive
- Sync Mode: Download then Delete
- Delete Delay: 14 days

## Technical Architecture

### Backend
- **Storage:** In-memory storage (can be replaced with database)
- **Services:** 
  - GoogleDriveService: Handles Google Drive API operations
  - OneDriveService: Handles Microsoft Graph API operations
- **Models:** SyncConfig with all configuration properties
- **Routes:** RESTful API endpoints for CRUD operations

### Frontend
- **Activities:**
  - MainActivity: Entry point and account management
  - SyncConfigListActivity: List and manage configurations
  - FolderSyncConfigActivity: Create/edit configurations
- **Models:** SyncMode enum and SyncConfig data class
- **API Client:** Retrofit-based REST API client
- **Adapters:** RecyclerView adapter for configuration list

## Future Enhancements

### Planned Features
1. **Scheduled Sync:** Configure sync to run at specific times
2. **Conflict Resolution:** Handle file conflicts in two-way sync
3. **Selective Sync:** Choose specific file types or patterns to sync
4. **Bandwidth Control:** Limit upload/download speeds
5. **Background Service:** Automatic sync in the background
6. **Storage Access Framework:** Native folder picker for Android
7. **Notification System:** Status updates and sync completion alerts
8. **Sync History:** View past sync operations and their results
9. **File Versioning:** Keep multiple versions of synced files
10. **Encryption:** End-to-end encryption for sensitive files

### Deletion Scheduling
Currently, the delete delay is configured but not actively scheduled. Future implementation will include:
- Background job scheduler
- Persistent tracking of files pending deletion
- Database storage for deletion schedules
- Retry mechanism for failed deletions

## Security Considerations

- **Authentication:** OAuth 2.0 for both Google Drive and OneDrive
- **Token Storage:** Secure storage using Android Keystore
- **Session Management:** Server-side session management
- **Data Privacy:** Files are transmitted directly between device and cloud
- **No Intermediate Storage:** Backend doesn't store file contents

## Testing Recommendations

### Unit Tests
- Sync mode logic
- Configuration validation
- API request/response handling

### Integration Tests
- End-to-end sync operations
- OAuth authentication flow
- File upload/download operations

### UI Tests
- Configuration creation flow
- UI state updates
- Error handling

### Manual Testing Checklist
- [ ] Create configuration for each sync mode
- [ ] Test with both Google Drive and OneDrive
- [ ] Verify delete delay slider behavior
- [ ] Test configuration enable/disable
- [ ] Verify sync execution with sample files
- [ ] Test error scenarios (no network, invalid paths)
- [ ] Verify UI responsiveness and feedback

## Troubleshooting

### Common Issues

**Issue:** Configuration not saving
- **Solution:** Check network connection and authentication status

**Issue:** Files not syncing
- **Solution:** Verify folder paths are correct and accessible

**Issue:** Delete delay not working
- **Solution:** This feature requires backend scheduler implementation (future enhancement)

**Issue:** Two-way sync conflicts
- **Solution:** Currently uses last-write-wins; conflict resolution is a planned feature

## Support

For issues or questions:
1. Check API_DOCUMENTATION.md for endpoint details
2. Review QUICKSTART.md for setup instructions
3. Consult CONTRIBUTING.md for development guidelines
