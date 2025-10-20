# GitHub Copilot Instructions - Cloud Sync Application

## Project Overview

This is a full-stack cloud synchronization application consisting of:
- **Backend**: Node.js/Express server with OAuth 2.0 integration for Google Drive and OneDrive
- **Frontend**: Native Android application for mobile data synchronization

## General Guidelines

### Code Style
- Use consistent indentation (2 spaces for JavaScript/JSON, 4 spaces for Java/Kotlin)
- Follow existing code patterns and conventions in each component
- Add comments for complex logic and business rules
- Use meaningful variable and function names
- Keep functions small and focused on a single responsibility

### Security Best Practices
- **Never hardcode credentials** - Always use environment variables or configuration files
- **OAuth 2.0**: All authentication must use OAuth 2.0 protocol
- **HTTPS in Production**: Ensure all production deployments use HTTPS
- **Token Storage**: Store access tokens securely (never expose to clients)
- **Environment Variables**: Never commit `.env` files or secrets to version control
- **Android Keystore**: Use Android Keystore for secure token storage on mobile

### Documentation
- Update relevant README files when making significant changes
- Keep API documentation in sync with code changes
- Document setup steps for new OAuth providers
- Include troubleshooting tips for common issues

### Testing
- Test all authentication flows before committing
- Verify API endpoints work correctly
- Test error handling and edge cases
- Ensure mobile app works on both emulators and physical devices

## Project Structure

```
.
├── backend/                    # Node.js backend server
│   ├── src/
│   │   ├── auth/              # OAuth authentication implementations
│   │   ├── services/          # Cloud storage service integrations
│   │   ├── routes/            # API route handlers
│   │   ├── middleware/        # Express middleware
│   │   └── config/            # Configuration management
│   ├── server.js              # Application entry point
│   └── .env.example           # Environment variables template
└── frontend/                   # Android mobile app
    └── CloudSyncApp/          # Android Studio project
        └── app/src/main/
            ├── java/          # Java source code
            └── res/           # Android resources
```

## Key Technologies

### Backend
- **Node.js** (v14+) with Express.js framework
- **Passport.js** for OAuth 2.0 authentication
- **Google Drive API** for Google cloud storage
- **Microsoft Graph API** for OneDrive integration
- **Express Session** for session management

### Frontend
- **Android SDK 24+** (Android 7.0+)
- **Material Design 3** components
- **Google Play Services Auth** for Google account selection
- **Microsoft Authentication Library (MSAL)** for OneDrive
- **Retrofit** and **OkHttp** for HTTP networking

## Common Development Tasks

### Adding a New Cloud Provider
1. Create authentication module in `backend/src/auth/`
2. Implement service layer in `backend/src/services/`
3. Add routes in `backend/src/routes/`
4. Update Android app to support new provider
5. Document OAuth setup in README and API documentation

### Modifying API Endpoints
1. Update route handler in `backend/src/routes/`
2. Update API documentation in `API_DOCUMENTATION.md`
3. Update Android app API client if needed
4. Test all affected endpoints

### UI Changes in Android App
1. Modify layouts in `frontend/CloudSyncApp/app/src/main/res/layout/`
2. Update strings in `res/values/strings.xml`
3. Follow Material Design guidelines
4. Test on different screen sizes and Android versions

## Environment Configuration

### Backend (.env)
Required environment variables:
- `PORT`: Server port (default: 3000)
- `SESSION_SECRET`: Secret for session encryption
- `GOOGLE_CLIENT_ID`: Google OAuth client ID
- `GOOGLE_CLIENT_SECRET`: Google OAuth client secret
- `MICROSOFT_CLIENT_ID`: Microsoft OAuth client ID
- `MICROSOFT_CLIENT_SECRET`: Microsoft OAuth client secret
- Callback URLs for OAuth redirects

### Frontend (strings.xml)
- `backend_url`: Backend server URL (use `http://10.0.2.2:3000` for emulator)
- `backend_api_url`: Backend API endpoint URL

### Frontend (auth_config_msal.json)
- Microsoft client ID and authorities for MSAL

## Helpful References

- Main README: [/README.md](/README.md)
- Backend Documentation: [/backend/README.md](/backend/README.md)
- Frontend Documentation: [/frontend/README.md](/frontend/README.md)
- API Documentation: [/API_DOCUMENTATION.md](/API_DOCUMENTATION.md)
- Quick Start Guide: [/QUICKSTART.md](/QUICKSTART.md)
- Contributing Guidelines: [/CONTRIBUTING.md](/CONTRIBUTING.md)

For component-specific instructions, refer to:
- Backend: [/backend/.github/copilot-instructions.md](/backend/.github/copilot-instructions.md)
- Frontend: [/frontend/.github/copilot-instructions.md](/frontend/.github/copilot-instructions.md)
