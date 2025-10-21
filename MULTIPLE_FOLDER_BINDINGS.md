# Multiple Folder Bindings Feature

## Overview

The Cloud Sync application now supports managing multiple folder pair bindings with cloud storage. Users can configure up to **10 folder synchronization pairs**, each with its own local folder, cloud folder, and sync mode settings.

## Key Features

### 1. Multiple Sync Configurations (Max 10)
- Users can create up to 10 folder synchronization configurations
- Each configuration is independent with its own settings
- Visual counter shows used/available slots (e.g., "Configurations: 5 / 10")
- Validation prevents adding more than 10 configurations

### 2. Local Folder Picker
- Browse local device storage to select folders
- Navigate through folder hierarchy
- Shows parent folder option for easy navigation
- Real-time path display
- Easy folder selection with "Select This Folder" button

### 3. Cloud Folder Picker
- Browse cloud storage (Google Drive or OneDrive) folders
- Dynamic loading from cloud provider
- Provider-specific folder browsing
- Hierarchical navigation with breadcrumb path
- Real-time API integration

### 4. Sync Configuration Options

Each folder pair can be configured with:
- **Local Folder Path**: Path on device storage
- **Cloud Folder Path**: Path in cloud storage
- **Cloud Provider**: Google Drive or OneDrive
- **Sync Mode**: 
  - Upload Only
  - Upload then Delete
  - Download Only
  - Download then Delete
  - Two-Way Sync
- **Delete Delay**: 0-30 days (for modes with deletion)

## User Interface

### Sync Configuration List
- **Location**: Accessible from Main Activity → "Manage Folder Sync" button
- **Features**:
  - List view of all sync configurations
  - Configuration counter (e.g., "Configurations: 3 / 10")
  - Provider icons (Google Drive/OneDrive)
  - Sync mode display
  - Active/Disabled status indicators
  - Quick actions: View details, Edit, Toggle enable/disable, Delete
  - FAB button to add new configurations (disabled when limit reached)

### Folder Sync Configuration Activity
- **Create/Edit** sync configurations
- **Folder Selection**:
  - "Browse Local Folder" button → Opens LocalFolderPickerActivity
  - "Browse Cloud Folder" button → Opens CloudFolderPickerActivity
  - Manual text input also supported
- **Sync Settings**:
  - Cloud provider dropdown
  - Sync mode dropdown with descriptions
  - Delete delay slider (conditional visibility)
- **Validation**:
  - Required field checks
  - Maximum configuration limit enforcement
  - User-friendly error messages

### Local Folder Picker Activity
- **Features**:
  - Browse device storage folders
  - Navigate into subfolders by tapping
  - Navigate to parent folder via ".." option
  - Current path display at top
  - Select/Cancel buttons
  - Back button navigation support

### Cloud Folder Picker Activity
- **Features**:
  - Browse Google Drive or OneDrive folders
  - Dynamic loading with progress indicator
  - Navigate into subfolders
  - Navigate to parent folder
  - Current path display
  - Provider-specific title
  - Select/Cancel buttons
  - Error handling for network issues

## Backend Implementation

### New API Endpoints

#### List Cloud Folders
```http
GET /api/sync/folders/list?folderPath=xxx&provider=google|microsoft
```

**Query Parameters:**
- `folderPath` (optional): Path to list subfolders of (empty for root)
- `provider` (required): "google" or "microsoft"

**Response:**
```json
{
  "success": true,
  "provider": "google",
  "currentPath": "/Documents",
  "folders": [
    {
      "id": "folder-id-123",
      "name": "Subfolder 1"
    },
    {
      "id": "folder-id-456",
      "name": "Subfolder 2"
    }
  ]
}
```

### Enhanced Sync Configuration Endpoints

#### Create Sync Configuration
- **Validation**: Maximum 10 configurations per user
- **Error Response** when limit reached:
```json
{
  "error": {
    "message": "Maximum number of sync configurations (10) reached. Please delete an existing configuration to add a new one.",
    "status": 400
  }
}
```

