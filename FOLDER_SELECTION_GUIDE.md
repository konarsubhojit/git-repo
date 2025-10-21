# Folder Selection UI Guide

## Overview

This guide explains how to use the folder selection UI for configuring multiple folder sync bindings in the Cloud Sync app.

## Quick Start

### 1. Access Sync Configuration Manager

From the main screen:
1. Tap **"Manage Folder Sync"** button
2. You'll see a list of your existing sync configurations (if any)
3. Configuration counter shows: **"Configurations: X / 10"**

### 2. Add New Sync Configuration

1. Tap the **"+"** (FAB) button at bottom-right
2. Opens **Folder Sync Configuration** screen

## Selecting Folders

### Local Folder Selection

#### Option 1: Browse with Folder Picker
1. In Folder Sync Configuration screen
2. Tap **"Browse Local Folder"** button
3. **Local Folder Picker** opens:
   - Shows current path at top
   - Lists all accessible folders
   - Tap any folder to navigate into it
   - Tap **".." (Parent Folder)** to go back
   - When at desired folder, tap **"Select This Folder"**
4. Selected path automatically fills the "Local Folder Path" field

#### Option 2: Manual Entry
1. Tap on **"Local Folder Path"** text field
2. Type the full path manually (e.g., `/storage/emulated/0/Documents`)
3. Press Enter or tap elsewhere

**Example Paths:**
```
/storage/emulated/0/DCIM/Camera
/storage/emulated/0/Documents
/storage/emulated/0/Download
/storage/emulated/0/Pictures
```

### Cloud Folder Selection

#### Step 1: Select Provider
1. Tap **"Cloud Provider"** dropdown
2. Choose either:
   - **Google Drive**
   - **OneDrive**

#### Step 2: Browse Cloud Folders
1. Tap **"Browse Cloud Folder"** button
2. **Cloud Folder Picker** opens:
   - Shows current path at top (starts at "/")
   - Loading indicator appears while fetching folders
   - Lists all folders from your cloud storage
   - Tap any folder to navigate into it
   - Tap **".." (Parent Folder)** to go back
   - When at desired folder, tap **"Select This Folder"**
3. Selected path automatically fills the "Cloud Folder Path" field

#### Option 2: Manual Entry
1. Tap on **"Cloud Folder Path"** text field
2. Type the cloud path manually (e.g., `Documents/Work`)
3. Press Enter or tap elsewhere

**Example Cloud Paths:**
```
Documents
Photos/2024
Projects/MyApp
Backup/Phone
Work/Reports
```

## Configuring Sync Settings

### 1. Sync Mode Selection

Tap **"Sync Mode"** dropdown and choose:

- **Upload Only**: Upload files from local to cloud (one-way)
- **Upload then Delete**: Upload and remove from local after delay
- **Download Only**: Download files from cloud to local (one-way)
- **Download then Delete**: Download and remove from cloud after delay
- **Two-Way Sync**: Bidirectional synchronization

### 2. Delete Delay (if applicable)

For "Upload then Delete" or "Download then Delete" modes:
1. A **slider** appears
2. Drag slider or tap to set delay: **0-30 days**
3. "0 days" = Immediate deletion
4. Shows preview: "Immediate" or "X day(s)"

### 3. Save Configuration

1. Review all settings
2. Tap **"Save Configuration"** button
3. Returns to Sync Configuration List
4. New configuration appears in the list

## Managing Existing Configurations

### View Configuration Details
1. Tap on any configuration in the list
2. Select **"View Details"**
3. See all configuration properties

### Enable/Disable Configuration
1. Tap on configuration
2. Select **"Toggle Enable/Disable"**
3. Status updates immediately
4. Disabled configurations won't sync

### Delete Configuration
1. **Option A**: Long press on configuration
2. **Option B**: Tap configuration → Select "Delete"
3. Confirm deletion in dialog
4. Configuration removed and count updates

## Configuration Limit (Max 10)

### When Below Limit
- Configuration counter shows: **"Configurations: 5 / 10"**
- FAB button is active
- Can add new configurations

### When At Limit
- Configuration counter shows: **"Configurations: 10 / 10"**
- FAB button still visible but shows warning
- Tapping FAB shows message:
  > "Maximum number of sync configurations (10) reached. Please delete a configuration to add a new one."
- Must delete a configuration to add new ones

## Complete Example Workflow

### Example 1: Photo Backup to Google Drive

1. **Open app** → Tap "Manage Folder Sync"
2. **Add new config** → Tap "+" button
3. **Select local folder**:
   - Tap "Browse Local Folder"
   - Navigate to `/storage/emulated/0/DCIM/Camera`
   - Tap "Select This Folder"
4. **Select cloud provider**: Choose "Google Drive"
5. **Select cloud folder**:
   - Tap "Browse Cloud Folder"
   - Navigate to `Photos/Backup` (or create if needed)
   - Tap "Select This Folder"
6. **Configure sync**:
   - Sync Mode: "Upload Only"
7. **Save** → Tap "Save Configuration"

**Result**: Photos will be uploaded to Google Drive but not deleted locally.

### Example 2: Archive Documents to OneDrive

1. **Open app** → Tap "Manage Folder Sync"
2. **Add new config** → Tap "+" button
3. **Select local folder**:
   - Type manually: `/storage/emulated/0/Documents/Archive`
4. **Select cloud provider**: Choose "OneDrive"
5. **Select cloud folder**:
   - Type manually: `Archive`
