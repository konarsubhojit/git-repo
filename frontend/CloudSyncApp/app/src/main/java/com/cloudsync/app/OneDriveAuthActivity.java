package com.cloudsync.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;

public class OneDriveAuthActivity extends AppCompatActivity {
    private static final String TAG = "OneDriveAuthActivity";
    private static final String[] SCOPES = {"User.Read", "Files.ReadWrite"};
    
    private ISingleAccountPublicClientApplication msalApp;
    private TextView statusText;
    private ImageView statusIcon;
    private ProgressBar authProgress;
    private MaterialButton signInButton;
    private MaterialButton signOutButton;
    
    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onedrive_auth);
        
        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        statusText = findViewById(R.id.statusText);
        statusIcon = findViewById(R.id.statusIcon);
        authProgress = findViewById(R.id.authProgress);
        signInButton = findViewById(R.id.signInButton);
        signOutButton = findViewById(R.id.signOutButton);
        
        initializeMSAL();
        setupClickListeners();
    }
    
    private void initializeMSAL() {
        showLoading(true);
        PublicClientApplication.createSingleAccountPublicClientApplication(
                getApplicationContext(),
                R.raw.auth_config_msal,
                new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application) {
                        msalApp = application;
                        loadAccount();
                    }

                    @Override
                    public void onError(MsalException exception) {
                        Log.e(TAG, "MSAL initialization failed", exception);
                        showLoading(false);
                        showErrorSnackbar("Failed to initialize OneDrive authentication: " + exception.getMessage());
                    }
                });
    }
    
    private void setupClickListeners() {
        signInButton.setOnClickListener(v -> signIn());
        signOutButton.setOnClickListener(v -> signOut());
    }
    
    private void loadAccount() {
        if (msalApp == null) {
            showLoading(false);
            return;
        }
        
        msalApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
            @Override
            public void onAccountLoaded(@Nullable IAccount account) {
                showLoading(false);
                if (account != null) {
                    isConnected = true;
                    updateUI(account);
                } else {
                    isConnected = false;
                    updateUI(null);
                }
            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount, @Nullable IAccount currentAccount) {
                showLoading(false);
                if (currentAccount != null) {
                    isConnected = true;
                    updateUI(currentAccount);
                } else {
                    isConnected = false;
                    updateUI(null);
                }
            }

            @Override
            public void onError(@NonNull MsalException exception) {
                Log.e(TAG, "Error loading account", exception);
                showLoading(false);
                isConnected = false;
                updateUI(null);
            }
        });
    }
    
    private void signIn() {
        if (msalApp == null) {
            showErrorSnackbar("Authentication not initialized");
            return;
        }
        
        showLoading(true);
        signInButton.setEnabled(false);
        
        msalApp.signIn(this, "", SCOPES, new AuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                showLoading(false);
                signInButton.setEnabled(true);
                isConnected = true;
                IAccount account = authenticationResult.getAccount();
                updateUI(account);
                showSuccessSnackbar("Successfully signed in to OneDrive");
            }

            @Override
            public void onError(MsalException exception) {
                Log.e(TAG, "Sign in failed", exception);
                showLoading(false);
                signInButton.setEnabled(true);
                isConnected = false;
                updateUI(null);
                showErrorSnackbar("Sign in failed: " + exception.getMessage());
            }

            @Override
            public void onCancel() {
                showLoading(false);
                signInButton.setEnabled(true);
                Toast.makeText(OneDriveAuthActivity.this,
                        "Sign in cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void signOut() {
        if (msalApp == null) return;
        
        showLoading(true);
        signOutButton.setEnabled(false);
        
        msalApp.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
            @Override
            public void onSignOut() {
                showLoading(false);
                signOutButton.setEnabled(true);
                isConnected = false;
                updateUI(null);
                showSuccessSnackbar("Signed out successfully");
            }

            @Override
            public void onError(@NonNull MsalException exception) {
                Log.e(TAG, "Sign out failed", exception);
                showLoading(false);
                signOutButton.setEnabled(true);
                showErrorSnackbar("Sign out failed: " + exception.getMessage());
            }
        });
    }
    
    private void showLoading(boolean show) {
        runOnUiThread(() -> {
            authProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            statusIcon.setVisibility(show ? View.GONE : View.VISIBLE);
        });
    }
    
    private void updateUI(IAccount account) {
        runOnUiThread(() -> {
            if (account != null) {
                statusText.setText(getString(R.string.onedrive_connected) + "\n" + account.getUsername());
                statusIcon.setImageResource(R.drawable.ic_check_circle);
                signInButton.setVisibility(View.GONE);
                signOutButton.setVisibility(View.VISIBLE);
            } else {
                statusText.setText(R.string.onedrive_not_connected);
                statusIcon.setImageResource(R.drawable.ic_error);
                signInButton.setVisibility(View.VISIBLE);
                signOutButton.setVisibility(View.GONE);
            }
        });
    }
    
    private void showSuccessSnackbar(String message) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
                .setAction("OK", v -> {})
                .show();
    }
    
    private void showErrorSnackbar(String message) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(android.R.color.holo_red_dark))
                .setAction("OK", v -> {})
                .show();
    }
    
    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("connected", isConnected);
        setResult(RESULT_OK, resultIntent);
        super.onBackPressed();
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
