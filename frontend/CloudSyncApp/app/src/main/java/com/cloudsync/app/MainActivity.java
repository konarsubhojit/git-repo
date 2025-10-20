package com.cloudsync.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "CloudSyncPrefs";
    private static final String KEY_GOOGLE_ACCOUNT = "google_account";
    private static final String KEY_ONEDRIVE_CONNECTED = "onedrive_connected";
    
    private TextView googleAccountStatus;
    private TextView oneDriveAccountStatus;
    private ImageView googleStatusIcon;
    private ImageView oneDriveStatusIcon;
    private MaterialButton selectGoogleAccountButton;
    private MaterialButton manageOneDriveButton;
    private MaterialButton syncButton;
    private MaterialButton configureFolderSyncButton;
    
    private SharedPreferences prefs;
    private String selectedGoogleAccount = null;
    private boolean isOneDriveConnected = false;
    
    private static final int REQUEST_FOLDER_SYNC_CONFIG = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        initializeViews();
        loadSavedState();
        setupClickListeners();
        updateUI();
    }
    
    private void initializeViews() {
        googleAccountStatus = findViewById(R.id.googleAccountStatus);
        oneDriveAccountStatus = findViewById(R.id.oneDriveAccountStatus);
        googleStatusIcon = findViewById(R.id.googleStatusIcon);
        oneDriveStatusIcon = findViewById(R.id.oneDriveStatusIcon);
        selectGoogleAccountButton = findViewById(R.id.selectGoogleAccountButton);
        manageOneDriveButton = findViewById(R.id.manageOneDriveButton);
        syncButton = findViewById(R.id.syncButton);
        configureFolderSyncButton = findViewById(R.id.configureFolderSyncButton);
    }
    
    private void loadSavedState() {
        selectedGoogleAccount = prefs.getString(KEY_GOOGLE_ACCOUNT, null);
        isOneDriveConnected = prefs.getBoolean(KEY_ONEDRIVE_CONNECTED, false);
    }
    
    private void setupClickListeners() {
        selectGoogleAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AccountSelectionActivity.class);
            startActivityForResult(intent, 100);
        });
        
        manageOneDriveButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OneDriveAuthActivity.class);
            startActivityForResult(intent, 200);
        });
        
        syncButton.setOnClickListener(v -> {
            showSyncConfirmation();
        });
        
        configureFolderSyncButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FolderSyncConfigActivity.class);
            startActivityForResult(intent, REQUEST_FOLDER_SYNC_CONFIG);
        });
    }
    
    private void updateUI() {
        // Update Google account status
        if (selectedGoogleAccount != null) {
            googleAccountStatus.setText(getString(R.string.account_selected, selectedGoogleAccount));
            googleStatusIcon.setVisibility(View.VISIBLE);
            googleStatusIcon.setImageResource(R.drawable.ic_check_circle);
            selectGoogleAccountButton.setText("Change Account");
        } else {
            googleAccountStatus.setText(getString(R.string.no_account_selected));
            googleStatusIcon.setVisibility(View.GONE);
            selectGoogleAccountButton.setText(R.string.select_google_account);
        }
        
        // Update OneDrive status
        if (isOneDriveConnected) {
            oneDriveAccountStatus.setText(R.string.onedrive_connected);
            oneDriveStatusIcon.setVisibility(View.VISIBLE);
            oneDriveStatusIcon.setImageResource(R.drawable.ic_check_circle);
            manageOneDriveButton.setText("Manage OneDrive Account");
        } else {
            oneDriveAccountStatus.setText(R.string.onedrive_not_connected);
            oneDriveStatusIcon.setVisibility(View.GONE);
            manageOneDriveButton.setText(R.string.add_onedrive_account);
        }
        
        // Enable sync button if at least one account is connected
        boolean canSync = selectedGoogleAccount != null || isOneDriveConnected;
        syncButton.setEnabled(canSync);
        syncButton.setAlpha(canSync ? 1.0f : 0.5f);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Google account selected
            if (data != null) {
                selectedGoogleAccount = data.getStringExtra("account_name");
                prefs.edit().putString(KEY_GOOGLE_ACCOUNT, selectedGoogleAccount).apply();
                showSuccessSnackbar("Google account connected successfully");
            }
        } else if (requestCode == 200 && resultCode == RESULT_OK) {
            // OneDrive authentication result
            if (data != null) {
                isOneDriveConnected = data.getBooleanExtra("connected", false);
                prefs.edit().putBoolean(KEY_ONEDRIVE_CONNECTED, isOneDriveConnected).apply();
                if (isOneDriveConnected) {
                    showSuccessSnackbar("OneDrive connected successfully");
                }
            }
        } else if (requestCode == REQUEST_FOLDER_SYNC_CONFIG && resultCode == RESULT_OK) {
            // Folder sync configuration saved
            if (data != null) {
                String localFolder = data.getStringExtra("local_folder");
                String cloudFolder = data.getStringExtra("cloud_folder");
                String syncMode = data.getStringExtra("sync_mode");
                String provider = data.getStringExtra("provider");
                int deleteDelayDays = data.getIntExtra("delete_delay_days", 0);
                
                // Save configuration (in a real app, send to backend API)
                showSuccessSnackbar("Sync configuration saved successfully");
            }
        }
        
        updateUI();
    }
    
    private void showSyncConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Sync")
                .setMessage(R.string.sync_confirmation)
                .setPositiveButton("Yes", (dialog, which) -> performSync())
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    private void performSync() {
        // Show progress
        syncButton.setEnabled(false);
        syncButton.setText(R.string.syncing);
        
        // Simulate sync operation (in real implementation, this would call the backend)
        syncButton.postDelayed(() -> {
            syncButton.setEnabled(true);
            syncButton.setText(R.string.sync_data);
            showSuccessSnackbar(getString(R.string.sync_success));
        }, 1500);
        
        // This is a placeholder for actual sync functionality
        // In a real implementation, this would:
        // 1. Use the selected Google account to authenticate with backend
        // 2. Or use OneDrive credentials to authenticate
        // 3. Upload/download data via the backend API
        // 4. Show progress and results to the user
    }
    
    private void showSuccessSnackbar(String message) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
                .setAction("OK", v -> {})
                .show();
    }
}
