# Multiple Folder Bindings - Architecture Diagram

## System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         Android App                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌────────────────┐         ┌─────────────────────────────┐    │
│  │  MainActivity  │─────────▶  SyncConfigListActivity     │    │
│  │                │         │  - List all configs (max 10)│    │
│  │  - Entry point │         │  - Add/Delete/View configs  │    │
│  │  - Account mgmt│         │  - Config counter display   │    │
│  └────────────────┘         └──────────┬──────────────────┘    │
│                                         │                         │
│                                         │ Tap "+" to add         │
│                                         ▼                         │
│                            ┌────────────────────────────┐        │
│                            │ FolderSyncConfigActivity   │        │
│                            │  - Configure sync settings │        │
│                            │  - Select folders          │        │
│                            │  - Choose sync mode        │        │
│                            └──────┬────────┬────────────┘        │
│                                   │        │                      │
│                  Browse Local     │        │  Browse Cloud       │
│                       ▼           │        │        ▼             │
│            ┌──────────────────┐  │        │  ┌────────────────┐ │
│            │ LocalFolderPicker│  │        │  │CloudFolderPicker│ │
│            │                  │  │        │  │                 │ │
│            │ - Browse device  │  │        │  │ - Browse cloud  │ │
│            │ - Navigate dirs  │  │        │  │ - Load via API  │ │
│            │ - Select folder  │  │        │  │ - Select folder │ │
│            └────────┬─────────┘  │        │  └────────┬────────┘ │
│                     │             │        │           │           │
│                     └─────────────┴────────┴───────────┘           │
│                                   │                                │
│                                   ▼                                │
│                         ┌──────────────────┐                      │
│                         │ SyncConfigManager│                      │
│                         │                  │                      │
│                         │ - Store configs  │                      │
│                         │ - Max 10 limit   │                      │
│                         │ - Persist data   │                      │
│                         └──────────────────┘                      │
│                                                                    │
└────────────────────────────────────────────────────────────────────┘
                                   │
                                   │ HTTP/HTTPS
                                   ▼
┌─────────────────────────────────────────────────────────────────┐
│                        Backend Server (Node.js)                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                    API Routes                            │   │
│  │                                                          │   │
│  │  /api/sync/folders/list                                 │   │
│  │  - List cloud folders (Google Drive / OneDrive)         │   │
│  │  - Query params: folderPath, provider                   │   │
│  │                                                          │   │
│  │  /api/sync-config                                       │   │
│  │  - GET: List all configs                                │   │
│  │  - POST: Create config (max 10 validation)              │   │
│  │  - PUT: Update config                                   │   │
│  │  - DELETE: Delete config                                │   │
│  └──────────────────┬───────────────────────────────────────┘   │
│                     │                                             │
│                     ▼                                             │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │               Cloud Services                             │   │
│  │                                                          │   │
│  │  GoogleDriveService              OneDriveService        │   │
│  │  - listFolders()                 - listFolders()        │   │
│  │  - listFoldersInPath()           - listFoldersInPath()  │   │
│  │  - getOrCreateFolder()           - getOrCreateFolder()  │   │
│  └──────────────┬───────────────────────┬──────────────────┘   │
│                 │                       │                        │
└─────────────────┼───────────────────────┼────────────────────────┘
                  │                       │
                  ▼                       ▼
         ┌─────────────────┐    ┌─────────────────┐
         │  Google Drive   │    │    OneDrive     │
         │     API         │    │      API        │
         └─────────────────┘    └─────────────────┘
```

## Data Flow

### Adding a Sync Configuration

```
User Action                  App Layer                  Backend              Cloud
    │                           │                          │                   │
    ├─ Tap "+" button           │                          │                   │
    ├──────────────────────────▶│                          │                   │
    │                           │ Open Config Activity     │                   │
    │                           │                          │                   │
    ├─ Tap "Browse Local"       │                          │                   │
    ├──────────────────────────▶│                          │                   │
    │                           │ Open LocalFolderPicker   │                   │
    │                           │ (reads device storage)   │                   │
    │                           │                          │                   │
    ├─ Select folder            │                          │                   │
    ├──────────────────────────▶│                          │                   │
    │                           │ Return path              │                   │
    │                           │                          │                   │
    ├─ Tap "Browse Cloud"       │                          │                   │
    ├──────────────────────────▶│                          │                   │
    │                           │ Open CloudFolderPicker   │                   │
    │                           ├─────────────────────────▶│                   │
    │                           │ GET /folders/list        │                   │
    │                           │                          ├──────────────────▶│
    │                           │                          │ List folders      │
    │                           │                          ◀──────────────────┤
    │                           ◀─────────────────────────┤                   │
    │                           │ Display folders          │                   │
    │                           │                          │                   │
    ├─ Select folder            │                          │                   │
    ├──────────────────────────▶│                          │                   │
    │                           │ Return cloud path        │                   │
    │                           │                          │                   │
    ├─ Configure settings       │                          │                   │
    ├──────────────────────────▶│                          │                   │
    │                           │ Set sync mode, delay     │                   │
    │                           │                          │                   │
    ├─ Tap "Save"               │                          │                   │
    ├──────────────────────────▶│                          │                   │
    │                           │ SyncConfigManager        │                   │
    │                           │ - Check if < 10 configs  │                   │
    │                           │ - Generate ID            │                   │
    │                           │ - Save to SharedPrefs    │                   │
    │                           │                          │                   │
    │  ◀─ Success message ──────┤                          │                   │
    │                           │ Return to list           │                   │
    │                           │                          │                   │
