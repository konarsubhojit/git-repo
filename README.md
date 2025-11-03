# Cloud Sync Application

A full-stack application that enables syncing data from mobile apps to cloud storage providers (Google Drive and OneDrive).

## Project Structure

```
.
├── backend/                    # Node.js backend server
│   ├── src/
│   │   ├── auth/              # OAuth authentication
│   │   ├── services/          # Cloud storage services
│   │   ├── routes/            # API routes
│   │   ├── middleware/        # Express middleware
│   │   └── config/            # Configuration
│   ├── server.js              # Application entry point
│   ├── package.json
│   └── README.md
└── frontend/                   # Android mobile app
    └── CloudSyncApp/          # Android project
```

## Features

### Backend
- **Google Drive Integration**: OAuth 2.0 authentication and file operations
- **OneDrive Integration**: OAuth 2.0 authentication and file operations
- **RESTful API**: Easy-to-use API endpoints for mobile app integration
- **Secure Authentication**: Session-based authentication with OAuth 2.0
- **Folder Browsing API**: Browse cloud storage folders for easy selection

### Frontend (Android)
- **Google Account Selection**: Select from already logged-in Google accounts on device
- **OneDrive Account Management**: Add and manage OneDrive accounts
- **Multiple Folder Sync (NEW)**: Configure up to 10 folder pair synchronizations
- **Local Folder Picker**: Browse and select local device folders
- **Cloud Folder Picker**: Browse and select cloud storage folders (Google Drive/OneDrive)
- **Sync Configuration Management**: Create, view, enable/disable, and delete sync configurations
- **5 Sync Modes**: Upload Only, Upload then Delete, Download Only, Download then Delete, Two-Way Sync
- **Material Design**: Modern Android UI with Material Design components

## Quick Start

### Backend Setup

1. Navigate to backend folder:
```bash
cd backend
```

2. Install dependencies:
```bash
npm install
```

3. Copy `.env.example` to `.env` and configure:
```bash
cp .env.example .env
```

4. Start the server:
```bash
npm start
```

See [backend/README.md](backend/README.md) for detailed backend documentation.

### Frontend Setup

1. Open the `frontend/CloudSyncApp` folder in Android Studio

2. Configure the backend URL in `app/src/main/res/values/strings.xml`

3. Build and run the app on an Android device or emulator

See [frontend/README.md](frontend/README.md) for detailed Android app documentation.

## Documentation

- **[API Documentation](API_DOCUMENTATION.md)**: Complete API reference
- **[CI/CD Documentation](CI_CD_DOCUMENTATION.md)**: Continuous integration and artifact creation pipelines
- **[Multiple Folder Bindings](MULTIPLE_FOLDER_BINDINGS.md)**: Feature documentation for multi-folder sync
- **[Folder Selection Guide](FOLDER_SELECTION_GUIDE.md)**: Step-by-step user guide
- **[Architecture Diagram](ARCHITECTURE_DIAGRAM.md)**: System architecture and data flows
- **[Folder Sync Feature](FOLDER_SYNC_FEATURE.md)**: Original folder sync documentation
- **[Quick Start Guide](QUICKSTART.md)**: Get started quickly
- **[Contributing](CONTRIBUTING.md)**: Contribution guidelines

## Usage Example

### From Android App

The Android app provides a user-friendly interface to:
1. Select a Google account from device accounts
2. Add OneDrive accounts
3. Upload and download data to/from cloud storage
4. Manage synced files

### From Backend API

See [backend/README.md](backend/README.md) for API usage examples.

## Security Considerations

- All authentication uses OAuth 2.0 protocol
- Access tokens are stored securely
- HTTPS should be used in production
- Environment variables should never be committed to version control

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for contribution guidelines.

## License

MIT
