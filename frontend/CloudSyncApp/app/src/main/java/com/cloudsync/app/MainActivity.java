package com.cloudsync.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "CloudSyncPrefs";
    private static final String KEY_GOOGLE_ACCOUNT = "google_account";
    private static final String KEY_ONEDRIVE_CONNECTED = "onedrive_connected";
    
    private TextView googleAccountStatus;
    private TextView oneDriveAccountStatus;
    private MaterialButton selectGoogleAccountButton;
    private MaterialButton manageOneDriveButton;
    private MaterialButton syncButton;
    
    private SharedPreferences prefs;
    private String selectedGoogleAccount = null;
    private boolean isOneDriveConnected = false;

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
        selectGoogleAccountButton = findViewById(R.id.selectGoogleAccountButton);
        manageOneDriveButton = findViewById(R.id.manageOneDriveButton);
        syncButton = findViewById(R.id.syncButton);
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
            performSync();
        });
    }
    
    private void updateUI() {
        // Update Google account status
        if (selectedGoogleAccount != null) {
            googleAccountStatus.setText(getString(R.string.account_selected, selectedGoogleAccount));
        } else {
            googleAccountStatus.setText("No account selected");
        }
        
        // Update OneDrive status
        if (isOneDriveConnected) {
            oneDriveAccountStatus.setText(R.string.onedrive_connected);
            manageOneDriveButton.setText("Manage OneDrive Account");
        } else {
            oneDriveAccountStatus.setText(R.string.onedrive_not_connected);
            manageOneDriveButton.setText(R.string.add_onedrive_account);
        }
        
        // Enable sync button if at least one account is connected
        syncButton.setEnabled(selectedGoogleAccount != null || isOneDriveConnected);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Google account selected
            if (data != null) {
                selectedGoogleAccount = data.getStringExtra("account_name");
                prefs.edit().putString(KEY_GOOGLE_ACCOUNT, selectedGoogleAccount).apply();
                Toast.makeText(this, "Google account selected: " + selectedGoogleAccount, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 200 && resultCode == RESULT_OK) {
            // OneDrive authentication result
            if (data != null) {
                isOneDriveConnected = data.getBooleanExtra("connected", false);
                prefs.edit().putBoolean(KEY_ONEDRIVE_CONNECTED, isOneDriveConnected).apply();
                if (isOneDriveConnected) {
                    Toast.makeText(this, "OneDrive account connected", Toast.LENGTH_SHORT).show();
                }
            }
        }
        
        updateUI();
    }
    
    private void performSync() {
        // This is a placeholder for actual sync functionality
        Toast.makeText(this, "Sync functionality will be implemented here", Toast.LENGTH_SHORT).show();
        
        // In a real implementation, this would:
        // 1. Use the selected Google account to authenticate with backend
        // 2. Or use OneDrive credentials to authenticate
        // 3. Upload/download data via the backend API
        // 4. Show progress and results to the user
    }
}