```

## Storage Architecture

### Local Storage (Android)

```
SharedPreferences: "sync_configs"
├─ Key: "configs"
└─ Value: JSON Array
    ├─ Config 1
    │   ├─ id: "config_1234567890_123"
    │   ├─ localFolderPath: "/storage/emulated/0/Documents"
    │   ├─ cloudFolderPath: "Documents/Work"
    │   ├─ provider: "google"
    │   ├─ syncMode: "two_way"
    │   ├─ deleteDelayDays: 0
    │   ├─ enabled: true
    │   └─ timestamps
    │
    ├─ Config 2
    │   └─ ...
    │
    └─ ... (up to 10 configs)
```

### Backend Storage (In-Memory)

```
Map<userId, Array<SyncConfig>>
├─ user_1
│   ├─ [Config 1, Config 2, ...]
│   └─ Max 10 configs
│
├─ user_2
│   └─ [Config 1, Config 2, ...]
│
└─ ...
```

## Component Interactions

### Folder Picker Flow

```
┌──────────────────────────────────────────────────────────┐
│                   Folder Picker Activity                  │
├──────────────────────────────────────────────────────────┤
│                                                           │
│  ┌─────────────┐      ┌──────────────┐                  │
│  │   Toolbar   │      │ Current Path │                  │
│  │  (back btn) │      │   TextView   │                  │
│  └─────────────┘      └──────────────┘                  │
│                                                           │
│  ┌───────────────────────────────────────────────────┐  │
│  │           RecyclerView (Folder List)              │  │
│  │  ┌──────────────────────────────────────────────┐ │  │
│  │  │  📁 .. (Parent Folder)                       │ │  │
│  │  ├──────────────────────────────────────────────┤ │  │
│  │  │  📁 Documents                                │ │  │
│  │  ├──────────────────────────────────────────────┤ │  │
│  │  │  📁 Pictures                                 │ │  │
│  │  ├──────────────────────────────────────────────┤ │  │
│  │  │  📁 Downloads                                │ │  │
│  │  └──────────────────────────────────────────────┘ │  │
│  └───────────────────────────────────────────────────┘  │
│                                                           │
│  ┌────────────┐                    ┌─────────────────┐  │
│  │   Cancel   │                    │ Select Folder   │  │
│  └────────────┘                    └─────────────────┘  │
└──────────────────────────────────────────────────────────┘
```

### Sync Config List View

```
┌──────────────────────────────────────────────────────────┐
│                  Manage Sync Configs                      │
├──────────────────────────────────────────────────────────┤
│                                                           │
│  Configurations: 5 / 10                                   │
│                                                           │
│  ┌───────────────────────────────────────────────────┐  │
│  │  [G] Two-Way Sync                        [Active] │  │
│  │  /storage/emulated/0/Documents                    │  │
│  │  → Documents/Work                                 │  │
│  └───────────────────────────────────────────────────┘  │
│                                                           │
│  ┌───────────────────────────────────────────────────┐  │
│  │  [O] Upload Only                        [Active]  │  │
│  │  /storage/emulated/0/DCIM                         │  │
│  │  → Photos/Backup                                  │  │
│  └───────────────────────────────────────────────────┘  │
│                                                           │
│  ┌───────────────────────────────────────────────────┐  │
│  │  [G] Upload then Delete (7d)           [Disabled] │  │
│  │  /storage/emulated/0/Download                     │  │
│  │  → Archive                                        │  │
│  └───────────────────────────────────────────────────┘  │
│                                                           │
│                                                           │
│                                               ┌────┐     │
│                                               │ +  │     │
│                                               └────┘     │
└──────────────────────────────────────────────────────────┘