### Backend Service Methods

#### GoogleDriveService
- `listFolders(parentFolderId, pageSize)`: List folders in a specific location
- `listFoldersInPath(folderPath)`: List folders by path

#### OneDriveService
- `listFolders(parentFolderId)`: List folders in a specific location
- `listFoldersInPath(folderPath)`: List folders by path

## Android Implementation

### New Classes

#### Activities
1. **LocalFolderPickerActivity**: Browse local device folders
2. **CloudFolderPickerActivity**: Browse cloud storage folders

#### Adapters
1. **FolderAdapter**: RecyclerView adapter for local folders
2. **CloudFolderAdapter**: RecyclerView adapter for cloud folders

#### Models
1. **CloudFolder**: Model for cloud folder representation

#### Utilities
1. **SyncConfigManager**: Manages sync configuration persistence and validation
   - Stores configurations in SharedPreferences
   - Enforces 10-config maximum
   - Provides CRUD operations
   - Uses Gson for JSON serialization

### Permissions

Added Android permissions:
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

### Data Persistence

Sync configurations are stored locally using:
- **SharedPreferences** for lightweight storage
- **Gson** for JSON serialization/deserialization
- Automatic ID generation for each configuration
- Persistent across app restarts

## Usage Flow

### Adding a New Sync Configuration

1. Open app and navigate to Main Activity
2. Tap "Manage Folder Sync" button
3. Review current configurations (shows count: X / 10)
4. Tap FAB (+) button to add new configuration
5. **Select Local Folder**:
   - Tap "Browse Local Folder" button
   - Navigate through device folders
   - Tap desired folder
   - Tap "Select This Folder"
6. **Select Cloud Folder**:
   - First select cloud provider (Google Drive/OneDrive)
   - Tap "Browse Cloud Folder" button
   - Navigate through cloud folders
   - Tap desired folder
   - Tap "Select This Folder"
7. **Configure Sync Settings**:
   - Choose sync mode from dropdown
   - Set delete delay if applicable
8. Tap "Save Configuration"
9. Configuration is added to list and persisted

### Managing Configurations

- **View Details**: Tap on configuration to see options, then select "View Details"
- **Edit**: Currently returns placeholder (future enhancement)
- **Toggle Enable/Disable**: Change active status without deleting
- **Delete**: Long press or select "Delete" from options menu
- **Add More**: Continue adding until 10 limit is reached

### Limit Enforcement

When 10 configurations exist:
- FAB button shows warning snackbar: "Maximum number of sync configurations (10) reached..."
- Cannot add new configurations
- Must delete existing configuration to add new one
- Visual counter shows "10 / 10"

## Technical Details

### Local Storage Structure

Configurations are stored as JSON array in SharedPreferences:
```json
[
  {
    "id": "config_1234567890_123",
    "localFolderPath": "/storage/emulated/0/Documents",
    "cloudFolderPath": "Documents/Work",
    "provider": "google",
    "syncMode": "two_way",
    "deleteDelayDays": 0,
    "enabled": true,
    "lastSyncTime": null,
    "createdAt": "2024-01-01T12:00:00Z",
    "updatedAt": "2024-01-01T12:00:00Z"
  }
]
```

### Validation Rules

1. **Maximum Configurations**: 10 per user
2. **Required Fields**:
   - Local folder path
   - Cloud folder path
   - Cloud provider
   - Sync mode
3. **Field Constraints**:
   - Delete delay: 0-30 days
   - Provider: "google" or "microsoft"
   - Sync mode: Valid enum value

### Error Handling

- **Network Errors**: Display user-friendly message with retry option
- **Permission Errors**: Request appropriate permissions for local storage access
- **Validation Errors**: Show inline error messages with field focus
- **API Errors**: Parse and display backend error messages

## Future Enhancements

