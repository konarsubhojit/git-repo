package com.cloudsync.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
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
    private MaterialButton signInButton;
    private MaterialButton signOutButton;
    
    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onedrive_auth);
        
        statusText = findViewById(R.id.statusText);
        signInButton = findViewById(R.id.signInButton);
        signOutButton = findViewById(R.id.signOutButton);
        
        initializeMSAL();
        setupClickListeners();
    }
    
    private void initializeMSAL() {
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
                        Toast.makeText(OneDriveAuthActivity.this,
                                "Failed to initialize OneDrive authentication: " + exception.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
    
    private void setupClickListeners() {
        signInButton.setOnClickListener(v -> signIn());
        signOutButton.setOnClickListener(v -> signOut());
    }
    
    private void loadAccount() {
        if (msalApp == null) return;
        
        msalApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
            @Override
            public void onAccountLoaded(@Nullable IAccount account) {
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
                isConnected = false;
                updateUI(null);
            }
        });
    }
    
    private void signIn() {
        if (msalApp == null) {
            Toast.makeText(this, "Authentication not initialized", Toast.LENGTH_SHORT).show();
            return;
        }
        
        msalApp.signIn(this, "", SCOPES, new AuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                isConnected = true;
                IAccount account = authenticationResult.getAccount();
                updateUI(account);
                Toast.makeText(OneDriveAuthActivity.this,
                        "Successfully signed in to OneDrive", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(MsalException exception) {
                Log.e(TAG, "Sign in failed", exception);
                isConnected = false;
                updateUI(null);
                Toast.makeText(OneDriveAuthActivity.this,
                        "Sign in failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(OneDriveAuthActivity.this,
                        "Sign in cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void signOut() {
        if (msalApp == null) return;
        
        msalApp.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
            @Override
            public void onSignOut() {
                isConnected = false;
                updateUI(null);
                Toast.makeText(OneDriveAuthActivity.this,
                        "Signed out successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull MsalException exception) {
                Log.e(TAG, "Sign out failed", exception);
                Toast.makeText(OneDriveAuthActivity.this,
                        "Sign out failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void updateUI(IAccount account) {
        runOnUiThread(() -> {
            if (account != null) {
                statusText.setText(getString(R.string.onedrive_connected) + "\n" + account.getUsername());
                signInButton.setVisibility(View.GONE);
                signOutButton.setVisibility(View.VISIBLE);
            } else {
                statusText.setText(R.string.onedrive_not_connected);
                signInButton.setVisibility(View.VISIBLE);
                signOutButton.setVisibility(View.GONE);
            }
        });
    }
    
    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("connected", isConnected);
        setResult(RESULT_OK, resultIntent);
        super.onBackPressed();
    }
}
