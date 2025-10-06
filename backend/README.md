# Cloud Sync Application - Backend

A Node.js backend application that enables syncing data from mobile apps to cloud storage providers (Google Drive and OneDrive).

## Features

- **Google Drive Integration**: Login with Google account and sync data to Google Drive
- **OneDrive Integration**: Login with Microsoft account and sync data to OneDrive
- **Multi-provider Support**: Choose between Google Drive or OneDrive for data storage
- **RESTful API**: Easy-to-use API endpoints for mobile app integration
- **Secure Authentication**: OAuth 2.0 authentication for both providers

## Prerequisites

- Node.js (v14 or higher)
- NPM or Yarn package manager
- Google Cloud Console project (for Google Drive API)
- Microsoft Azure App Registration (for OneDrive API)

## Setup

1. Install dependencies:
```bash
npm install
```

2. Copy `.env.example` to `.env` and configure your environment variables:
```bash
cp .env.example .env
```

3. Set up OAuth credentials:
   - Google Cloud Console: https://console.cloud.google.com/
   - Microsoft Azure Portal: https://portal.azure.com/

4. Start the server:
```bash
npm start
```

Or for development with auto-reload:
```bash
npm run dev
```

## API Endpoints

### Authentication

- `GET /auth/google` - Initiate Google OAuth flow
- `GET /auth/google/callback` - Google OAuth callback
- `GET /auth/microsoft` - Initiate Microsoft OAuth flow
- `GET /auth/microsoft/callback` - Microsoft OAuth callback
- `GET /auth/logout` - Logout user
- `GET /auth/status` - Check authentication status

### Data Sync

- `POST /api/sync/upload` - Upload data to cloud storage
- `GET /api/sync/download` - Download data from cloud storage
- `GET /api/sync/list` - List files in cloud storage
- `DELETE /api/sync/delete/:fileId` - Delete file from cloud storage

## Project Structure

```
backend/
├── src/
│   ├── auth/
│   │   ├── google.js          # Google OAuth implementation
│   │   └── microsoft.js       # Microsoft OAuth implementation
│   ├── services/
│   │   ├── googleDrive.js     # Google Drive operations
│   │   └── oneDrive.js        # OneDrive operations
│   ├── routes/
│   │   ├── auth.js            # Authentication routes
│   │   └── sync.js            # Sync routes
│   ├── middleware/
│   │   └── auth.js            # Authentication middleware
│   └── config/
│       └── config.js          # Configuration management
├── server.js                   # Application entry point
├── package.json
└── README.md
```

## Security Considerations

- All authentication uses OAuth 2.0 protocol
- Access tokens are stored securely and never exposed to clients
- HTTPS should be used in production
- Environment variables should never be committed to version control

## License

MIT
