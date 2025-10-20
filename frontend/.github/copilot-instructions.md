# GitHub Copilot Instructions - Frontend (Android)

## Android App Overview

Native Android application for cloud data synchronization supporting Google Drive and OneDrive with OAuth 2.0 authentication.

## Technology Stack

- **Language**: Java
- **Min SDK**: API 24 (Android 7.0 Nougat)
- **Target SDK**: API 33+ (Android 13+)
- **Build System**: Gradle 8.0+
- **UI Framework**: Material Design 3 (Material Components)
- **Architecture**: Activity-based with service integration
- **HTTP Client**: Retrofit 2 + OkHttp 3
- **Authentication**:
  - Google Play Services Auth for Google accounts
  - Microsoft Authentication Library (MSAL) for OneDrive

## Project Structure

```
frontend/CloudSyncApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/cloudsync/app/
│   │   │   ├── MainActivity.java              # Main screen
│   │   │   ├── AccountSelectionActivity.java  # Google account picker
│   │   │   ├── AccountAdapter.java            # RecyclerView adapter
│   │   │   └── OneDriveAuthActivity.java      # OneDrive authentication
│   │   ├── res/
│   │   │   ├── layout/                        # UI layouts (XML)
│   │   │   ├── values/                        # Resources (strings, colors, themes)
│   │   │   ├── drawable/                      # Icons and graphics
│   │   │   ├── mipmap/                        # App launcher icons
│   │   │   └── raw/
│   │   │       └── auth_config_msal.json     # MSAL configuration
│   │   └── AndroidManifest.xml               # App manifest
│   ├── build.gradle                          # App-level build configuration
│   └── proguard-rules.pro                    # ProGuard rules
├── build.gradle                              # Project-level build configuration
├── gradle.properties                         # Gradle properties
└── settings.gradle                           # Module configuration
```

## Code Patterns and Conventions

### Naming Conventions
- **Classes**: PascalCase - `MainActivity`, `AccountAdapter`
- **Methods**: camelCase - `onAccountSelected()`, `authenticateWithOneDrive()`
- **Variables**: camelCase - `accessToken`, `accountName`
- **Constants**: UPPER_CASE - `REQUEST_CODE_ACCOUNT`, `TAG`
- **Resources**: snake_case - `activity_main.xml`, `button_sync_data`
- **IDs**: snake_case - `@+id/button_google_account`, `@+id/text_view_status`

### Activity Lifecycle
```java
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize views and setup listeners
        initializeViews();
        setupClickListeners();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh UI state
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // Save state if needed
    }
}
```

### View Binding Pattern
```java
// Initialize views
private void initializeViews() {
    Button buttonGoogleAccount = findViewById(R.id.button_google_account);
    TextView textViewStatus = findViewById(R.id.text_view_status);
    // ... other views
}

// Setup click listeners
private void setupClickListeners() {
    findViewById(R.id.button_google_account).setOnClickListener(v -> 
        selectGoogleAccount()
    );
    findViewById(R.id.button_onedrive).setOnClickListener(v -> 
        authenticateWithOneDrive()
    );
}
```

## Google Account Integration

### Permissions Required
```xml
<uses-permission android:name="android.permission.GET_ACCOUNTS" />
<uses-permission android:name="android.permission.USE_CREDENTIALS" />
```

### Account Selection Pattern
```java
private static final int REQUEST_CODE_ACCOUNT = 1001;

private void selectGoogleAccount() {
    Intent intent = new Intent(this, AccountSelectionActivity.class);
    startActivityForResult(intent, REQUEST_CODE_ACCOUNT);
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_ACCOUNT && resultCode == RESULT_OK) {
        String accountName = data.getStringExtra("account_name");
        // Handle selected account
        handleAccountSelected(accountName);
    }
}

private void handleAccountSelected(String accountName) {
    // Update UI and communicate with backend
    updateAccountDisplay(accountName);
    notifyBackend(accountName);
}
```

### Getting Device Accounts
```java
// In AccountSelectionActivity
private List<String> getGoogleAccounts() {
    AccountManager accountManager = AccountManager.get(this);
    Account[] accounts = accountManager.getAccountsByType("com.google");
    List<String> accountNames = new ArrayList<>();
    for (Account account : accounts) {
        accountNames.add(account.name);
    }
    return accountNames;
}
```

## OneDrive/MSAL Integration

### MSAL Configuration (auth_config_msal.json)
```json
{
  "client_id": "YOUR_MICROSOFT_CLIENT_ID",
  "authorization_user_agent": "DEFAULT",
  "redirect_uri": "msauth://com.cloudsync.app/YOUR_SIGNATURE_HASH",
  "authorities": [
    {
      "type": "AAD",
      "audience": {
        "type": "AzureADandPersonalMicrosoftAccount"
      }
    }
  ]
}
```

