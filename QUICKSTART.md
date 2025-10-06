# Quick Start Guide

This guide will help you quickly set up and run the Cloud Sync Application, which consists of a Node.js backend and an Android frontend.

## Prerequisites

### Backend
- Node.js (v14 or higher)
- npm or yarn

### Frontend
- Android Studio Arctic Fox or later
- Android SDK 24+ (Android 7.0+)
- An Android device or emulator

## Backend Setup (5 minutes)

1. **Navigate to backend folder**
```bash
cd backend
```

2. **Install dependencies**
```bash
npm install
```

3. **Configure environment**
```bash
cp .env.example .env
```

4. **Get OAuth credentials**

For Google Drive:
- Visit [Google Cloud Console](https://console.cloud.google.com/)
- Create a project and enable Google Drive API
- Create OAuth 2.0 credentials
- Add redirect URI: `http://localhost:3000/auth/google/callback`
- Copy Client ID and Secret to `.env`

For OneDrive:
- Visit [Azure Portal](https://portal.azure.com/)
- Register an app in Azure Active Directory
- Add redirect URI: `http://localhost:3000/auth/microsoft/callback`
- Grant permissions: `User.Read`, `Files.ReadWrite`
- Copy Application ID and Secret to `.env`

5. **Start the server**
```bash
npm start
```

The backend will be available at `http://localhost:3000`

## Frontend Setup (10 minutes)

1. **Open in Android Studio**
   - Launch Android Studio
   - Open existing project
   - Navigate to `frontend/CloudSyncApp`

2. **Configure backend URL**
   
   Edit `app/src/main/res/values/strings.xml`:
   ```xml
   <string name="backend_url">http://10.0.2.2:3000</string>
   ```
   
   Note: `10.0.2.2` is the special IP for emulator to access host localhost. For a physical device, use your computer's IP address.

3. **Configure OneDrive authentication**
   
   a. Get your app's signature hash:
   ```bash
   keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore | openssl sha1 -binary | openssl base64
   ```
   Default password: `android`
   
   b. Update `app/src/main/res/raw/auth_config_msal.json`:
   - Replace `YOUR_MICROSOFT_CLIENT_ID` with your Azure app's Client ID
   - Replace `YOUR_SIGNATURE_HASH` with the hash from step 3a
   
   c. Update `app/src/main/AndroidManifest.xml`:
   - Replace `YOUR_SIGNATURE_HASH` in BrowserTabActivity
   
   d. In Azure Portal, add Android platform redirect URI:
   - Format: `msauth://com.cloudsync.app/YOUR_SIGNATURE_HASH`

4. **Build and run**
   - Click the "Run" button (green triangle)
   - Select your device or emulator
   - Wait for app to build and install

## Using the App

### Select Google Account
1. Tap "Select Google Account"
2. Choose from available accounts on your device
3. The selected account will be displayed

### Add OneDrive Account
1. Tap "Add OneDrive Account"
2. Tap "Sign In"
3. Complete Microsoft authentication
4. Grant requested permissions

### Sync Data
1. Once an account is connected, the "Sync Data" button becomes active
2. Tap it to sync your data to cloud storage

## Troubleshooting

### Backend won't start
- Make sure OAuth credentials are correctly configured in `.env`
- Check that port 3000 is not already in use

### Can't connect to backend from app
- For emulator: Use `http://10.0.2.2:3000`
- For physical device: Use your computer's IP (e.g., `http://192.168.1.100:3000`)
- Make sure backend is running
- Check firewall settings

### Google account selection not working
- Grant GET_ACCOUNTS permission when prompted
- Make sure you have at least one Google account on your device

### OneDrive authentication fails
- Verify MSAL configuration is correct
- Check signature hash matches in all locations
- Ensure redirect URI is added in Azure Portal
- Verify API permissions are granted

## Next Steps

- See [backend/README.md](backend/README.md) for detailed backend documentation
- See [frontend/README.md](frontend/README.md) for detailed Android app documentation
- See [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for API reference

## Production Deployment

For production deployment:
- Use HTTPS for the backend
- Use proper OAuth credentials (not development ones)
- Enable ProGuard for the Android app
- Use a signed release APK
- Store secrets securely

## Support

For issues or questions, please open an issue on GitHub.
