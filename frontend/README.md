# Cloud Sync Android App

An Android application that allows users to sync data to Google Drive and OneDrive cloud storage providers.

## Features

- **Google Account Selection**: Select from already logged-in Google accounts on the device
- **OneDrive Account Management**: Sign in and manage OneDrive accounts using Microsoft Authentication Library (MSAL)
- **Cloud Storage Sync**: Upload and download data to/from cloud storage
- **Material Design**: Modern Android UI with Material Design 3 components
- **Secure Authentication**: OAuth 2.0 authentication for both Google and OneDrive

## Requirements

- Android Studio Arctic Fox or later
- Android SDK 24 or higher (Android 7.0+)
- Gradle 8.0+
- Java 8 or higher

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

## App Structure

```
app/
├── src/main/
│   ├── java/com/cloudsync/app/
│   │   ├── MainActivity.java              # Main screen
│   │   ├── AccountSelectionActivity.java  # Google account selection
│   │   ├── AccountAdapter.java            # RecyclerView adapter for accounts
│   │   └── OneDriveAuthActivity.java      # OneDrive authentication
│   ├── res/
│   │   ├── layout/                        # UI layouts
│   │   │   ├── activity_main.xml
│   │   │   ├── activity_account_selection.xml
│   │   │   ├── activity_onedrive_auth.xml
│   │   │   └── item_account.xml
│   │   ├── values/                        # Resources
│   │   │   ├── strings.xml
│   │   │   ├── colors.xml
│   │   │   └── themes.xml
│   │   └── raw/
│   │       └── auth_config_msal.json     # MSAL configuration
│   └── AndroidManifest.xml
├── build.gradle                           # App-level build config
└── proguard-rules.pro                     # ProGuard rules
```

## Usage

### Selecting a Google Account

1. Launch the app
2. Tap "Select Google Account"
3. Choose from the list of Google accounts already logged in on your device
4. The selected account will be displayed on the main screen

### Adding OneDrive Account

1. From the main screen, tap "Add OneDrive Account"
2. Tap "Sign In"
3. Complete the Microsoft authentication flow
4. Grant the requested permissions
5. Your OneDrive account will be connected

### Syncing Data

1. Make sure you have either a Google account or OneDrive account connected
2. Tap "Sync Data" button
3. The app will sync your data to the selected cloud storage

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
