# Cloud Sync Application

A Node.js application that enables syncing data from mobile apps to cloud storage providers (Google Drive and OneDrive).

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

1. Clone the repository:
```bash
git clone https://github.com/konarsubhojit/git-repo.git
cd git-repo
```

2. Install dependencies:
```bash
npm install
```

3. Configure environment variables:
Create a `.env` file in the root directory with the following:
```
PORT=3000
NODE_ENV=development

# Google Drive Configuration
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
GOOGLE_REDIRECT_URI=http://localhost:3000/auth/google/callback

# Microsoft OneDrive Configuration
MICROSOFT_CLIENT_ID=your_microsoft_client_id
MICROSOFT_CLIENT_SECRET=your_microsoft_client_secret
MICROSOFT_REDIRECT_URI=http://localhost:3000/auth/microsoft/callback

# Session Secret
SESSION_SECRET=your_random_session_secret
```

4. Start the server:
```bash
npm start
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

## Usage Example

### From Mobile App

```javascript
// 1. Authenticate user
const authUrl = 'http://your-server.com/auth/google'; // or /auth/microsoft
// Open in web view or browser

// 2. Upload data
const response = await fetch('http://your-server.com/api/sync/upload', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${accessToken}`
  },
  body: JSON.stringify({
    filename: 'app_data.json',
    content: JSON.stringify(yourAppData)
  })
});

// 3. Download data
const data = await fetch('http://your-server.com/api/sync/download?filename=app_data.json', {
  headers: {
    'Authorization': `Bearer ${accessToken}`
  }
});
```

## Project Structure

```
.
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