6. **Configure sync**:
   - Sync Mode: "Upload then Delete"
   - Delete Delay: Drag slider to "7 days"
7. **Save** → Tap "Save Configuration"

**Result**: Documents uploaded to OneDrive and deleted from phone after 7 days.

### Example 3: Two-Way Sync for Work Files

1. **Open app** → Tap "Manage Folder Sync"
2. **Add new config** → Tap "+" button
3. **Select local folder**:
   - Tap "Browse Local Folder"
   - Navigate to `/storage/emulated/0/Documents/Work`
   - Tap "Select This Folder"
4. **Select cloud provider**: Choose "Google Drive"
5. **Select cloud folder**:
   - Tap "Browse Cloud Folder"
   - Navigate to `Work/Projects`
   - Tap "Select This Folder"
6. **Configure sync**:
   - Sync Mode: "Two-Way Sync"
7. **Save** → Tap "Save Configuration"

**Result**: Files stay in sync between phone and Google Drive.

## Folder Picker Navigation Tips

### Local Folder Picker
- **Start location**: Usually `/storage/emulated/0/` (internal storage)
- **Going up**: Always shows ".." option if not at root
- **Hidden folders**: Not shown by default
- **Permissions**: May need to grant storage access on first use

### Cloud Folder Picker
- **Start location**: Root of your cloud storage ("/" path)
- **Going up**: Shows ".." when in subfolder
- **Loading**: Brief delay while fetching from cloud
- **Empty folders**: Shows "No folders found" message
- **Network required**: Must be online to browse

## Validation and Errors

### Common Validation Messages

**"Please enter a local folder path"**
- Solution: Select or type a local folder path

**"Please enter a cloud folder path"**
- Solution: Select or type a cloud folder path

**"Maximum number of sync configurations (10) reached..."**
- Solution: Delete an existing configuration first

### Common Error Messages

**"Cannot access this folder"**
- Solution: Check folder permissions or select different folder

**"Failed to load folders"**
- Solution: Check internet connection and cloud account login

**"Network error: ..."**
- Solution: Verify internet connection, retry operation

## Best Practices

### Folder Path Organization
1. Use descriptive folder names
2. Keep folder structure organized
3. Avoid deeply nested paths (>5 levels)
4. Use consistent naming conventions

### Configuration Management
1. Give each config a clear purpose
2. Use different sync modes appropriately
3. Group related folders under same parent
4. Review and clean up unused configs

### Performance Tips
1. Don't sync large folders unnecessarily
2. Use appropriate sync modes
3. Set realistic delete delays
4. Disable configs when not needed

## Keyboard Shortcuts

- **Back button**: Navigate to parent folder (in folder pickers)
- **Back button**: Exit activity (at root level)
- **Enter**: Confirm text input in manual entry

## Accessibility

- All buttons have content descriptions
- Clear visual hierarchy
- High contrast icons
- Touch targets meet minimum size requirements

## Troubleshooting

### Can't See Local Folders
1. Check storage permissions in Android Settings
2. Navigate to parent directory
3. Ensure folder exists and is not hidden

### Can't See Cloud Folders
1. Verify internet connection
2. Ensure cloud account is logged in
3. Check provider selection matches your account
4. Try logging out and back into cloud account

### Configuration Not Saving
1. Fill all required fields (marked with *)
2. Verify not at 10-config limit
3. Check for validation error messages
4. Try again or restart app

### FAB Button Not Working
1. Check if already at 10 configurations
2. Look for error snackbar message
3. Delete a configuration if at limit

## API Integration Notes

For developers implementing similar features:

### Local Folder Picker Implementation
```java
// Start activity
Intent intent = new Intent(this, LocalFolderPickerActivity.class);
startActivityForResult(intent, REQUEST_CODE);

// Handle result
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK && data != null) {
        String folderPath = data.getStringExtra("folder_path");
        // Use folderPath
    }
}
```

### Cloud Folder Picker Implementation
```java
// Start activity with provider
Intent intent = new Intent(this, CloudFolderPickerActivity.class);
intent.putExtra("provider", "google"); // or "microsoft"
startActivityForResult(intent, REQUEST_CODE);

// Handle result
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK && data != null) {
        String folderPath = data.getStringExtra("folder_path");
        // Use folderPath
    }
}
```

### Sync Config Manager Usage
```java
SyncConfigManager manager = new SyncConfigManager(context);

// Add config
SyncConfig config = new SyncConfig(...);
boolean added = manager.addConfig(config);

// Get all configs
List<SyncConfig> configs = manager.getAllConfigs();

// Delete config
boolean deleted = manager.deleteConfig(configId);

// Check limit
boolean atLimit = manager.hasReachedMaxLimit();
int remaining = manager.getRemainingSlots();
```

## Summary

The folder selection UI provides an intuitive way to:
- ✅ Browse and select local device folders
- ✅ Browse and select cloud storage folders  
- ✅ Configure sync settings with visual controls
- ✅ Manage multiple configurations (up to 10)
- ✅ View, edit, and delete configurations
- ✅ Enforce configuration limits with clear feedback

For more details, see:
- [Multiple Folder Bindings Documentation](MULTIPLE_FOLDER_BINDINGS.md)
- [Folder Sync Feature Documentation](FOLDER_SYNC_FEATURE.md)
- [API Documentation](API_DOCUMENTATION.md)
