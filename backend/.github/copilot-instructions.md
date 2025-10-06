# GitHub Copilot Instructions - Backend (Node.js)

## Backend Overview

Node.js/Express backend server providing RESTful API for cloud synchronization with OAuth 2.0 authentication for Google Drive and OneDrive.

## Technology Stack

- **Runtime**: Node.js (v14 or higher)
- **Framework**: Express.js
- **Authentication**: Passport.js with OAuth 2.0 strategies
- **Session Management**: express-session with in-memory store
- **Cloud APIs**: 
  - Google Drive API v3 (googleapis package)
  - Microsoft Graph API (OneDrive)

## Project Structure

```
backend/
├── src/
│   ├── auth/
│   │   ├── google.js          # Google OAuth strategy and configuration
│   │   └── microsoft.js       # Microsoft OAuth strategy and configuration
│   ├── services/
│   │   ├── googleDrive.js     # Google Drive file operations
│   │   └── oneDrive.js        # OneDrive file operations
│   ├── routes/
│   │   ├── auth.js            # Authentication endpoints
│   │   └── sync.js            # File sync endpoints
│   ├── middleware/
│   │   └── auth.js            # Authentication middleware
│   └── config/
│       └── config.js          # Configuration management
├── server.js                   # Application entry point
├── package.json
├── .env.example               # Environment variables template
└── README.md
```

## Code Patterns and Conventions

### File Organization
- **Routes**: Define API endpoints and delegate to service layer
- **Services**: Contain business logic and external API interactions
- **Auth**: OAuth strategies and authentication configuration
- **Middleware**: Request processing, authentication checks, error handling
- **Config**: Environment variable management and app configuration

### Naming Conventions
- Use camelCase for variables and functions: `getUserProfile`, `accessToken`
- Use PascalCase for classes: `GoogleDriveService`
- Use UPPER_CASE for constants: `DEFAULT_TIMEOUT`, `MAX_RETRIES`
- Prefix private functions with underscore: `_validateToken`

### Error Handling
```javascript
// Always use try-catch for async operations
try {
  const result = await someAsyncOperation();
  res.json({ success: true, data: result });
} catch (error) {
  console.error('Operation failed:', error);
  res.status(500).json({ success: false, error: error.message });
}
```

### Response Format
```javascript
// Success response
res.json({
  success: true,
  data: { /* result data */ },
  message: 'Operation completed successfully'
});

// Error response
res.status(statusCode).json({
  success: false,
  error: 'Error message',
  details: 'Additional error details'
});
```

## API Endpoint Patterns

### Authentication Routes (src/routes/auth.js)
- `GET /auth/google` - Initiates Google OAuth flow
- `GET /auth/google/callback` - Handles Google OAuth callback
- `GET /auth/microsoft` - Initiates Microsoft OAuth flow
- `GET /auth/microsoft/callback` - Handles Microsoft OAuth callback
- `GET /auth/status` - Returns current authentication status
- `GET /auth/logout` - Clears session and logs out user

### Sync Routes (src/routes/sync.js)
- `POST /api/sync/upload` - Upload file to cloud storage
- `GET /api/sync/download` - Download file from cloud storage
- `GET /api/sync/list` - List files in cloud storage
- `DELETE /api/sync/delete/:fileId` - Delete file from cloud storage

All sync endpoints require authentication via middleware.

## OAuth 2.0 Implementation

### Google OAuth Setup
1. Configure strategy in `src/auth/google.js`
2. Use `passport-google-oauth20` package
3. Scopes required: `profile`, `email`, `https://www.googleapis.com/auth/drive.file`
4. Store tokens in session for API calls

### Microsoft OAuth Setup
1. Configure strategy in `src/auth/microsoft.js`
2. Use `passport-microsoft` or `passport-azure-ad` package
3. Scopes required: `User.Read`, `Files.ReadWrite`
4. Store tokens in session for API calls

### Session Management
```javascript
// Configure session with secure settings
app.use(session({
  secret: process.env.SESSION_SECRET,
  resave: false,
  saveUninitialized: false,
  cookie: { 
    secure: process.env.NODE_ENV === 'production', // HTTPS only in production
    httpOnly: true,
    maxAge: 24 * 60 * 60 * 1000 // 24 hours
  }
}));
```

## Cloud Storage Service Patterns

### Google Drive Service (src/services/googleDrive.js)
```javascript
class GoogleDriveService {
  constructor(accessToken) {
    this.auth = new google.auth.OAuth2();
    this.auth.setCredentials({ access_token: accessToken });
    this.drive = google.drive({ version: 'v3', auth: this.auth });
  }

  async uploadFile(fileName, content) {
    // Implementation for uploading files
  }

  async downloadFile(fileId) {
    // Implementation for downloading files
  }

  async listFiles() {
    // Implementation for listing files
  }

  async deleteFile(fileId) {
    // Implementation for deleting files
  }
}
```

