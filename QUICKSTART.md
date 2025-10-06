# Quick Start Guide

This guide will help you get the Cloud Sync Application up and running in minutes.

## Step 1: Install Dependencies

```bash
npm install
```

## Step 2: Set Up Environment Variables

Copy the example environment file:

```bash
cp .env.example .env
```

Edit `.env` with your credentials (see below for how to get them).

## Step 3: Get OAuth Credentials

### For Google Drive:

1. Visit [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project
3. Enable "Google Drive API"
4. Go to Credentials → Create OAuth 2.0 Client ID
5. Add redirect URI: `http://localhost:3000/auth/google/callback`
6. Copy Client ID and Secret to `.env`

### For Microsoft OneDrive:

1. Visit [Azure Portal](https://portal.azure.com/)
2. Go to Azure Active Directory → App registrations
3. Create new registration
4. Add redirect URI: `http://localhost:3000/auth/microsoft/callback`
5. Create client secret under "Certificates & secrets"
6. Add API permissions: User.Read, Files.ReadWrite
7. Copy Application ID and Secret to `.env`

## Step 4: Start the Server

```bash
npm start
```

The server will start on `http://localhost:3000`

## Step 5: Test the API

### Test with Browser:

1. Open browser and go to `http://localhost:3000`
2. You should see the API information page
3. Visit `http://localhost:3000/auth/google` to test Google login
4. Visit `http://localhost:3000/auth/microsoft` to test Microsoft login

### Test with cURL:

```bash
# Check server status
curl http://localhost:3000/

# Check auth status
curl http://localhost:3000/auth/status
```

## Step 6: Integrate with Your Mobile App

Use the API endpoints documented in `API_DOCUMENTATION.md` to integrate with your mobile app.

Example workflow:
1. User authenticates via OAuth (opens web view)
2. Mobile app uploads data via `/api/sync/upload`
3. Data is stored in Google Drive or OneDrive
4. Mobile app can download data via `/api/sync/download`

## Troubleshooting

### "Client ID not configured"
- Make sure you've set up `.env` with your OAuth credentials

### "Redirect URI mismatch"
- Ensure redirect URIs match exactly in your OAuth app settings

### "Permission denied"
- Check that you've enabled the required APIs and permissions

## Next Steps

- Read the full [README.md](README.md) for detailed information
- Check [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for complete API reference
- See [CONTRIBUTING.md](CONTRIBUTING.md) if you want to contribute

## Support

If you encounter issues, please open an issue on GitHub.
