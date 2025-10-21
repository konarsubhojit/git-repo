# ⚡ Quick Start Guide

Get the Cloud Sync Application up and running in **15 minutes**! This guide covers both the Node.js backend server and Android mobile app.

## 📋 Prerequisites

### Backend Requirements
- ✅ Node.js v14 or higher ([Download](https://nodejs.org/))
- ✅ npm (comes with Node.js) or yarn
- ✅ Git for cloning the repository
- ✅ Text editor (VS Code, Sublime, etc.)

### Frontend Requirements
- ✅ Android Studio Arctic Fox or later ([Download](https://developer.android.com/studio))
- ✅ Android SDK 24+ (Android 7.0+)
- ✅ Java Development Kit (JDK) 8 or higher
- ✅ An Android device or emulator

### Cloud Provider Accounts (One or Both)
- 🔐 Google account with [Google Cloud Console](https://console.cloud.google.com/) access
- 🔐 Microsoft account with [Azure Portal](https://portal.azure.com/) access

## 🖥️ Backend Setup (5 minutes)

### Step 1: Clone and Install

```bash
# Clone the repository
git clone https://github.com/konarsubhojit/git-repo.git
cd git-repo/backend

# Install dependencies
npm install
```

### Step 2: Configure Environment

```bash
# Copy environment template
cp .env.example .env
```

### Step 3: Get OAuth Credentials

#### For Google Drive Integration:

1. **Go to [Google Cloud Console](https://console.cloud.google.com/)**
2. **Create a new project** or select existing one
3. **Enable Google Drive API:**
   - Go to "APIs & Services" → "Library"
   - Search for "Google Drive API"
   - Click "Enable"
4. **Create OAuth 2.0 credentials:**
   - Go to "APIs & Services" → "Credentials"
   - Click "Create Credentials" → "OAuth 2.0 Client ID"
   - Select "Web application"
   - Add authorized redirect URI: `http://localhost:3000/auth/google/callback`
5. **Copy credentials to `.env`:**
   ```env
   GOOGLE_CLIENT_ID=your_client_id_here
   GOOGLE_CLIENT_SECRET=your_client_secret_here
   GOOGLE_CALLBACK_URL=http://localhost:3000/auth/google/callback
   ```

#### For OneDrive Integration:

1. **Go to [Azure Portal](https://portal.azure.com/)**
2. **Register a new app:**
   - Navigate to "Azure Active Directory" → "App registrations"
   - Click "New registration"
   - Name: "Cloud Sync App"
   - Supported account types: "Accounts in any organizational directory and personal Microsoft accounts"
   - Redirect URI: `http://localhost:3000/auth/microsoft/callback`
3. **Create client secret:**
   - Go to "Certificates & secrets"
   - Click "New client secret"
   - Copy the secret value (you can only see it once!)
4. **Add API permissions:**
   - Go to "API permissions"
   - Click "Add a permission" → "Microsoft Graph"
   - Select "Delegated permissions"
   - Add: `User.Read` and `Files.ReadWrite`
   - Click "Grant admin consent"
5. **Copy credentials to `.env`:**
   ```env
   MICROSOFT_CLIENT_ID=your_application_id_here
   MICROSOFT_CLIENT_SECRET=your_client_secret_here
   MICROSOFT_CALLBACK_URL=http://localhost:3000/auth/microsoft/callback
   ```

### Step 4: Configure Session Secret

Add a secure random string for session encryption:
```env
SESSION_SECRET=your_random_secret_string_here
```

**Generate a secure secret:**
```bash
# On Linux/Mac
openssl rand -base64 32

# On Windows (PowerShell)
[Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Minimum 0 -Maximum 256 }))
```

### Step 5: Start the Server

```bash
# Production mode
npm start

# Development mode (with auto-reload)
npm run dev
```

✅ **Backend is ready!** Server running at `http://localhost:3000`

**Test it:** Open `http://localhost:3000/auth/status` in your browser

## 📱 Frontend Setup (10 minutes)

### Step 1: Open in Android Studio

1. **Launch Android Studio**
2. **Click "Open"** (or File → Open)
3. **Navigate to** `frontend/CloudSyncApp`
4. **Click "OK"**
5. **Wait** for Gradle sync to complete

### Step 2: Configure Backend URL

Edit `app/src/main/res/values/strings.xml`:

```xml
<resources>
    <!-- For Android Emulator -->
    <string name="backend_url">http://10.0.2.2:3000</string>
    <string name="backend_api_url">http://10.0.2.2:3000/api</string>
    
    <!-- For Physical Device (replace with your computer's IP) -->
    <!--
    <string name="backend_url">http://192.168.1.100:3000</string>
    <string name="backend_api_url">http://192.168.1.100:3000/api</string>
    -->
    
    <!-- Other strings... -->
</resources>
```

**💡 To find your computer's IP address:**

**Windows:**
```cmd
ipconfig
```
Look for "IPv4 Address" under your active network adapter

**Mac/Linux:**
```bash
ifconfig | grep "inet "
# or
ip addr show
```

### Step 3: Configure OneDrive Authentication (Optional)

⚠️ **Skip this step if you only need Google Drive support**

#### 3.1 Get App Signature Hash

Run this command to get your debug keystore signature:

**Mac/Linux:**
```bash
keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore | openssl sha1 -binary | openssl base64
```

**Windows:**
```cmd
keytool -exportcert -alias androiddebugkey -keystore %USERPROFILE%\.android\debug.keystore | openssl sha1 -binary | openssl base64
```

**Default keystore password:** `android`

Copy the output (your signature hash)

#### 3.2 Update MSAL Configuration

Edit `app/src/main/res/raw/auth_config_msal.json`:

```json
{
  "client_id": "YOUR_MICROSOFT_CLIENT_ID",
  "authorization_user_agent": "DEFAULT",
  "redirect_uri": "msauth://com.cloudsync.app/YOUR_SIGNATURE_HASH",
  "authorities": [
    {
      "type": "AAD",
      "audience": {
        "type": "AzureADandPersonalMicrosoftAccount",
        "tenant_id": "common"
      }
    }
  ]
}
```

Replace:
- `YOUR_MICROSOFT_CLIENT_ID` with your Azure app client ID
- `YOUR_SIGNATURE_HASH` with the hash from step 3.1

#### 3.3 Update AndroidManifest.xml

Edit `app/src/main/AndroidManifest.xml`, find the `BrowserTabActivity` and update:

```xml
<activity
    android:name="com.microsoft.identity.client.BrowserTabActivity">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
            android:scheme="msauth"
            android:host="com.cloudsync.app"
            android:path="/YOUR_SIGNATURE_HASH" />
    </intent-filter>
</activity>
```

Replace `YOUR_SIGNATURE_HASH` with your signature hash

#### 3.4 Add Redirect URI in Azure Portal

1. Go back to [Azure Portal](https://portal.azure.com/)
2. Navigate to your app registration
3. Click "Authentication" → "Add a platform" → "Android"
4. Enter:
   - **Package name:** `com.cloudsync.app`
   - **Signature hash:** Your hash from step 3.1
5. Click "Configure"

### Step 4: Build and Run

1. **Connect a device** or **start an emulator**
   - For emulator: AVD Manager → Create/Start virtual device
   - For device: Enable USB debugging in Developer Options

2. **Select device** from the device dropdown

3. **Click Run** (▶️ button) or press `Shift + F10`

4. **Grant permissions** when prompted:
   - Internet access (automatic)
   - Storage access (for local folder browsing)
   - Account access (for Google account selection)

5. **Wait for build** to complete and app to install

✅ **Frontend is ready!** The app should launch on your device

### Step 5: Verify Connection

1. **Check backend connection:**
   - The app should connect to the backend
   - No "Connection Error" messages should appear

2. **Test authentication:**
   - Try "Select Google Account" (should show device accounts)
   - Try "Add OneDrive Account" if configured (should open Microsoft login)

## 🎯 Using the App

### First Time Setup

1. **Select Cloud Account:**
   - **For Google Drive:** Tap "Select Google Account" → Choose account from list
   - **For OneDrive:** Tap "Add OneDrive Account" → Sign in with Microsoft

2. **Create a Sync Configuration:**
   - Tap "Manage Folder Sync" button
   - Tap the "+" button to add new configuration
   - Browse and select local folder on your device
   - Select cloud provider (Google Drive or OneDrive)
   - Browse and select cloud folder
   - Choose sync mode:
     - 🔼 **Upload Only** - Backup to cloud
     - 🔼🗑️ **Upload then Delete** - Archive and free space
     - 🔽 **Download Only** - Get files from cloud
     - 🔽🗑️ **Download then Delete** - Move from cloud
     - 🔄 **Two-Way Sync** - Keep in sync
   - Set delete delay (0-30 days) if applicable
   - Save configuration

3. **Start Syncing:**
   - Return to main screen
   - Tap "Execute Sync" to start synchronization
   - View sync progress and results

### Managing Multiple Configurations

- **View All:** Tap "Manage Folder Sync" to see all configurations (max 10)
- **Enable/Disable:** Toggle configurations on/off without deleting
- **Delete:** Long press or tap options menu to delete
- **Add More:** Add up to 10 independent sync configurations

## 🔧 Troubleshooting

### Backend Issues

**❌ Backend won't start**
- ✅ Check OAuth credentials in `.env` file
- ✅ Verify port 3000 is not in use: `lsof -i :3000` (Mac/Linux) or `netstat -ano | findstr :3000` (Windows)
- ✅ Check for Node.js errors in console
- ✅ Ensure all dependencies installed: `npm install`

**❌ OAuth redirect fails**
- ✅ Verify callback URLs match in Google/Azure console
- ✅ Check that `SESSION_SECRET` is set in `.env`
- ✅ Clear browser cookies and try again

### Frontend Issues

**❌ Can't connect to backend from app**
- ✅ **For emulator:** Use `http://10.0.2.2:3000`
- ✅ **For physical device:** 
  - Use your computer's IP (e.g., `http://192.168.1.100:3000`)
  - Ensure device and computer are on same network
- ✅ Verify backend is running
- ✅ Check firewall/antivirus isn't blocking connection
- ✅ Try `ping` from device to computer IP

**❌ Google account selection not working**
- ✅ Grant GET_ACCOUNTS permission when prompted
- ✅ Check at least one Google account exists on device
- ✅ Go to Settings → Apps → Cloud Sync → Permissions → Enable Contacts
- ✅ On Android 6.0+, manually grant permission in app settings

**❌ OneDrive authentication fails**
- ✅ Verify MSAL configuration in `auth_config_msal.json`
- ✅ Check signature hash matches in all 3 locations:
  - `auth_config_msal.json`
  - `AndroidManifest.xml`
  - Azure Portal redirect URI
- ✅ Ensure redirect URI added in Azure Portal
- ✅ Verify API permissions granted (User.Read, Files.ReadWrite)
- ✅ Check admin consent granted for permissions
- ✅ Clear app data and try again

**❌ Build fails**
- ✅ Check Android SDK installed (SDK 24+)
- ✅ Sync Gradle files: File → Sync Project with Gradle Files
- ✅ Clean and rebuild: Build → Clean Project, then Build → Rebuild Project
- ✅ Check for error messages in Build output

**❌ App crashes on startup**
- ✅ Check LogCat for error messages
- ✅ Verify backend URL is correct
- ✅ Ensure all required permissions granted
- ✅ Try uninstalling and reinstalling app

### Connection Testing

**Test backend manually:**
```bash
# Check if backend is running
curl http://localhost:3000/auth/status

# From Android device/emulator (using adb)
adb shell curl http://10.0.2.2:3000/auth/status
```

**Expected response:**
```json
{"authenticated":false}
```

## 📚 Next Steps

### Learn More
- **[Backend Documentation](backend/README.md)** - Detailed backend setup and API info
- **[Frontend Documentation](frontend/README.md)** - Android app architecture and features
- **[API Reference](API_DOCUMENTATION.md)** - Complete REST API documentation
- **[Multiple Folder Bindings](MULTIPLE_FOLDER_BINDINGS.md)** - Advanced sync configuration
- **[Architecture Overview](ARCHITECTURE_DIAGRAM.md)** - System design and data flows

### Explore Features
- Configure multiple folder sync pairs (up to 10)
- Try different sync modes
- Test with both Google Drive and OneDrive
- Explore folder browsing capabilities

## 🚀 Production Deployment

### Backend Production Checklist
- [ ] Use **HTTPS** with SSL certificate (Let's Encrypt, etc.)
- [ ] Create **production OAuth credentials** (separate from development)
- [ ] Set strong **SESSION_SECRET** (64+ characters)
- [ ] Configure **production domain** in callback URLs
- [ ] Set up **process manager** (PM2, systemd)
- [ ] Configure **reverse proxy** (nginx, Apache)
- [ ] Enable **logging and monitoring**
- [ ] Set up **backup strategy**
- [ ] Configure **rate limiting**
- [ ] Review **security headers**

### Android Production Checklist
- [ ] Generate **production keystore**
- [ ] Create **production OAuth credentials**
- [ ] Update **backend URL** to production server
- [ ] Enable **ProGuard/R8** code obfuscation
- [ ] Build **signed release APK/AAB**
- [ ] Test on multiple devices
- [ ] Prepare **Google Play Store** listing
- [ ] Configure **app signing** by Google Play

### Security Reminders
⚠️ **Never commit:**
- `.env` files
- Keystore files
- OAuth secrets
- API keys

⚠️ **Always use:**
- HTTPS in production
- Strong session secrets
- Minimal OAuth scopes
- Signed releases

## 💬 Support

### Getting Help
- **📖 Documentation:** Start with [README.md](README.md)
- **🐛 Bug Reports:** Open an [issue](../../issues)
- **💡 Feature Requests:** Submit an [issue](../../issues) with enhancement label
- **❓ Questions:** Check [existing issues](../../issues) or start a [discussion](../../discussions)

### Useful Resources
- [Google Drive API Documentation](https://developers.google.com/drive)
- [Microsoft Graph API Documentation](https://docs.microsoft.com/en-us/graph)
- [Android Developers Guide](https://developer.android.com)
- [Express.js Documentation](https://expressjs.com)

---

<div align="center">
  <strong>Happy Syncing! 🎉</strong>
  <br>
  <sub>If you find this project helpful, please consider giving it a ⭐</sub>
</div>
