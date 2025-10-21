# Cloud Sync Android App

A powerful Android mobile application for synchronizing device files with Google Drive and OneDrive. Features Material Design 3 UI, multiple sync configurations, and comprehensive folder management.

## 📋 Table of Contents

- [Features](#-features)
- [Requirements](#-requirements)
- [Setup Instructions](#-setup-instructions)
- [App Structure](#-app-structure)
- [Usage Guide](#-usage-guide)
- [Permissions](#-permissions)
- [Dependencies](#-dependencies)
- [Troubleshooting](#-troubleshooting)
- [Security](#-security-notes)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

### 🔐 Authentication
- **Google Account Integration**: Select from device accounts using Google Play Services
- **OneDrive/Microsoft Login**: MSAL-powered authentication for Microsoft accounts
- **Multi-Account Support**: Switch between Google and OneDrive accounts
- **Secure Token Storage**: Android Keystore integration for credentials

### 📁 Folder Synchronization
- **Multiple Sync Pairs**: Configure up to 10 independent folder synchronizations
- **Local Folder Picker**: Browse and select device folders with intuitive navigation
- **Cloud Folder Browser**: Navigate Google Drive and OneDrive folders
- **Five Sync Modes**:
  - 🔼 Upload Only
  - 🔼🗑️ Upload then Delete
  - 🔽 Download Only
  - 🔽🗑️ Download then Delete
  - 🔄 Two-Way Sync
- **Delete Delay Configuration**: Set delays from 0-30 days for automatic deletion

### 📱 User Interface
- **Material Design 3**: Modern, beautiful UI components
- **Dark Mode Support**: Follows system theme preferences
- **Responsive Layouts**: Optimized for different screen sizes
- **Intuitive Navigation**: Easy-to-use interface for all features
- **Real-Time Updates**: Live sync status and progress tracking

### ⚙️ Configuration Management
- **CRUD Operations**: Create, view, update, and delete sync configurations
- **Enable/Disable Toggles**: Control active configurations without deletion
- **Configuration Counter**: Visual indicator of used slots (X/10)
- **Persistent Storage**: Configurations saved locally using SharedPreferences

## 📦 Requirements

### Development Environment
- ✅ **Android Studio** Arctic Fox (2020.3.1) or later
  - [Download Android Studio](https://developer.android.com/studio)
- ✅ **JDK** 8 or higher (JDK 11 recommended)
- ✅ **Gradle** 8.0+ (managed by Android Studio)
- ✅ **Android SDK** with the following components:
  - SDK Platform 24+ (Android 7.0+)
  - Build Tools 30.0.3+
  - SDK Tools

### Target Devices
- 📱 **Minimum SDK**: API 24 (Android 7.0 Nougat)
- 📱 **Target SDK**: API 33 (Android 13)
- 📱 **Test Coverage**: Android 7.0 through Android 14

### Backend Requirements
- 🖥️ Running backend server (see [backend/README.md](../backend/README.md))
- 🌐 Network connectivity between device and backend

## Setup Instructions

### 1. Open Project in Android Studio

1. Open Android Studio
2. Click "Open an existing project"
3. Navigate to `frontend/CloudSyncApp` folder
4. Click "OK" to open the project

### 2. Configure Backend URL

Edit `app/src/main/res/values/strings.xml` and update the backend URL:

```xml
<string name="backend_url">http://YOUR_BACKEND_IP:3000</string>
<string name="backend_api_url">http://YOUR_BACKEND_IP:3000/api</string>
```

**Note**: For Android emulator, use `http://10.0.2.2:3000` to connect to localhost on your development machine.

### 3. Configure OneDrive Authentication

#### Get Microsoft App Credentials

1. Go to [Azure Portal](https://portal.azure.com/)
2. Navigate to "Azure Active Directory" → "App registrations"
3. Click "New registration"
4. Set up your app with:
   - Name: "Cloud Sync App"
   - Supported account types: "Accounts in any organizational directory and personal Microsoft accounts"
   - Redirect URI: Leave blank for now (we'll add it later)
5. Note down the **Application (client) ID**

#### Get Signature Hash

Run this command to get your app's signature hash:

```bash
keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore | openssl sha1 -binary | openssl base64
```

Default password for debug keystore: `android`

#### Update Configuration

1. Update `app/src/main/res/raw/auth_config_msal.json`:
   - Replace `YOUR_MICROSOFT_CLIENT_ID` with your Application (client) ID
   - Replace `YOUR_SIGNATURE_HASH` with the hash you generated

2. Update `app/src/main/AndroidManifest.xml`:
   - Replace `YOUR_SIGNATURE_HASH` with the hash you generated (in the BrowserTabActivity data path)

3. In Azure Portal, add redirect URI:
   - Format: `msauth://com.cloudsync.app/YOUR_SIGNATURE_HASH`
   - Go to your app → Authentication → Add a platform → Android
   - Enter package name: `com.cloudsync.app`
   - Enter signature hash

#### Add API Permissions

In Azure Portal, go to your app → API permissions:
1. Click "Add a permission"
2. Select "Microsoft Graph"
3. Select "Delegated permissions"
4. Add:
   - `User.Read`
   - `Files.ReadWrite`
5. Click "Add permissions"
6. Click "Grant admin consent"

### 4. Build and Run

1. Connect an Android device or start an emulator
2. Click the "Run" button in Android Studio
3. Select your device/emulator
4. Wait for the app to build and install

## 🏗️ App Structure

```
CloudSyncApp/
├── app/                                   # Application module
│   ├── src/main/
│   │   ├── java/com/cloudsync/app/       # Java source code
│   │   │   ├── MainActivity.java                      # Main activity
│   │   │   ├── AccountSelectionActivity.java          # Google account picker
│   │   │   ├── OneDriveAuthActivity.java              # OneDrive authentication
│   │   │   ├── SyncConfigListActivity.java            # List all configurations
│   │   │   ├── FolderSyncConfigActivity.java          # Create/edit config
│   │   │   ├── LocalFolderPickerActivity.java         # Local folder browser
│   │   │   ├── CloudFolderPickerActivity.java         # Cloud folder browser
│   │   │   ├── adapters/                              # RecyclerView adapters
│   │   │   │   ├── AccountAdapter.java
│   │   │   │   ├── SyncConfigAdapter.java
│   │   │   │   ├── FolderAdapter.java
│   │   │   │   └── CloudFolderAdapter.java
│   │   │   ├── models/                                # Data models
│   │   │   │   ├── SyncConfig.java
│   │   │   │   └── CloudFolder.java
│   │   │   ├── utils/                                 # Utility classes
│   │   │   │   └── SyncConfigManager.java             # Config persistence
│   │   │   └── api/                                   # API interfaces
│   │   │       └── ApiService.java                    # Retrofit service
│   │   ├── res/                          # Resources
│   │   │   ├── layout/                   # XML layouts
│   │   │   │   ├── activity_main.xml
│   │   │   │   ├── activity_sync_config_list.xml
│   │   │   │   ├── activity_folder_sync_config.xml
│   │   │   │   ├── activity_local_folder_picker.xml
│   │   │   │   ├── activity_cloud_folder_picker.xml
│   │   │   │   └── item_*.xml            # List item layouts
│   │   │   ├── values/                   # Value resources
│   │   │   │   ├── strings.xml           # String resources
│   │   │   │   ├── colors.xml            # Color definitions
│   │   │   │   └── themes.xml            # Material themes
│   │   │   ├── drawable/                 # Drawable resources
│   │   │   └── raw/                      # Raw resources
│   │   │       └── auth_config_msal.json # MSAL configuration
│   │   └── AndroidManifest.xml           # App manifest
│   ├── build.gradle                      # App build configuration
│   └── proguard-rules.pro                # ProGuard/R8 rules
├── gradle/                                # Gradle wrapper
└── build.gradle                          # Project build configuration
```

### Key Components

#### Activities
- **MainActivity**: Entry point, account selection, and main navigation
- **SyncConfigListActivity**: Displays all sync configurations with management options
- **FolderSyncConfigActivity**: Form to create/edit sync configurations
- **LocalFolderPickerActivity**: Browse device folders with navigation
- **CloudFolderPickerActivity**: Browse Google Drive/OneDrive folders

#### Adapters
- **SyncConfigAdapter**: RecyclerView adapter for sync configuration list
- **FolderAdapter**: Adapter for local folder browsing
- **CloudFolderAdapter**: Adapter for cloud folder browsing

#### Utilities
- **SyncConfigManager**: Handles configuration persistence using SharedPreferences
  - Save/load configurations
  - Enforce 10-config limit
  - Generate unique IDs

## 📖 Usage Guide

### First-Time Setup

#### 1. Select Cloud Provider Account

**For Google Drive:**
1. Launch the app
2. Tap **"Select Google Account"**
3. Choose from device accounts
4. Account will be displayed on main screen

**For OneDrive:**
1. Tap **"Add OneDrive Account"**
2. Tap **"Sign In"**
3. Complete Microsoft authentication
4. Grant requested permissions
5. Account connected successfully

#### 2. Create Your First Sync Configuration

1. From main screen, tap **"Manage Folder Sync"**
2. Tap the **"+"** (FAB) button
3. **Select Local Folder:**
   - Tap "Browse Local Folder"
   - Navigate to desired folder
   - Tap folder to enter it
   - Use ".." to go up one level
   - Tap "Select This Folder" when ready
4. **Select Cloud Provider:**
   - Choose "Google Drive" or "OneDrive" from dropdown
5. **Select Cloud Folder:**
   - Tap "Browse Cloud Folder"
   - Navigate through cloud folders
   - Tap "Select This Folder" when ready
6. **Configure Sync Mode:**
   - Choose from 5 modes:
     - Upload Only
     - Upload then Delete
     - Download Only
     - Download then Delete
     - Two-Way Sync
7. **Set Delete Delay** (if applicable):
   - Adjust slider from 0-30 days
   - Only visible for modes with deletion
8. Tap **"Save Configuration"**

#### 3. Execute Sync

1. Return to main screen
2. Tap **"Execute Sync"** button
3. View progress and results
4. Check sync status in configuration list

### Managing Configurations

#### View All Configurations
- Tap "Manage Folder Sync"
- See all configurations (X/10 counter at top)
- Each card shows:
  - Local and cloud folder paths
  - Provider icon (Google/OneDrive)
  - Sync mode
  - Enable/disable status
  - Last sync time

#### Enable/Disable Configuration
- Tap the toggle switch on configuration card
- Green = Enabled, Gray = Disabled
- Disabled configs won't sync

#### Delete Configuration
1. Long press on configuration card, or
2. Tap configuration → Select "Delete" from menu
3. Confirm deletion
4. Counter updates (e.g., 4/10 → 3/10)

#### Configuration Limit
- Maximum 10 configurations allowed
- When limit reached:
  - FAB button shows warning
  - Must delete existing config to add new one
  - Counter shows "10/10"

## Permissions

The app requires the following permissions:

- `INTERNET`: For communicating with the backend API and cloud services
- `GET_ACCOUNTS`: For retrieving Google accounts from the device
- `USE_CREDENTIALS`: For accessing account credentials

## Dependencies

- **AndroidX Libraries**: Core Android components
- **Material Components**: Material Design UI components
- **Google Play Services Auth**: Google Sign-In and account access
- **Microsoft Authentication Library (MSAL)**: OneDrive authentication
- **Retrofit**: HTTP client for API calls
- **OkHttp**: HTTP client and logging

## Troubleshooting

### Google Account Selection Not Working

- Make sure you have at least one Google account added to your device
- Grant the GET_ACCOUNTS permission when prompted
- On Android 6.0+, you may need to manually grant the permission in Settings

### OneDrive Authentication Fails

- Verify your MSAL configuration in `auth_config_msal.json`
- Check that the signature hash is correct
- Ensure the redirect URI is properly configured in Azure Portal
- Make sure API permissions are granted in Azure Portal

### Cannot Connect to Backend

- Verify the backend URL in `strings.xml`
- For emulator, use `http://10.0.2.2:3000` for localhost
- For physical device, use your computer's IP address
- Make sure the backend server is running

## Security Notes

- Never commit your Microsoft Client ID or other secrets to version control
- Use different OAuth credentials for production builds
- Always use HTTPS in production
- Store access tokens securely using Android Keystore

## Contributing

See the main [CONTRIBUTING.md](../../CONTRIBUTING.md) for contribution guidelines.

## License

MIT
