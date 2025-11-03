# Cloud Sync Application

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A comprehensive full-stack solution for synchronizing mobile app data with cloud storage providers. Seamlessly backup and sync your Android device files to Google Drive and OneDrive with powerful configuration options and multiple sync modes.

## 📋 Table of Contents

- [Features](#-features)
- [Project Structure](#-project-structure)
- [Quick Start](#-quick-start)
- [Documentation](#-documentation)
- [Usage Example](#-usage-example)
- [Security](#-security-considerations)
- [Contributing](#-contributing)
- [License](#-license)

## 🏗️ Project Structure

```
.
├── backend/                    # Node.js backend server
│   ├── src/
│   │   ├── auth/              # OAuth 2.0 authentication modules
│   │   ├── services/          # Cloud storage service integrations
│   │   ├── routes/            # Express API routes
│   │   ├── middleware/        # Authentication & validation middleware
│   │   ├── models/            # Data models
│   │   └── config/            # Configuration management
│   ├── server.js              # Application entry point
│   ├── package.json           # Dependencies and scripts
│   └── README.md              # Backend-specific documentation
└── frontend/                   # Android mobile application
    ├── CloudSyncApp/          # Android Studio project
    │   ├── app/src/main/
    │   │   ├── java/          # Java source code
    │   │   └── res/           # Android resources
    │   └── build.gradle       # Build configuration
    └── README.md              # Frontend-specific documentation
```

## ✨ Features

### 🔐 Authentication & Security
- **OAuth 2.0 Integration**: Secure authentication with Google and Microsoft accounts
- **Session Management**: Robust session-based authentication
- **Multi-Account Support**: Manage multiple cloud provider accounts
- **Secure Token Storage**: Android Keystore integration for sensitive data

### ☁️ Cloud Storage Support
- **Google Drive**: Full OAuth 2.0 integration with file operations
- **OneDrive**: Microsoft Graph API integration for file management
- **Folder Browsing**: Intuitive folder navigation for both providers
- **Multi-Provider Sync**: Switch between providers seamlessly

### 📁 Folder Synchronization
- **Multiple Sync Configurations**: Create up to 10 independent folder sync pairs
- **5 Sync Modes**:
  - 🔼 **Upload Only**: Backup local files to cloud
  - 🔼🗑️ **Upload then Delete**: Archive files and free up local storage
  - 🔽 **Download Only**: Sync cloud files to device
  - 🔽🗑️ **Download then Delete**: Move files from cloud to local
  - 🔄 **Two-Way Sync**: Bidirectional synchronization
- **Configurable Delete Delays**: Set deletion delays from 0-30 days
- **Folder Pickers**: Browse both local and cloud folders easily

### 📱 Android Application
- **Material Design 3**: Modern, intuitive user interface
- **Account Selector**: Choose from device Google accounts
- **Configuration Manager**: Easy-to-use sync configuration interface
- **Real-Time Status**: View sync status and configuration details
- **Enable/Disable Configs**: Toggle sync configurations on/off

### 🔧 Backend API
- **RESTful Architecture**: Clean, well-documented API endpoints
- **CORS Support**: Cross-origin requests for mobile integration
- **Error Handling**: Comprehensive error responses
- **File Operations**: Upload, download, list, and delete operations

## 🚀 Quick Start

### Prerequisites
- **Backend**: Node.js v14+ and npm
- **Frontend**: Android Studio Arctic Fox+ with SDK 24+ (Android 7.0+)
- **Cloud Accounts**: Google and/or Microsoft developer accounts

### Backend Setup (5 minutes)

1. **Navigate to backend and install dependencies:**
   ```bash
   cd backend
   npm install
   ```

2. **Configure environment variables:**
   ```bash
   cp .env.example .env
   # Edit .env with your OAuth credentials
   ```

3. **Set up OAuth credentials** (see [QUICKSTART.md](QUICKSTART.md) for detailed steps):
   - [Google Cloud Console](https://console.cloud.google.com/) - Get Client ID and Secret
   - [Azure Portal](https://portal.azure.com/) - Get Application ID and Secret

4. **Start the server:**
   ```bash
   npm start
   # Server will run on http://localhost:3000
   ```

📖 **Detailed backend guide:** [backend/README.md](backend/README.md)

### Frontend Setup (10 minutes)

1. **Open project in Android Studio:**
   - Open `frontend/CloudSyncApp` folder

2. **Configure backend URL** in `app/src/main/res/values/strings.xml`:
   ```xml
   <!-- For emulator -->
   <string name="backend_url">http://10.0.2.2:3000</string>
   
   <!-- For physical device -->
   <string name="backend_url">http://YOUR_COMPUTER_IP:3000</string>
   ```

3. **Configure OneDrive authentication** (Optional):
   - Update `auth_config_msal.json` with your Microsoft Client ID
   - Update `AndroidManifest.xml` with signature hash
   - See [frontend/README.md](frontend/README.md) for detailed steps

4. **Build and run:**
   - Click "Run" button in Android Studio
   - Select your device/emulator

📖 **Detailed frontend guide:** [frontend/README.md](frontend/README.md)  
📖 **Complete setup guide:** [QUICKSTART.md](QUICKSTART.md)

## 📚 Documentation

### Getting Started
- **[Quick Start Guide](QUICKSTART.md)** - Fast setup guide (15 minutes)
- **[Backend Setup](backend/README.md)** - Detailed backend configuration
- **[Frontend Setup](frontend/README.md)** - Android app setup and configuration

### API & Integration
- **[API Documentation](API_DOCUMENTATION.md)** - Complete REST API reference with examples
- **[Authentication Guide](QUICKSTART.md#get-oauth-credentials)** - OAuth 2.0 setup for Google & Microsoft

### Features & Usage
- **[Multiple Folder Bindings](MULTIPLE_FOLDER_BINDINGS.md)** - Configure up to 10 sync pairs
- **[Folder Selection Guide](FOLDER_SELECTION_GUIDE.md)** - Step-by-step user guide
- **[Folder Sync Modes](FOLDER_SYNC_FEATURE.md)** - Understanding sync modes and delete delays

### Architecture & Development
- **[Architecture Diagram](ARCHITECTURE_DIAGRAM.md)** - System design and data flows
- **[Contributing Guidelines](CONTRIBUTING.md)** - How to contribute to the project
- **[UI Improvements](UI_IMPROVEMENTS.md)** - Recent UI enhancements

### Quick References
- **[Quick Implementation Guide](QUICK_IMPLEMENTATION_GUIDE.md)** - Implementation overview
- **[Quick Reference](QUICK_REFERENCE.md)** - Commands and common tasks

## 💡 Usage Example

### Android App Workflow

1. **Launch and Select Account**
   - Open the app and tap "Select Google Account"
   - Choose from device accounts or "Add OneDrive Account"

2. **Configure Folder Sync**
   - Tap "Manage Folder Sync" from main screen
   - Press the "+" button to create a new configuration
   - Browse and select local folder
   - Browse and select cloud folder
   - Choose sync mode (Upload Only, Two-Way, etc.)
   - Set delete delay if applicable
   - Save configuration

3. **Manage Sync Configurations**
   - View all configurations (up to 10 maximum)
   - Enable/disable configurations as needed
   - Delete unwanted configurations
   - Monitor sync status

4. **Execute Sync**
   - Configurations can be synced individually or in batch
   - View sync results and any errors
   - Check last sync timestamp

### Backend API Usage

```javascript
// Example: Upload a file via the API
const response = await fetch('http://localhost:3000/api/sync/upload', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  credentials: 'include',
  body: JSON.stringify({
    filename: 'app_data.json',
    content: JSON.stringify({ data: 'example' })
  })
});

// Example: List all files
const files = await fetch('http://localhost:3000/api/sync/list', {
  credentials: 'include'
});
```

📖 **More examples:** [API_DOCUMENTATION.md](API_DOCUMENTATION.md#mobile-app-integration-example)

## 🔒 Security Considerations

### Authentication & Authorization
- ✅ **OAuth 2.0 Protocol**: Industry-standard authentication for both Google and Microsoft
- ✅ **Session-Based Auth**: Secure session management with Express
- ✅ **Token Security**: Access tokens stored securely, never exposed to clients

### Data Protection
- ✅ **Android Keystore**: Sensitive data stored in Android Keystore
- ✅ **HTTPS Required**: Always use HTTPS in production environments
- ✅ **Environment Variables**: Never commit secrets to version control
- ✅ **CORS Configuration**: Properly configured cross-origin policies

- **[API Documentation](API_DOCUMENTATION.md)**: Complete API reference
- **[CI/CD Documentation](CI_CD_DOCUMENTATION.md)**: Continuous integration and artifact creation pipelines
- **[Multiple Folder Bindings](MULTIPLE_FOLDER_BINDINGS.md)**: Feature documentation for multi-folder sync
- **[Folder Selection Guide](FOLDER_SELECTION_GUIDE.md)**: Step-by-step user guide
- **[Architecture Diagram](ARCHITECTURE_DIAGRAM.md)**: System architecture and data flows
- **[Folder Sync Feature](FOLDER_SYNC_FEATURE.md)**: Original folder sync documentation
- **[Quick Start Guide](QUICKSTART.md)**: Get started quickly
- **[Contributing](CONTRIBUTING.md)**: Contribution guidelines

### Important Notes
⚠️ **Never commit `.env` files or API keys to version control**  
⚠️ **Use signed release builds for production Android apps**  
⚠️ **Enable ProGuard/R8 for code obfuscation**

## 🤝 Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for:
- Development setup guide
- Code style guidelines
- Pull request process
- Testing requirements

## 📞 Support

- **Documentation**: Browse our comprehensive [documentation](#-documentation)
- **Issues**: Report bugs or request features via [GitHub Issues](../../issues)
- **Discussions**: Join the conversation in [GitHub Discussions](../../discussions)

## 🙏 Acknowledgments

- **Google Drive API** for cloud storage integration
- **Microsoft Graph API** for OneDrive support
- **Android Community** for Material Design components
- **Open Source Contributors** for various dependencies

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">
  <strong>Built with ❤️ for seamless cloud synchronization</strong>
  <br>
  <sub>Supporting Google Drive and OneDrive</sub>
</div>