### MSAL Authentication Pattern
```java
private ISingleAccountPublicClientApplication mSingleAccountApp;

private void initializeMSAL() {
    PublicClientApplication.createSingleAccountPublicClientApplication(
        getApplicationContext(),
        R.raw.auth_config_msal,
        new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
            @Override
            public void onCreated(ISingleAccountPublicClientApplication application) {
                mSingleAccountApp = application;
            }
            
            @Override
            public void onError(MsalException exception) {
                Log.e(TAG, "MSAL initialization failed", exception);
            }
        }
    );
}

private void signInWithMSAL() {
    String[] scopes = {"User.Read", "Files.ReadWrite"};
    
    mSingleAccountApp.signIn(
        this,
        null,
        scopes,
        new AuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult result) {
                String accessToken = result.getAccessToken();
                // Use access token for OneDrive operations
                handleMSALSuccess(accessToken);
            }
            
            @Override
            public void onError(MsalException exception) {
                Log.e(TAG, "MSAL sign-in failed", exception);
                showError("Authentication failed: " + exception.getMessage());
            }
            
            @Override
            public void onCancel() {
                showMessage("Sign-in cancelled");
            }
        }
    );
}
```

### AndroidManifest.xml Configuration
```xml
<!-- MSAL Redirect URI Activity -->
<activity
    android:name="com.microsoft.identity.client.BrowserTabActivity">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
            android:scheme="msauth"
            android:host="com.cloudsync.app"
            android:path="/YOUR_SIGNATURE_HASH" />
    </intent-filter>
</activity>
```

## Backend API Communication

### Retrofit Setup
```java
// API interface
public interface CloudSyncApi {
    @POST("api/sync/upload")
    Call<SyncResponse> uploadData(@Body UploadRequest request);
    
    @GET("api/sync/download")
    Call<DownloadResponse> downloadData(@Query("filename") String filename);
    
    @GET("api/sync/list")
    Call<ListResponse> listFiles();
    
    @DELETE("api/sync/delete/{fileId}")
    Call<DeleteResponse> deleteFile(@Path("fileId") String fileId);
}

// Retrofit client
private CloudSyncApi createApiClient() {
    String baseUrl = getString(R.string.backend_api_url);
    
    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(new HttpLoggingInterceptor().setLevel(
            BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE
        ))
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build();
    
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    
    return retrofit.create(CloudSyncApi.class);
}
```

### Making API Calls
```java
private void syncData() {
    showProgressDialog("Syncing...");
    
    UploadRequest request = new UploadRequest(filename, content);
    apiClient.uploadData(request).enqueue(new Callback<SyncResponse>() {
        @Override
        public void onResponse(Call<SyncResponse> call, Response<SyncResponse> response) {
            hideProgressDialog();
            if (response.isSuccessful() && response.body() != null) {
                handleSyncSuccess(response.body());
            } else {
                handleSyncError("Upload failed: " + response.code());
            }
        }
        
        @Override
        public void onFailure(Call<SyncResponse> call, Throwable t) {
            hideProgressDialog();
            handleSyncError("Network error: " + t.getMessage());
        }
    });
}
```

## UI/UX Patterns

### Material Design 3
- Use MaterialButton, MaterialCardView, MaterialTextView
- Follow Material color system (colors.xml)
- Use Material themes (themes.xml)
- Implement proper elevation and shadows

### Layouts
```xml
<!-- Use ConstraintLayout for complex layouts -->
<androidx.constraintlayout.widget.ConstraintLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">
    
    <com.google.android.material.button.MaterialButton
        android:id="@+id/button_google_account"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="@string/select_google_account"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />
        
</androidx.constraintlayout.widget.ConstraintLayout>
```

### RecyclerView Pattern (for account lists)
```java
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    private List<String> accounts;
    private OnAccountClickListener listener;
    
    public interface OnAccountClickListener {
        void onAccountClick(String accountName);
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_account, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String account = accounts.get(position);
        holder.textViewAccount.setText(account);
        holder.itemView.setOnClickListener(v -> listener.onAccountClick(account));
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAccount;
        
        ViewHolder(View itemView) {
            super(itemView);
            textViewAccount = itemView.findViewById(R.id.text_view_account);
        }
    }
}
```

## Resource Management

### Strings (res/values/strings.xml)
```xml
<resources>
    <string name="app_name">Cloud Sync</string>
    <string name="backend_url">http://10.0.2.2:3000</string>
    <string name="backend_api_url">http://10.0.2.2:3000/api</string>
    <string name="select_google_account">Select Google Account</string>
    <string name="add_onedrive">Add OneDrive Account</string>
    <string name="sync_data">Sync Data</string>
</resources>
```

### Colors (res/values/colors.xml)
```xml
<resources>
    <!-- Material Design 3 color scheme -->
    <color name="md_theme_light_primary">#6750A4</color>
    <color name="md_theme_light_onPrimary">#FFFFFF</color>
    <!-- Add other Material colors -->
</resources>
```

## Permissions and Runtime Requests

