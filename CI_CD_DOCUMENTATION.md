# CI/CD Pipelines Documentation

This repository uses GitHub Actions for Continuous Integration (CI) and artifact creation. There are separate workflows for the backend (Node.js) and frontend (Android) components.

## Workflows Overview

### 1. Backend CI (`backend-ci.yml`)

**Triggers:**
- Push to `main` or `develop` branches (when backend files change)
- Pull requests to `main` or `develop` branches (when backend files change)
- Manual workflow dispatch

**Jobs:**

#### Build and Test
- **Matrix Testing:** Runs on Node.js versions 18.x and 20.x (currently supported LTS versions)
- **Steps:**
  - Checkout code
  - Setup Node.js with npm caching
  - Install dependencies with `npm ci`
  - Run tests with `npm test`
  - Check for security vulnerabilities with `npm audit`

#### Create Artifact
- **Runs after:** Successful build and test
- **Condition:** Only on push events or manual dispatch
- **Steps:**
  - Install production dependencies only
  - Create optimized artifact directory
  - Upload artifact with 30-day retention
  - For tagged releases: Create zip file with 90-day retention

**Artifacts Created:**
- `backend-{commit-sha}` - Regular build artifact
- `backend-release-{tag}` - Release artifact (only for tagged commits)

### 2. Android CI (`android-ci.yml`)

**Triggers:**
- Push to `main` or `develop` branches (when frontend files change)
- Pull requests to `main` or `develop` branches (when frontend files change)
- Manual workflow dispatch

**Jobs:**

#### Build and Test
- **Environment:** Ubuntu with JDK 17
- **Steps:**
  - Checkout code
  - Setup JDK 17 (Temurin distribution) with Gradle caching
  - Grant execute permission to gradlew
  - Build with Gradle
  - Run unit tests
  - Upload test reports (always, even on failure)

#### Build APK
- **Runs after:** Successful build and test
- **Condition:** Only on push events or manual dispatch
- **Steps:**
  - Build debug APK
  - Build release APK (unsigned)
  - Upload both APK artifacts with 30-day retention

#### Build Bundle
- **Runs after:** Successful build and test
- **Condition:** Only on push events or manual dispatch
- **Steps:**
  - Build release AAB (Android App Bundle)
  - Upload AAB artifact with 30-day retention
  - For tagged releases: Create named release artifacts with 90-day retention

**Artifacts Created:**
- `app-debug-{commit-sha}` - Debug APK
- `app-release-{commit-sha}` - Release APK (unsigned)
- `app-bundle-{commit-sha}` - App Bundle (AAB)
- `android-test-reports-{commit-sha}` - Test reports (7-day retention)
- `android-release-{tag}` - Tagged release artifacts (only for tagged commits)

## Usage

### Viewing Artifacts

1. Go to the **Actions** tab in the GitHub repository
2. Click on a workflow run
3. Scroll down to the **Artifacts** section
4. Download the desired artifact

### Running Workflows Manually

1. Go to the **Actions** tab
2. Select the desired workflow (Backend CI or Android CI)
3. Click **Run workflow**
4. Select the branch and click **Run workflow**

### Creating Release Artifacts

To create release artifacts with extended retention (90 days):

1. Create and push a git tag:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

2. The workflows will automatically:
   - Build the artifacts
   - Create specially named release artifacts
   - Store them with 90-day retention

## Artifact Retention

- **Regular artifacts:** 30 days
- **Test reports:** 7 days
- **Release artifacts (tagged):** 90 days

## Caching Strategy

### Backend
- **npm packages:** Cached using `actions/setup-node` with `cache: 'npm'`
- **Cache key:** Based on `package-lock.json`

### Android
- **Gradle wrapper and packages:** Cached using `actions/cache`
- **Cache key:** Based on `*.gradle*` and `gradle-wrapper.properties` files
- **JDK:** Cached by `actions/setup-java`

## Optimization Features

### Backend
- Uses `npm ci` for faster, reproducible installs
- Production-only dependencies for artifacts (`--omit=dev`)
- Removes cache directories from artifacts
- High compression level (9) for artifacts

### Android
- Gradle caching for faster builds
- Separate jobs for APK and AAB to parallelize builds
- Test reports uploaded even on failure for debugging

## Path Filtering

Both workflows use path filtering to only run when relevant files change:

**Backend:** Triggers only when files in `backend/` or the workflow file itself change

**Frontend:** Triggers only when files in `frontend/` or the workflow file itself change

This prevents unnecessary workflow runs and saves CI/CD minutes.

## Security

### Backend
- Security vulnerability scanning with `npm audit`
- Audit level set to `high` (only fails on critical and high severity issues)
- Continues on error to not fail builds, but alerts will be visible in logs
- Node.js engine requirement: >=18.0.0 (currently supported LTS versions)

### Android
- Uses official Temurin JDK distribution
- Gradle wrapper validation can be added for additional security

## Troubleshooting

### Backend build fails
1. Check Node.js version compatibility in `package.json` engines
2. Review `npm audit` output for security issues
3. Ensure dependencies are properly defined in `package.json`

### Android build fails
1. Check JDK version (must be 17)
2. Review Gradle wrapper version compatibility
3. Ensure Android SDK components are available (GitHub provides them)
4. Check `build.gradle` for any local dependencies

### Artifacts not created
1. Verify the job succeeded
2. Check if the trigger condition was met (push vs PR)
3. Ensure `needs: build-and-test` job passed

## Future Enhancements

Potential improvements to consider:

1. **Code signing:** Add signing configuration for release APKs/AABs
2. **Automated deployment:** Deploy to staging/production environments
3. **Code coverage:** Add coverage reporting and badges
4. **Linting:** Add ESLint for backend and Android Lint checks
5. **Release automation:** Automatically create GitHub releases for tags
6. **Notifications:** Add Slack/email notifications for build status
7. **Performance testing:** Add performance benchmarks
8. **Docker:** Create Docker images for backend deployment

## Contributing

When modifying workflows:
1. Test locally with [act](https://github.com/nektos/act) if possible
2. Use `workflow_dispatch` to test in GitHub before merging
3. Update this documentation when adding new features
4. Follow GitHub Actions best practices