### OneDrive Service (src/services/oneDrive.js)
- Use Microsoft Graph API endpoints
- Similar structure to GoogleDriveService
- Base URL: `https://graph.microsoft.com/v1.0/me/drive`

## Environment Variables

Required in `.env`:
```bash
PORT=3000
SESSION_SECRET=your-session-secret-here

# Google OAuth
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
GOOGLE_CALLBACK_URL=http://localhost:3000/auth/google/callback

# Microsoft OAuth
MICROSOFT_CLIENT_ID=your-microsoft-client-id
MICROSOFT_CLIENT_SECRET=your-microsoft-client-secret
MICROSOFT_CALLBACK_URL=http://localhost:3000/auth/microsoft/callback

# Frontend URL (for CORS)
FRONTEND_URL=http://localhost:3000
```

## Middleware Patterns

### Authentication Middleware
```javascript
function ensureAuthenticated(req, res, next) {
  if (req.isAuthenticated()) {
    return next();
  }
  res.status(401).json({ 
    success: false, 
    error: 'Authentication required' 
  });
}
```

### CORS Configuration
```javascript
const cors = require('cors');
app.use(cors({
  origin: process.env.FRONTEND_URL || 'http://localhost:3000',
  credentials: true
}));
```

## Development Commands

```bash
# Install dependencies
npm install

# Start server (production)
npm start

# Start server with auto-reload (development)
npm run dev

# Run tests (if implemented)
npm test
```

## Common Tasks

### Adding a New OAuth Provider
1. Install passport strategy package: `npm install passport-<provider>`
2. Create auth configuration in `src/auth/<provider>.js`
3. Add service implementation in `src/services/<provider>.js`
4. Register routes in `src/routes/auth.js`
5. Update environment variables in `.env.example`
6. Document in README and API_DOCUMENTATION.md

### Adding a New API Endpoint
1. Define route in appropriate file under `src/routes/`
2. Implement business logic in service layer
3. Add authentication middleware if needed
4. Document in `API_DOCUMENTATION.md`
5. Test endpoint with Postman or curl

### Modifying File Operations
1. Update service method in `src/services/googleDrive.js` or `src/services/oneDrive.js`
2. Ensure error handling is comprehensive
3. Update route handler if API contract changes
4. Test with both providers

## Security Best Practices

1. **Never commit secrets**: Use `.env` for all sensitive data
2. **Validate input**: Always validate and sanitize user input
3. **Rate limiting**: Consider adding rate limiting middleware
4. **Token refresh**: Implement OAuth token refresh logic
5. **HTTPS**: Use HTTPS in production (set `cookie.secure: true`)
6. **Helmet.js**: Consider adding helmet for security headers
7. **Input validation**: Use joi or express-validator for request validation

## Testing

### Manual Testing with cURL
```bash
# Test authentication status
curl http://localhost:3000/auth/status

# Test file upload (requires authentication)
curl -X POST http://localhost:3000/api/sync/upload \
  -H "Content-Type: application/json" \
  -d '{"filename": "test.txt", "content": "Hello World"}'
```

### Session Testing
- Use Postman with cookie persistence
- Test OAuth flow end-to-end
- Verify session persistence across requests

## Error Handling Patterns

```javascript
// Async route handler wrapper
const asyncHandler = (fn) => (req, res, next) => {
  Promise.resolve(fn(req, res, next)).catch(next);
};

// Usage
router.post('/upload', ensureAuthenticated, asyncHandler(async (req, res) => {
  const result = await uploadService.upload(req.body);
  res.json({ success: true, data: result });
}));

// Global error handler
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(err.status || 500).json({
    success: false,
    error: err.message || 'Internal server error'
  });
});
```

## Logging Best Practices

```javascript
// Use consistent logging
console.log('[AUTH] User authenticated:', user.id);
console.error('[ERROR] Failed to upload file:', error.message);
console.warn('[WARNING] Token expires soon:', expiryTime);

// Consider using a logging library like winston or pino in production
```

## Performance Considerations

1. **Caching**: Consider caching frequently accessed files
2. **Streaming**: Use streaming for large file uploads/downloads
3. **Connection pooling**: Reuse HTTP connections when possible
4. **Async/await**: Always use async/await for I/O operations
5. **Compression**: Enable gzip compression for responses

## References

- Express.js documentation: https://expressjs.com/
- Passport.js documentation: http://www.passportjs.org/
- Google Drive API: https://developers.google.com/drive/api/v3/about-sdk
- Microsoft Graph API: https://docs.microsoft.com/en-us/graph/overview
- Node.js best practices: https://github.com/goldbergyoni/nodebestpractices
