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

## 🔐 Permissions

The app requires the following Android permissions:

| Permission | Purpose | Required When |
|------------|---------|---------------|
| `INTERNET` | Backend API and cloud service communication | Always (automatic) |
| `GET_ACCOUNTS` | Retrieve Google accounts from device | Google Drive integration |
| `USE_CREDENTIALS` | Access account credentials | Google Drive integration |
| `READ_EXTERNAL_STORAGE` | Browse local folders | Local folder selection |
| `WRITE_EXTERNAL_STORAGE` | File sync operations | File upload/download |

### Permission Handling

**Runtime Permissions** (Android 6.0+):
- Storage permissions requested when needed
- User can grant or deny
- App provides fallback if denied

**Automatic Permissions**:
- `INTERNET` granted automatically
- No user action required

### Granting Permissions Manually

If permissions are denied or not working:

1. Open **Settings** on your device
2. Navigate to **Apps** → **Cloud Sync**
3. Tap **Permissions**
4. Enable required permissions:
   - Storage
   - Contacts (for GET_ACCOUNTS)

## 📚 Dependencies

### Core Libraries

| Library | Version | Purpose |
|---------|---------|---------|
| **AndroidX Core** | Latest | Core Android components |
| **AndroidX AppCompat** | Latest | Backward compatibility |
| **Material Components** | Latest | Material Design 3 UI |
| **ConstraintLayout** | Latest | Flexible layouts |

### Authentication

| Library | Version | Purpose |
|---------|---------|---------|
| **Google Play Services Auth** | Latest | Google account selection |
| **MSAL (Microsoft Authentication Library)** | 4.x | OneDrive authentication |

### Networking

| Library | Version | Purpose |
|---------|---------|---------|
| **Retrofit** | 2.9.x | Type-safe HTTP client |
| **OkHttp** | 4.x | HTTP client |
| **OkHttp Logging Interceptor** | 4.x | Network debugging |
| **Gson** | 2.x | JSON serialization |

### Utilities

| Library | Version | Purpose |
|---------|---------|---------|
| **RecyclerView** | Latest | List displays |
| **CardView** | Latest | Card-based layouts |
| **SwipeRefreshLayout** | Latest | Pull-to-refresh |

### Build Configuration

All dependencies are managed in `app/build.gradle`:

```gradle
dependencies {
    // AndroidX
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.9.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    
    // Google Auth
    implementation 'com.google.android.gms:play-services-auth:20.7.0'
    
    // Microsoft Auth
    implementation 'com.microsoft.identity.client:msal:4.9.0'
    
    // Networking
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:okhttp:4.11.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.11.0'
}
```

## 🔧 Troubleshooting

### Build & Installation Issues

**❌ Gradle sync fails**

**Solution:**
```bash
# In Android Studio:
File → Invalidate Caches → Invalidate and Restart

# Or clean and rebuild:
Build → Clean Project
Build → Rebuild Project
```

---

**❌ App won't install on device**

**Solution:**
- Check device has enough storage space
- Uninstall previous version
- Enable "Install from unknown sources" for development
- Check USB debugging is enabled
- Try `adb uninstall com.cloudsync.app`

---

**❌ Build error: SDK not found**

**Solution:**
1. Open SDK Manager (Tools → SDK Manager)
2. Install Android SDK Platform 24+
3. Install Build Tools 30.0.3+
4. Sync Gradle files

---

**❌ Build error: Could not find com.microsoft.device.display:display-mask**

**Solution:**
This is a known issue with MSAL 4.9.0. The Microsoft Maven repository is already configured in `settings.gradle`:
```gradle
repositories {
    google()
    mavenCentral()
    maven {
        url 'https://pkgs.dev.azure.com/MicrosoftDeviceSDK/DuoSDK-Public/_packaging/Duo-SDK-Feed/maven/v1'
    }
}
```
If you still encounter this issue:
1. Sync Gradle files (File → Sync Project with Gradle Files)
2. Clean project (Build → Clean Project)
3. Rebuild project (Build → Rebuild Project)

### Authentication Issues

**❌ Google account selection not working**