### Requesting Permissions (Android 6.0+)
```java
private static final int PERMISSION_REQUEST_GET_ACCOUNTS = 100;

private void checkAndRequestPermissions() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)
            != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.GET_ACCOUNTS},
            PERMISSION_REQUEST_GET_ACCOUNTS);
    } else {
        // Permission already granted
        loadAccounts();
    }
}

@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == PERMISSION_REQUEST_GET_ACCOUNTS) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadAccounts();
        } else {
            showPermissionDeniedMessage();
        }
    }
}
```

## Error Handling and User Feedback

### Toast Messages
```java
private void showMessage(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
}

private void showError(String error) {
    Toast.makeText(this, error, Toast.LENGTH_LONG).show();
}
```

### Progress Dialogs
```java
private ProgressDialog progressDialog;

private void showProgressDialog(String message) {
    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage(message);
    progressDialog.setCancelable(false);
    progressDialog.show();
}

private void hideProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
        progressDialog.dismiss();
    }
}
```

### Alert Dialogs
```java
private void showConfirmationDialog(String message, DialogInterface.OnClickListener onConfirm) {
    new AlertDialog.Builder(this)
        .setTitle("Confirm")
        .setMessage(message)
        .setPositiveButton("Yes", onConfirm)
        .setNegativeButton("No", null)
        .show();
}
```

## Security Best Practices

1. **Never hardcode credentials** in source code
2. **Use Android Keystore** for storing sensitive tokens
3. **Validate SSL certificates** in production
4. **Use ProGuard/R8** for code obfuscation in release builds
5. **Clear sensitive data** from memory when no longer needed
6. **Use HTTPS** for all network communication
7. **Don't log sensitive information** in production builds

### Storing Tokens Securely
```java
// Use SharedPreferences with encryption or Android Keystore
private void storeToken(String token) {
    SharedPreferences prefs = getSharedPreferences("secure_prefs", MODE_PRIVATE);
    prefs.edit().putString("access_token", token).apply();
    // Better: Use EncryptedSharedPreferences or Android Keystore
}
```

## Gradle Dependencies

### app/build.gradle
```gradle
dependencies {
    // AndroidX
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.recyclerview:recyclerview:1.3.0'
    
    // Material Design
    implementation 'com.google.android.material:material:1.9.0'
    
    // Google Play Services
    implementation 'com.google.android.gms:play-services-auth:20.6.0'
    
    // Microsoft MSAL
    implementation 'com.microsoft.identity.client:msal:4.7.0'
    
    // Retrofit and OkHttp
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.11.0'
}
```

## Testing Considerations

### Testing on Emulator
- Use `http://10.0.2.2:3000` to access host machine's localhost
- Enable Google Play Services on emulator for Google account testing
- Test MSAL flows with test Microsoft accounts

### Testing on Physical Device
- Use actual IP address of development machine (e.g., `http://192.168.1.100:3000`)
- Ensure device and development machine are on same network
- Test permissions on different Android versions

## Common Development Tasks

### Adding a New Activity
1. Create Java class extending AppCompatActivity
2. Create layout XML in res/layout/
3. Register in AndroidManifest.xml
4. Add string resources
5. Implement navigation from other activities

### Updating UI Strings
1. Edit res/values/strings.xml
2. Use `@string/resource_name` in layouts and code
3. Never hardcode strings in Java code

### Changing Backend URL
1. Update backend_url and backend_api_url in res/values/strings.xml
2. For emulator: use `http://10.0.2.2:3000`
3. For physical device: use computer's IP address

### Updating MSAL Configuration
1. Get signature hash: `keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore | openssl sha1 -binary | openssl base64`
2. Update res/raw/auth_config_msal.json with client_id and redirect_uri
3. Update AndroidManifest.xml BrowserTabActivity data path
4. Configure redirect URI in Azure Portal

## Build and Release

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```
Note: Configure signing config in build.gradle for release builds

### ProGuard Configuration
Edit proguard-rules.pro to keep necessary classes:
```proguard
# Keep MSAL classes
-keep class com.microsoft.identity.** { *; }

# Keep Retrofit classes
-keepattributes Signature
-keepattributes Annotations
-keep class retrofit2.** { *; }
```

## Logging Best Practices

```java
private static final String TAG = "MainActivity";

// Use appropriate log levels
Log.d(TAG, "Debug message");      // Development only
Log.i(TAG, "Info message");       // General information
Log.w(TAG, "Warning message");    // Warning conditions
Log.e(TAG, "Error message");      // Error conditions

// Remove debug logs in production using BuildConfig
if (BuildConfig.DEBUG) {
    Log.d(TAG, "Sensitive debug info");
}
```

## References

- Android Developer Guide: https://developer.android.com/guide
- Material Design 3: https://m3.material.io/
- Google Play Services Auth: https://developers.google.com/android/guides/setup
- MSAL for Android: https://github.com/AzureAD/microsoft-authentication-library-for-android
- Retrofit: https://square.github.io/retrofit/
- Android Best Practices: https://developer.android.com/topic/architecture
