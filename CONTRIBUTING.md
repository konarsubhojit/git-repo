# Contributing to Cloud Sync Application

Thank you for your interest in contributing to the Cloud Sync Application! We appreciate your help in making this project better.

## 📋 Table of Contents

- [Getting Started](#getting-started)
- [Development Setup](#development-setup)
- [Code Style Guidelines](#code-style-guidelines)
- [Testing Requirements](#testing-requirements)
- [Pull Request Process](#pull-request-process)
- [Questions?](#questions)

## Getting Started

### Fork and Clone

1. **Fork the repository** to your GitHub account
2. **Clone your fork:**
   ```bash
   git clone https://github.com/your-username/git-repo.git
   cd git-repo
   ```

3. **Add upstream remote:**
   ```bash
   git remote add upstream https://github.com/konarsubhojit/git-repo.git
   ```

4. **Create a feature branch:**
   ```bash
   git checkout -b feature/your-feature-name
   # or
   git checkout -b fix/your-bug-fix
   ```

### Branch Naming Convention

- `feature/description` - For new features
- `fix/description` - For bug fixes
- `docs/description` - For documentation updates
- `refactor/description` - For code refactoring
- `test/description` - For adding tests

## Development Setup

### Backend Development

1. **Navigate to backend directory:**
   ```bash
   cd backend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Configure environment:**
   ```bash
   cp .env.example .env
   # Edit .env with your development credentials
   ```

4. **Set up OAuth credentials:**
   - [Google Cloud Console](https://console.cloud.google.com/)
   - [Microsoft Azure Portal](https://portal.azure.com/)
   - See [QUICKSTART.md](QUICKSTART.md) for detailed steps

5. **Start development server with auto-reload:**
   ```bash
   npm run dev
   ```

### Frontend Development

1. **Open Android Studio:**
   - Open the `frontend/CloudSyncApp` folder

2. **Configure backend URL:**
   - Edit `app/src/main/res/values/strings.xml`
   - Use `http://10.0.2.2:3000` for emulator

3. **Sync Gradle files:**
   - Click "Sync Now" when prompted

4. **Run the app:**
   - Select a device/emulator
   - Click the Run button (▶️)

### Keeping Your Fork Updated

```bash
git fetch upstream
git checkout main
git merge upstream/main
git push origin main
```

## Code Style Guidelines

### JavaScript/Node.js (Backend)

- **Indentation:** 2 spaces (no tabs)
- **Quotes:** Single quotes for strings
- **Semicolons:** Use semicolons
- **Naming:**
  - `camelCase` for variables and functions
  - `PascalCase` for classes
  - `UPPER_CASE` for constants
- **Comments:** JSDoc for functions, inline for complex logic
- **Async/Await:** Prefer over callbacks/promises.then()

**Example:**
```javascript
/**
 * Upload a file to cloud storage
 * @param {string} filename - Name of the file
 * @param {string} content - File content
 * @returns {Promise<object>} Upload result
 */
async function uploadFile(filename, content) {
  try {
    const result = await cloudService.upload(filename, content);
    return result;
  } catch (error) {
    logger.error('Upload failed:', error);
    throw error;
  }
}
```

### Java/Android (Frontend)

- **Indentation:** 4 spaces
- **Naming:**
  - `camelCase` for methods and variables
  - `PascalCase` for classes
  - `UPPER_CASE` for constants
- **Comments:** JavaDoc for public methods
- **Layout Files:** Use meaningful IDs and follow Material Design guidelines
- **Resources:** Use descriptive resource names

**Example:**
```java
/**
 * Browse cloud storage folders
 * @param provider Cloud provider (google or microsoft)
 * @param folderPath Current folder path
 */
private void browseCloudFolder(String provider, String folderPath) {
    // Implementation here
}
```

### General Principles

- ✅ Follow existing patterns in the codebase
- ✅ Keep functions small and focused
- ✅ Write self-documenting code
- ✅ Add comments only when necessary
- ✅ Remove commented-out code
- ✅ Use meaningful variable names

## Testing Requirements

### Before Submitting a Pull Request

#### Backend Testing
- [ ] Test all affected API endpoints using Postman or curl
- [ ] Verify authentication flows (Google and/or Microsoft)
- [ ] Test error handling and edge cases
- [ ] Ensure no syntax errors (`npm start` runs without errors)
- [ ] Check console for warnings or errors
- [ ] Test with both cloud providers if applicable

**Example manual test:**
```bash
# Start the server
npm start

# In another terminal, test an endpoint
curl -X GET http://localhost:3000/auth/status \
  --cookie "session_cookie_here"
```

#### Frontend Testing
- [ ] Test on both emulator and physical device if possible
- [ ] Verify UI renders correctly on different screen sizes
- [ ] Test all user flows affected by your changes
- [ ] Ensure no build errors or warnings
- [ ] Test with different Android versions (SDK 24+)
- [ ] Verify Material Design guidelines are followed

#### Integration Testing
- [ ] Test backend-frontend communication
- [ ] Verify OAuth flows work end-to-end
- [ ] Test file upload/download operations
- [ ] Verify sync configurations persist correctly

### Test Checklist for Common Changes

**Adding a new API endpoint:**
- [ ] Test with valid authentication
- [ ] Test without authentication (should fail)
- [ ] Test with invalid parameters
- [ ] Test error responses
- [ ] Update API documentation

**Modifying the UI:**
- [ ] Test on different screen orientations
- [ ] Test with different system themes (light/dark)
- [ ] Verify accessibility
- [ ] Check for proper error messages
- [ ] Ensure consistent styling

## Pull Request Process

### 1. Prepare Your Changes

Before creating a PR:
```bash
# Make sure you're on your feature branch
git checkout feature/your-feature-name

# Commit your changes
git add .
git commit -m "Clear, descriptive commit message"

# Update from upstream
git fetch upstream
git rebase upstream/main

# Push to your fork
git push origin feature/your-feature-name
```

### 2. Create the Pull Request

1. **Go to the original repository** on GitHub
2. **Click "New Pull Request"**
3. **Select your fork and branch**
4. **Fill out the PR template:**

#### PR Title Format
- `feat: Add new sync mode for selective sync`
- `fix: Resolve OAuth token refresh issue`
- `docs: Update API documentation for folder endpoints`
- `refactor: Improve error handling in cloud services`

#### PR Description Template
```markdown
## Description
Brief description of what this PR does

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Documentation update
- [ ] Code refactoring
- [ ] Other (please describe)

## Changes Made
- Change 1
- Change 2
- Change 3

## Testing Done
- How you tested these changes
- Devices/environments tested on

## Related Issues
Fixes #123
Relates to #456

## Screenshots (if applicable)
Add screenshots for UI changes

## Checklist
- [ ] Code follows project style guidelines
- [ ] Self-review completed
- [ ] Comments added for complex code
- [ ] Documentation updated
- [ ] No new warnings or errors
- [ ] Tested on relevant platforms
```

### 3. PR Guidelines

✅ **Do:**
- Keep PRs focused on a single feature or fix
- Write clear, descriptive commit messages
- Update documentation for your changes
- Add comments for complex logic
- Reference related issues
- Respond to review feedback promptly
- Keep PRs reasonably sized (< 500 lines when possible)

❌ **Don't:**
- Mix multiple unrelated changes
- Include unnecessary formatting changes
- Commit sensitive data (API keys, tokens)
- Include commented-out code
- Submit untested code
- Ignore build/test failures

### 4. Review Process

1. **Automated Checks**: Wait for any CI checks to pass
2. **Code Review**: Address reviewer feedback
3. **Approval**: Wait for maintainer approval
4. **Merge**: Maintainer will merge your PR

### 5. After Your PR is Merged

```bash
# Switch back to main
git checkout main

# Pull the latest changes
git pull upstream main

# Delete your feature branch (optional)
git branch -d feature/your-feature-name
git push origin --delete feature/your-feature-name
```

## Questions?

### Getting Help

- **Documentation**: Check the [README](README.md) and related docs first
- **Issues**: Search [existing issues](../../issues) for similar questions
- **Discussions**: Start a [discussion](../../discussions) for general questions
- **New Issue**: Open a new issue for bugs or feature requests

### What to Include When Asking for Help

1. **Clear description** of the problem
2. **Steps to reproduce** (if applicable)
3. **Expected vs actual behavior**
4. **Environment details:**
   - Node.js version (for backend issues)
   - Android Studio version (for frontend issues)
   - Operating system
5. **Error messages** or logs
6. **Screenshots** (if applicable)

### Communication Guidelines

- Be respectful and constructive
- Provide context and details
- Stay on topic
- Follow up on your questions/issues

---

Thank you for contributing! 🎉