**Solution:**
- ✅ Add at least one Google account to device
- ✅ Grant GET_ACCOUNTS permission
- ✅ Check Settings → Apps → Cloud Sync → Permissions → Contacts
- ✅ Try logging out and back into Google account on device
- ✅ Clear app data: Settings → Apps → Cloud Sync → Storage → Clear Data

---

**❌ OneDrive authentication fails**

**Solution:**
- ✅ Verify MSAL configuration in `auth_config_msal.json`
- ✅ Check signature hash matches in all 3 locations:
  - `auth_config_msal.json`
  - `AndroidManifest.xml`
  - Azure Portal
- ✅ Ensure redirect URI format: `msauth://com.cloudsync.app/YOUR_HASH`
- ✅ Verify API permissions granted in Azure Portal
- ✅ Check admin consent granted
- ✅ Clear app data and try again

---

**❌ "Authentication failed" error**

**Solution:**
- Check internet connectivity
- Verify backend server is running
- Check backend URL is correct
- Review LogCat for detailed errors

### Backend Connection Issues

**❌ Cannot connect to backend**

**Solution:**

**For Emulator:**
```xml
<string name="backend_url">http://10.0.2.2:3000</string>
```

**For Physical Device:**
1. Find your computer's IP:
   ```bash
   # Mac/Linux
   ifconfig | grep "inet "
   
   # Windows
   ipconfig
   ```
2. Update `strings.xml`:
   ```xml
   <string name="backend_url">http://YOUR_IP:3000</string>
   ```
3. Ensure device and computer on same network
4. Check firewall isn't blocking port 3000

---

**❌ Connection timeout**

**Solution:**
- ✅ Verify backend server is running: `curl http://localhost:3000/auth/status`
- ✅ Check device has internet access
- ✅ Test connectivity: `adb shell ping YOUR_COMPUTER_IP`
- ✅ Disable VPN if active
- ✅ Check router firewall settings

### App Functionality Issues

**❌ App crashes on startup**

**Solution:**
1. Check LogCat for error stack trace
2. Common causes:
   - Missing backend URL
   - Invalid MSAL configuration
   - Permission issues
3. Clear app data and reinstall
4. Check all required files present

---

**❌ Local folder picker shows "No folders"**

**Solution:**
- Grant storage permissions
- Check external storage is mounted
- Try different folder (some may be restricted)
- Restart app after granting permissions

---

**❌ Cloud folder picker shows errors**

**Solution:**
- Verify internet connection
- Re-authenticate with cloud provider
- Check backend server logs
- Ensure cloud provider API is accessible

---

**❌ Sync fails or hangs**

**Solution:**
- Check both local and cloud folders accessible
- Verify sufficient permissions
- Check network stability
- Review LogCat for specific errors
- Try smaller files first

---

**❌ "Maximum configurations reached" but count shows less than 10**

**Solution:**
```bash
# Clear app data to reset:
Settings → Apps → Cloud Sync → Storage → Clear Data
```

### Debugging Tips

#### View Logs

```bash
# View all app logs
adb logcat | grep "CloudSync"

# View errors only
adb logcat *:E | grep "CloudSync"

# Save logs to file
adb logcat > logs.txt
```

#### Check App Info

```bash
# Check if app is installed
adb shell pm list packages | grep cloudsync

# Check app version
adb shell dumpsys package com.cloudsync.app | grep versionName

# Check granted permissions
adb shell dumpsys package com.cloudsync.app | grep permission
```

#### Network Debugging

```bash
# Check if backend is reachable from device
adb shell curl -v http://10.0.2.2:3000/auth/status

# Test with device IP
adb shell curl -v http://YOUR_COMPUTER_IP:3000/auth/status
```

### Common Error Messages

| Error | Meaning | Solution |
|-------|---------|----------|
| `NetworkOnMainThreadException` | Network call on UI thread | Already handled with async operations |
| `UnknownHostException` | Cannot resolve backend hostname | Check backend URL and connectivity |
| `ConnectException` | Cannot connect to backend | Verify backend running and URL correct |
| `SecurityException` | Missing permission | Grant required permission |
| `MsalClientException` | MSAL configuration error | Check MSAL config files |

### Getting Help

If issues persist:

1. **Check LogCat** for detailed error messages
2. **Review documentation** for similar issues
3. **Search existing issues** on GitHub
4. **Create new issue** with:
   - Android version
   - Device model
   - Steps to reproduce
   - Error messages from LogCat
   - Screenshots if applicable

