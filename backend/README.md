# Cloud Sync Application - Backend

A robust Node.js backend server that provides RESTful API for synchronizing mobile app data with Google Drive and OneDrive cloud storage providers.

## 📋 Table of Contents

- [Features](#-features)
- [Prerequisites](#-prerequisites)
- [Setup](#-setup)
- [API Endpoints](#-api-endpoints)
- [Project Structure](#-project-structure)
- [Environment Variables](#-environment-variables)
- [Development](#-development)
- [Security](#-security-considerations)
- [Troubleshooting](#-troubleshooting)
- [License](#-license)

## ✨ Features

### Authentication & Security
- 🔐 **OAuth 2.0 Integration**: Secure authentication with Google and Microsoft
- 🔒 **Session Management**: Express session-based authentication
- 🛡️ **CORS Support**: Configurable cross-origin resource sharing
- 🔑 **Token Management**: Automatic token refresh and validation

### Cloud Storage Operations
- ☁️ **Google Drive API**: Full integration for file operations
- ☁️ **OneDrive API**: Microsoft Graph API integration
- 📁 **Folder Management**: Create, browse, and navigate cloud folders
- 📂 **File Operations**: Upload, download, list, and delete files

### Sync Configuration
- ⚙️ **Multiple Configurations**: Support up to 10 sync configurations per user
- 🔄 **Five Sync Modes**: Upload Only, Upload then Delete, Download Only, Download then Delete, Two-Way Sync
- ⏱️ **Delete Delays**: Configurable deletion delays (0-30 days)
- 📊 **Configuration Management**: CRUD operations for sync settings

### API Features
- 🌐 **RESTful Architecture**: Clean, intuitive API design
- 📝 **Comprehensive Endpoints**: Full set of operations for mobile integration
- ⚡ **Efficient Processing**: Optimized for mobile network conditions
- 📋 **Error Handling**: Detailed error responses for debugging

## 📦 Prerequisites

### Required Software
- ✅ **Node.js** v14.0.0 or higher ([Download](https://nodejs.org/))
- ✅ **npm** (comes with Node.js) or **yarn**
- ✅ **Git** for version control

### Cloud Provider Setup (One or Both)
- 🌐 **Google Cloud Console** project with Drive API enabled
  - [Google Cloud Console](https://console.cloud.google.com/)
  - OAuth 2.0 Client ID and Secret
- 🌐 **Microsoft Azure** App Registration
  - [Azure Portal](https://portal.azure.com/)
  - Application (Client) ID and Secret
  - Microsoft Graph API permissions

### Development Tools (Recommended)
- 📝 **VS Code** or your preferred IDE
- 🔍 **Postman** or **curl** for API testing
- 🐛 **Node Inspector** for debugging

## 🚀 Setup

### Step 1: Install Dependencies

```bash
# Navigate to backend directory
cd backend

# Install packages
npm install

# Expected output: 
# added XXX packages, and audited XXX packages in Xs
```

### Step 2: Configure Environment

```bash
# Copy environment template
cp .env.example .env

# Edit .env file with your credentials
# Use your preferred text editor: nano, vim, or code
code .env  # Opens in VS Code
```

### Step 3: Configure OAuth Credentials

See the [QUICKSTART.md](../QUICKSTART.md) guide for detailed instructions on:
- Setting up Google Cloud Console project
- Registering Azure App
- Getting Client IDs and Secrets

**Quick reference - Required `.env` variables:**
```env
# Server Configuration
PORT=3000
SESSION_SECRET=your_secure_random_string_here

# Google OAuth
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
GOOGLE_CALLBACK_URL=http://localhost:3000/auth/google/callback

# Microsoft OAuth
MICROSOFT_CLIENT_ID=your_microsoft_client_id
MICROSOFT_CLIENT_SECRET=your_microsoft_client_secret
MICROSOFT_CALLBACK_URL=http://localhost:3000/auth/microsoft/callback
```

### Step 4: Start the Server

```bash
# Production mode
npm start

# Development mode (with auto-reload using nodemon)
npm run dev
```

**Expected output:**
```
Server is running on port 3000
```

### Step 5: Verify Installation

Test the server is running:
```bash
# In a new terminal
curl http://localhost:3000/auth/status

# Expected response:
# {"authenticated":false}
```

Or open in browser: `http://localhost:3000/auth/status`

## 🌐 API Endpoints

### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/auth/google` | Initiate Google OAuth flow | No |
| `GET` | `/auth/google/callback` | Google OAuth callback | No |
| `GET` | `/auth/microsoft` | Initiate Microsoft OAuth flow | No |
| `GET` | `/auth/microsoft/callback` | Microsoft OAuth callback | No |
| `GET` | `/auth/status` | Check authentication status | No |
| `GET` | `/auth/logout` | Logout current user | Yes |

### File Operations

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/api/sync/upload` | Upload file to cloud storage | Yes |
| `GET` | `/api/sync/download` | Download file from cloud | Yes |
| `GET` | `/api/sync/list` | List all files in cloud | Yes |
| `DELETE` | `/api/sync/delete/:fileId` | Delete file from cloud | Yes |

### Folder Operations

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/api/sync/folders/list` | List folders in cloud | Yes |
| `POST` | `/api/sync/folder/upload` | Upload file to specific folder | Yes |
| `GET` | `/api/sync/folder/list` | List files in folder | Yes |

### Sync Configuration

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/api/sync-config` | Get all sync configurations | Yes |
| `POST` | `/api/sync-config` | Create new sync configuration | Yes |
| `PUT` | `/api/sync-config/:configId` | Update sync configuration | Yes |
| `DELETE` | `/api/sync-config/:configId` | Delete sync configuration | Yes |
| `POST` | `/api/sync/execute/:configId` | Execute sync for configuration | Yes |

📖 **Detailed API documentation:** [API_DOCUMENTATION.md](../API_DOCUMENTATION.md)

## 📁 Project Structure

```
backend/
├── src/                           # Source code directory
│   ├── auth/                      # OAuth authentication modules
│   │   ├── google.js              # Google OAuth 2.0 strategy & config
│   │   └── microsoft.js           # Microsoft OAuth 2.0 strategy & config
│   ├── services/                  # Cloud storage service layers
│   │   ├── googleDrive.js         # Google Drive API operations
│   │   └── oneDrive.js            # OneDrive/Graph API operations
│   ├── routes/                    # Express route handlers
│   │   ├── auth.js                # Authentication routes
│   │   ├── sync.js                # File sync routes
│   │   └── syncConfig.js          # Sync configuration routes
│   ├── middleware/                # Express middleware
│   │   └── auth.js                # Authentication middleware
│   ├── models/                    # Data models
│   │   └── syncConfig.js          # Sync configuration model
│   └── config/                    # Configuration management
│       └── config.js              # Environment config loader
├── server.js                      # Application entry point
├── package.json                   # Dependencies and scripts
├── .env.example                   # Environment variables template
├── .gitignore                     # Git ignore rules
└── README.md                      # This file
```

### Key Files Explained

- **`server.js`**: Main application entry point, sets up Express app, middleware, and routes
- **`auth/*.js`**: Passport.js strategies for OAuth authentication
- **`services/*.js`**: Business logic for interacting with cloud storage APIs
- **`routes/*.js`**: API endpoint definitions and request handling
- **`middleware/auth.js`**: Authentication check middleware for protected routes
- **`models/*.js`**: Data structures and in-memory storage (can be replaced with database)

## 🔐 Environment Variables

### Complete `.env` Template

```env
# Server Configuration
PORT=3000
NODE_ENV=development

# Session Configuration
SESSION_SECRET=your_long_random_secret_string_here

# Google OAuth Credentials
GOOGLE_CLIENT_ID=your_google_client_id_from_cloud_console
GOOGLE_CLIENT_SECRET=your_google_client_secret_from_cloud_console
GOOGLE_CALLBACK_URL=http://localhost:3000/auth/google/callback

# Microsoft OAuth Credentials
MICROSOFT_CLIENT_ID=your_microsoft_app_client_id_from_azure
MICROSOFT_CLIENT_SECRET=your_microsoft_app_client_secret_from_azure
MICROSOFT_CALLBACK_URL=http://localhost:3000/auth/microsoft/callback

# CORS Configuration (optional)
ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8080
```

### Environment Variables Reference

| Variable | Required | Description | Example |
|----------|----------|-------------|---------|
| `PORT` | No | Server port number | `3000` |
| `NODE_ENV` | No | Environment mode | `development` or `production` |
| `SESSION_SECRET` | **Yes** | Secret for session encryption | Random 64-char string |
| `GOOGLE_CLIENT_ID` | No* | Google OAuth client ID | `123456789.apps.googleusercontent.com` |
| `GOOGLE_CLIENT_SECRET` | No* | Google OAuth secret | `GOCSPX-xxxxxxxxxxxxx` |
| `GOOGLE_CALLBACK_URL` | No* | Google OAuth callback | `http://localhost:3000/auth/google/callback` |
| `MICROSOFT_CLIENT_ID` | No* | Azure app client ID | `12345678-1234-1234-1234-123456789012` |
| `MICROSOFT_CLIENT_SECRET` | No* | Azure app secret | `xxxxxxxxxxxxxxxxxxxxx` |
| `MICROSOFT_CALLBACK_URL` | No* | Microsoft OAuth callback | `http://localhost:3000/auth/microsoft/callback` |

*At least one provider (Google or Microsoft) must be configured

## 💻 Development

### Available Scripts

```bash
# Start production server
npm start

# Start development server with auto-reload
npm run dev

# Run tests (if implemented)
npm test
```

### Development Workflow

1. **Make code changes** in `src/` directory
2. **Server auto-reloads** (when using `npm run dev`)
3. **Test endpoints** using Postman, curl, or browser
4. **Check console** for errors or logs
5. **Commit changes** when tests pass

### Testing API Endpoints

#### Using curl

```bash
# Check server status
curl http://localhost:3000/auth/status

# Initiate Google OAuth (opens in browser)
curl http://localhost:3000/auth/google

# Test authenticated endpoint (after logging in)
curl http://localhost:3000/api/sync/list \
  --cookie "connect.sid=your_session_cookie"
```

#### Using Postman

1. Import the API collection (if available)
2. Test authentication flow
3. Save session cookies
4. Test protected endpoints

### Debugging

Enable Node.js inspector:
```bash
node --inspect server.js

# Or with nodemon
nodemon --inspect server.js
```

Then attach debugger from VS Code or Chrome DevTools.

### Adding New Features

1. **Create route handler** in `src/routes/`
2. **Add business logic** in `src/services/` if needed
3. **Update documentation** in relevant README files
4. **Test thoroughly** before committing

## 🔒 Security Considerations

### Authentication & Authorization
- ✅ OAuth 2.0 protocol for all authentication
- ✅ Access tokens stored securely in session
- ✅ Tokens never exposed to clients
- ✅ Session-based authentication with secure cookies
- ✅ HTTPS required for production

### Data Protection
- ✅ Environment variables for sensitive data
- ✅ Never commit `.env` files to version control
- ✅ Use strong `SESSION_SECRET` (64+ characters)
- ✅ Validate and sanitize all user inputs
- ✅ CORS properly configured

### Best Practices
- 🔐 Rotate OAuth credentials regularly
- 🔐 Use separate credentials for dev/prod
- 🔐 Enable HTTPS in production
- 🔐 Implement rate limiting (recommended)
- 🔐 Keep dependencies updated
- 🔐 Review security audits: `npm audit`

### Security Headers (Recommended for Production)

Install and configure helmet:
```bash
npm install helmet
```

```javascript
// In server.js
const helmet = require('helmet');
app.use(helmet());
```

## 🔧 Troubleshooting

### Server Won't Start

**Problem:** `Error: listen EADDRINUSE: address already in use :::3000`

**Solution:**
```bash
# Find process using port 3000
# Mac/Linux:
lsof -i :3000
kill -9 <PID>

# Windows:
netstat -ano | findstr :3000
taskkill /PID <PID> /F
```

---

**Problem:** `Error: Cannot find module 'express'`

**Solution:**
```bash
# Reinstall dependencies
rm -rf node_modules package-lock.json
npm install
```

### OAuth Errors

**Problem:** `Error: redirect_uri_mismatch`

**Solution:**
- Check callback URL in `.env` matches exactly with Google/Azure console
- Ensure `http://` or `https://` is included
- Verify port number is correct
- Check for trailing slashes

---

**Problem:** `Error: invalid_client`

**Solution:**
- Verify Client ID and Secret are correct
- Check for extra spaces in `.env` file
- Ensure credentials are for correct environment

### API Errors

**Problem:** `401 Unauthorized`

**Solution:**
- User not authenticated - complete OAuth flow first
- Session expired - log in again
- Cookie not sent - check `credentials: 'include'` in fetch

---

**Problem:** `500 Internal Server Error`

**Solution:**
- Check server console for detailed error
- Verify cloud provider API tokens are valid
- Check internet connectivity
- Review API rate limits

### Google Drive API Issues

**Problem:** `403 Forbidden - Daily Limit Exceeded`

**Solution:**
- Check quota in Google Cloud Console
- Request quota increase if needed
- Implement caching to reduce API calls

---

**Problem:** `The API is not enabled`

**Solution:**
```
1. Go to Google Cloud Console
2. Navigate to "APIs & Services" → "Library"
3. Search for "Google Drive API"
4. Click "Enable"
```

### Microsoft Graph API Issues

**Problem:** `Authorization_RequestDenied`

**Solution:**
- Check API permissions in Azure Portal
- Ensure admin consent granted
- Verify scope includes `Files.ReadWrite`

### Common Fixes

```bash
# Clear node modules and reinstall
rm -rf node_modules package-lock.json
npm install

# Check Node.js version
node --version  # Should be v14+

# Verify environment variables loaded
node -e "require('dotenv').config(); console.log(process.env.PORT)"

# Test with minimal setup
curl -v http://localhost:3000/auth/status
```

## 📚 Additional Resources

### Documentation
- [Main README](../README.md) - Project overview
- [API Documentation](../API_DOCUMENTATION.md) - Complete API reference
- [Frontend README](../frontend/README.md) - Android app documentation
- [Quick Start Guide](../QUICKSTART.md) - Fast setup guide

### External Resources
- [Express.js Documentation](https://expressjs.com/)
- [Passport.js Documentation](http://www.passportjs.org/)
- [Google Drive API](https://developers.google.com/drive/api/v3/about-sdk)
- [Microsoft Graph API](https://docs.microsoft.com/en-us/graph/overview)

### Community & Support
- [GitHub Issues](../../issues) - Bug reports and feature requests
- [GitHub Discussions](../../discussions) - Questions and discussions
- [Contributing Guide](../CONTRIBUTING.md) - How to contribute

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](../LICENSE) file for details.

---

<div align="center">
  <strong>Backend API Server</strong>
  <br>
  <sub>Part of the Cloud Sync Application</sub>
</div>