### Planned Features
1. **Edit Configuration**: Modify existing configurations
2. **Configuration Import/Export**: Backup and restore configurations
3. **Batch Operations**: Enable/disable multiple configurations
4. **Folder Size Display**: Show storage usage for each folder pair
5. **Sync History**: View past sync operations per configuration
6. **Conflict Resolution**: Handle file conflicts in two-way sync
7. **Scheduled Sync**: Configure automatic sync times
8. **Storage Access Framework**: Native Android folder picker
9. **Notification Preferences**: Customize notifications per configuration
10. **Advanced Filters**: Filter by provider, status, or sync mode

### Known Limitations
1. Local folder picker uses basic File API (pre-SAF)
2. Edit functionality shows placeholder
3. Delete delay is configured but not actively scheduled
4. No conflict resolution for two-way sync
5. No sync progress tracking per configuration

## Testing Recommendations

### Manual Testing Checklist
- [ ] Add sync configuration with local folder picker
- [ ] Add sync configuration with cloud folder picker
- [ ] Add configurations up to limit (10)
- [ ] Verify limit enforcement (cannot add 11th)
- [ ] Delete configuration and verify count updates
- [ ] Toggle configuration enable/disable status
- [ ] Navigate local folder hierarchy (parent/child)
- [ ] Navigate cloud folder hierarchy (parent/child)
- [ ] Test with Google Drive provider
- [ ] Test with OneDrive provider
- [ ] Verify persistence (close and reopen app)
- [ ] Test all sync modes
- [ ] Test delete delay slider
- [ ] Verify validation messages

### Edge Cases
- [ ] Empty folder (no subfolders)
- [ ] Root folder selection
- [ ] Very long folder paths
- [ ] Special characters in folder names
- [ ] Network disconnection during cloud browse
- [ ] Permission denied for local folder access
- [ ] Simultaneous configuration additions

## Security Considerations

1. **Path Validation**: Backend validates folder paths to prevent traversal attacks
2. **Authentication**: All cloud folder operations require valid OAuth tokens
3. **Local Storage**: Configurations stored in private app storage
4. **Permissions**: Minimal required permissions requested
5. **Input Validation**: All user inputs validated before storage

## Performance Considerations

1. **Lazy Loading**: Cloud folders loaded on-demand
2. **Caching**: Consider caching folder lists (future enhancement)
3. **Pagination**: Backend supports pagination for large folder lists
4. **Efficient Storage**: JSON serialization keeps data lightweight
5. **Background Operations**: Network calls happen asynchronously

## Troubleshooting

### Common Issues

**Issue**: FAB button doesn't open configuration screen
- **Solution**: Check if 10 configurations already exist; delete one to continue

**Issue**: Local folder picker shows "No folders found"
- **Solution**: Grant storage permissions in Android settings

**Issue**: Cloud folder picker shows error
- **Solution**: Verify internet connection and cloud account authentication

**Issue**: Configuration not saved
- **Solution**: Check all required fields are filled; verify not at 10-config limit

**Issue**: Folders not loading in cloud picker
- **Solution**: Re-authenticate with cloud provider; check network connection

## Developer Notes

### Code Organization
- **Activities**: Main UI screens
- **Adapters**: RecyclerView data binding
- **Models**: Data structures
- **Utils**: Helper classes (SyncConfigManager)
- **API**: Retrofit service interfaces

### Dependencies
- Gson: JSON serialization (via Retrofit)
- Material Design: UI components
- RecyclerView: List displays
- Retrofit/OkHttp: Network operations

### Build Configuration
No additional dependencies required beyond existing setup.

## Support and Documentation

For more information:
- [Main README](README.md)
- [API Documentation](API_DOCUMENTATION.md)
- [Folder Sync Feature](FOLDER_SYNC_FEATURE.md)
- [Quick Start Guide](QUICKSTART.md)
- [Contributing Guidelines](CONTRIBUTING.md)