## 🔒 Security Notes

### Development Security

- ⚠️ **Never commit secrets** to version control
  - Microsoft Client ID in `auth_config_msal.json`
  - Backend URLs with credentials
  - API keys or tokens
- ⚠️ **Use debug keystore** only for development
- ⚠️ **Different credentials** for dev and production

### Production Security Checklist

#### Code Obfuscation
Enable ProGuard/R8 in `app/build.gradle`:
```gradle
android {
    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

#### Signing Configuration
Create production keystore:
```bash
keytool -genkey -v -keystore release-keystore.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias release-key
```

**Never commit keystores to version control!**

#### Network Security
Configure network security in `res/xml/network_security_config.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="false">
        <trust-anchors>
            <certificates src="system" />
        </trust-anchors>
    </base-config>
    <!-- Only for development -->
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">10.0.2.2</domain>
    </domain-config>
</network-security-config>
```

#### Token Storage
- ✅ Use Android Keystore for sensitive data
- ✅ Don't store tokens in SharedPreferences (for production)
- ✅ Implement token encryption
- ✅ Clear tokens on logout

#### Best Practices
- 🔐 Use HTTPS for all network calls in production
- 🔐 Validate SSL certificates
- 🔐 Implement certificate pinning for critical apps
- 🔐 Sanitize all user inputs
- 🔐 Use SQLCipher for encrypted database (if needed)
- 🔐 Enable Google Play App Signing
- 🔐 Regular security audits

### OAuth Security

#### Google OAuth
- Use official Google Sign-In library
- Request minimal required scopes
- Validate tokens server-side
- Handle token refresh properly

#### Microsoft OAuth (MSAL)
- Keep MSAL library updated
- Use broker authentication when available
- Implement proper token caching
- Handle authentication errors gracefully

### Data Protection

- **Local Data**: Sync configurations stored in app-private SharedPreferences
- **In Transit**: Use HTTPS for backend communication
- **At Rest**: Consider encrypting sensitive local data
- **User Privacy**: Follow data minimization principles

## 🤝 Contributing

We welcome contributions to improve the Android app!

### How to Contribute

1. **Fork the repository**
2. **Create a feature branch**
3. **Make your changes**
4. **Test thoroughly** on multiple devices/versions
5. **Submit a pull request**

See the main [CONTRIBUTING.md](../../CONTRIBUTING.md) for detailed guidelines.

### Areas for Contribution

- 🐛 Bug fixes
- ✨ New features
- 🎨 UI improvements
- 📝 Documentation
- 🧪 Tests
- 🌍 Translations
- ♿ Accessibility improvements

## 📚 Additional Resources

### Documentation
- [Main README](../../README.md) - Project overview
- [Backend README](../../backend/README.md) - Backend setup
- [API Documentation](../../API_DOCUMENTATION.md) - API reference
- [Quick Start Guide](../../QUICKSTART.md) - Fast setup
- [Multiple Folder Bindings](../../MULTIPLE_FOLDER_BINDINGS.md) - Feature documentation

### Android Development
- [Android Developers](https://developer.android.com/) - Official documentation
- [Material Design](https://material.io/develop/android) - Design guidelines
- [Android Jetpack](https://developer.android.com/jetpack) - Modern Android components

### APIs & Libraries
- [Google Play Services Auth](https://developers.google.com/identity/sign-in/android)
- [MSAL for Android](https://docs.microsoft.com/en-us/azure/active-directory/develop/msal-android-overview)
- [Retrofit](https://square.github.io/retrofit/)
- [OkHttp](https://square.github.io/okhttp/)

### Community
- [GitHub Issues](../../../../issues) - Bug reports and features
- [GitHub Discussions](../../../../discussions) - Questions and ideas
- [Stack Overflow](https://stackoverflow.com/questions/tagged/android) - Technical questions

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](../../LICENSE) file for details.

---

<div align="center">
  <strong>Android Cloud Sync App</strong>
  <br>
  <sub>Part of the Cloud Sync Application</sub>
  <br><br>
  <sub>Built with Material Design 3 and modern Android practices</sub>
</div>
