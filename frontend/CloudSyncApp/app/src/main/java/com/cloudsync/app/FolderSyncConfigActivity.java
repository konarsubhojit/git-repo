package com.cloudsync.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudsync.app.models.SyncMode;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class FolderSyncConfigActivity extends AppCompatActivity {
    private static final int REQUEST_SELECT_LOCAL_FOLDER = 1001;
    private static final int REQUEST_SELECT_CLOUD_FOLDER = 1002;
    
    private TextInputEditText localFolderInput;
    private TextInputEditText cloudFolderInput;
    private AutoCompleteTextView syncModeDropdown;
    private AutoCompleteTextView providerDropdown;
    private LinearLayout deleteDelayContainer;
    private Slider deleteDelaySlider;
    private TextView deleteDelayValue;
    private MaterialButton selectLocalFolderButton;
    private MaterialButton selectCloudFolderButton;
    private MaterialButton saveConfigButton;
    private MaterialButton cancelButton;
    
    private String selectedLocalFolder = "";
    private String selectedCloudFolder = "";
    private SyncMode selectedSyncMode = SyncMode.UPLOAD_ONLY;
    private String selectedProvider = "google";
    private int deleteDelayDays = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_sync_config);
        
        initializeViews();
        setupSyncModeDropdown();
        setupProviderDropdown();
        setupDeleteDelaySlider();
        setupClickListeners();
        updateDeleteDelayVisibility();
    }
    
    private void initializeViews() {
        localFolderInput = findViewById(R.id.localFolderInput);
        cloudFolderInput = findViewById(R.id.cloudFolderInput);
        syncModeDropdown = findViewById(R.id.syncModeDropdown);
        providerDropdown = findViewById(R.id.providerDropdown);
        deleteDelayContainer = findViewById(R.id.deleteDelayContainer);
        deleteDelaySlider = findViewById(R.id.deleteDelaySlider);
        deleteDelayValue = findViewById(R.id.deleteDelayValue);
        selectLocalFolderButton = findViewById(R.id.selectLocalFolderButton);
        selectCloudFolderButton = findViewById(R.id.selectCloudFolderButton);
        saveConfigButton = findViewById(R.id.saveConfigButton);
        cancelButton = findViewById(R.id.cancelButton);
    }
    
    private void setupSyncModeDropdown() {
        String[] syncModes = new String[SyncMode.values().length];
        for (int i = 0; i < SyncMode.values().length; i++) {
            syncModes[i] = SyncMode.values()[i].getDisplayName();
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            syncModes
        );
        
        syncModeDropdown.setAdapter(adapter);
        syncModeDropdown.setText(SyncMode.UPLOAD_ONLY.getDisplayName(), false);
        
        syncModeDropdown.setOnItemClickListener((parent, view, position, id) -> {
            selectedSyncMode = SyncMode.values()[position];
            updateDeleteDelayVisibility();
        });
    }
    
    private void setupProviderDropdown() {
        String[] providers = {"Google Drive", "OneDrive"};
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            providers
        );
        
        providerDropdown.setAdapter(adapter);
        providerDropdown.setText("Google Drive", false);
        
        providerDropdown.setOnItemClickListener((parent, view, position, id) -> {
            selectedProvider = position == 0 ? "google" : "microsoft";
        });
    }
    
    private void setupDeleteDelaySlider() {
        deleteDelaySlider.setValueFrom(0);
        deleteDelaySlider.setValueTo(30);
        deleteDelaySlider.setStepSize(1);
        deleteDelaySlider.setValue(0);
        
        deleteDelaySlider.addOnChangeListener((slider, value, fromUser) -> {
            deleteDelayDays = (int) value;
            updateDeleteDelayText();
        });
        
        updateDeleteDelayText();
    }
    
    private void updateDeleteDelayText() {
        if (deleteDelayDays == 0) {
            deleteDelayValue.setText("Immediate");
        } else if (deleteDelayDays == 1) {
            deleteDelayValue.setText("1 day");
        } else {
            deleteDelayValue.setText(deleteDelayDays + " days");
        }
    }
    
    private void updateDeleteDelayVisibility() {
        boolean showDelay = selectedSyncMode == SyncMode.UPLOAD_THEN_DELETE || 
                            selectedSyncMode == SyncMode.DOWNLOAD_THEN_DELETE;
        deleteDelayContainer.setVisibility(showDelay ? View.VISIBLE : View.GONE);
    }
    
    private void setupClickListeners() {
        selectLocalFolderButton.setOnClickListener(v -> selectLocalFolder());
        
        selectCloudFolderButton.setOnClickListener(v -> selectCloudFolder());
        
        saveConfigButton.setOnClickListener(v -> saveConfiguration());
        
        cancelButton.setOnClickListener(v -> finish());
    }
    
    private void selectLocalFolder() {
        Intent intent = new Intent(this, LocalFolderPickerActivity.class);
        startActivityForResult(intent, REQUEST_SELECT_LOCAL_FOLDER);
    }
    
    private void selectCloudFolder() {
        Intent intent = new Intent(this, CloudFolderPickerActivity.class);
        intent.putExtra("provider", selectedProvider);
        startActivityForResult(intent, REQUEST_SELECT_CLOUD_FOLDER);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_SELECT_LOCAL_FOLDER) {
                selectedLocalFolder = data.getStringExtra("folder_path");
                if (selectedLocalFolder != null) {
                    localFolderInput.setText(selectedLocalFolder);
                }
            } else if (requestCode == REQUEST_SELECT_CLOUD_FOLDER) {
                selectedCloudFolder = data.getStringExtra("folder_path");
                if (selectedCloudFolder != null) {
                    cloudFolderInput.setText(selectedCloudFolder);
                }
            }
        }
    }
    
    private void saveConfiguration() {
        String localFolder = localFolderInput.getText() != null ? 
                            localFolderInput.getText().toString().trim() : "";
        String cloudFolder = cloudFolderInput.getText() != null ? 
                            cloudFolderInput.getText().toString().trim() : "";
        
        // Validation
        if (localFolder.isEmpty()) {
            showSnackbar("Please enter a local folder path");
            localFolderInput.requestFocus();
            return;
        }
        
        if (cloudFolder.isEmpty()) {
            showSnackbar("Please enter a cloud folder path");
            cloudFolderInput.requestFocus();
            return;
        }
        
        // Return result to MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("local_folder", localFolder);
        resultIntent.putExtra("cloud_folder", cloudFolder);
        resultIntent.putExtra("sync_mode", selectedSyncMode.getValue());
        resultIntent.putExtra("provider", selectedProvider);
        resultIntent.putExtra("delete_delay_days", deleteDelayDays);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
    
    private void showSnackbar(String message) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }
}