Legend:
[G] = Google Drive
[O] = OneDrive
```

## Class Relationships

```
┌─────────────────────────┐
│  SyncConfigListActivity │
└────────┬────────────────┘
         │
         │ uses
         ▼
┌─────────────────────────┐         ┌────────────────────┐
│   SyncConfigManager     │────────▶│   SyncConfig       │
│                         │  stores │   (Model)          │
│ - getAllConfigs()       │         └────────────────────┘
│ - addConfig()           │
│ - deleteConfig()        │
│ - hasReachedMaxLimit()  │
└────────┬────────────────┘
         │
         │ persists to
         ▼
┌─────────────────────────┐
│  SharedPreferences      │
│  (Android Storage)      │
└─────────────────────────┘


┌──────────────────────────┐
│ FolderSyncConfigActivity │
└────────┬─────────────────┘
         │
         │ launches
         ├────────────────────────────┬────────────────────────┐
         ▼                            ▼                        ▼
┌───────────────────┐    ┌─────────────────────┐   ┌───────────────────┐
│LocalFolderPicker  │    │CloudFolderPicker    │   │SyncConfigManager  │
│                   │    │                     │   │                   │
│- FolderAdapter    │    │- CloudFolderAdapter │   │- validates limit  │
└───────────────────┘    └─────────┬───────────┘   └───────────────────┘
                                   │
                                   │ calls API
                                   ▼
                         ┌────────────────────┐
                         │  Backend API       │
                         │  /folders/list     │
                         └────────────────────┘
```

## Security & Validation Flow

```
┌──────────────────────────────────────────────────────────────┐
│                      Validation Layers                        │
├──────────────────────────────────────────────────────────────┤
│                                                               │
│  User Input                                                   │
│      ├─ Local Folder Path                                    │
│      ├─ Cloud Folder Path                                    │
│      ├─ Sync Mode                                            │
│      └─ Delete Delay                                         │
│                                                               │
│      ↓                                                        │
│                                                               │
│  Android App Validation                                       │
│      ├─ Required field checks                                │
│      ├─ Max 10 config limit                                  │
│      ├─ Valid sync mode enum                                 │
│      └─ Delete delay range (0-30)                            │
│                                                               │
│      ↓                                                        │
│                                                               │
│  Backend Validation                                           │
│      ├─ Max 10 config limit per user                         │
│      ├─ Path traversal prevention (..)                       │
│      ├─ Valid provider (google/microsoft)                    │
│      ├─ Valid sync mode                                      │
│      └─ OAuth token validation                               │
│                                                               │
│      ↓                                                        │
│                                                               │
│  Cloud Provider Validation                                    │
│      ├─ Authentication                                        │
│      ├─ Authorization                                         │
│      └─ Folder access permissions                            │
│                                                               │
└──────────────────────────────────────────────────────────────┘
```

## Performance Considerations

```
Optimization Points:

1. Local Folder Browsing
   - File I/O operations
   - Filter hidden folders
   - Sort by name
   - Limited to visible folders only

2. Cloud Folder Browsing
   - Async API calls
   - Progress indicators
   - Error handling & retry
   - Pagination support (backend)

3. Config Storage
   - Lightweight JSON serialization
   - In-memory operations
   - Persist only on changes
   - Max 10 configs = small data size

4. UI Updates
   - RecyclerView for efficient lists
   - Update only changed items
   - Lazy loading where applicable
   - Material Design components
```

## Error Handling Strategy

```
Layer              Error Type              Handling
─────────────────  ──────────────────────  ────────────────────────
Android UI         User Input              Snackbar with message
                   Validation Failed       Focus on field + message
                   Max Limit Reached       Prevent action + explain

Android Logic      File Access Denied      Permission request
                   Storage Full            Alert user
                   Config Save Failed      Retry option

Network            Connection Failed       Retry button
                   Timeout                 Retry with backoff
                   Invalid Response        Error message

Backend            Max Limit Reached       HTTP 400 + error msg
                   Invalid Path            HTTP 400 + validation
                   Auth Failed             HTTP 401 + re-auth

Cloud Provider     Access Denied           Re-authenticate
                   Folder Not Found        Create or select other
                   API Quota Exceeded      Wait and retry
```

## Summary

The architecture implements a clean separation of concerns:

- **UI Layer**: Activities and Adapters for user interaction
- **Business Logic**: SyncConfigManager for validation and storage
- **Network Layer**: Retrofit API client for backend communication
- **Backend Layer**: Express routes and cloud service wrappers
- **Cloud Layer**: Google Drive and OneDrive APIs

All layers work together to provide a seamless experience for managing multiple folder sync configurations with proper validation, error handling, and user feedback.
