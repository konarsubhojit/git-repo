package com.cloudsync.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudsync.app.api.ApiClient;
import com.cloudsync.app.api.SyncConfigService;
import com.cloudsync.app.api.responses.SyncConfigListResponse;
import com.cloudsync.app.models.SyncConfig;
import com.cloudsync.app.models.SyncMode;
import com.cloudsync.app.utils.SyncConfigManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncConfigListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SyncConfigAdapter adapter;
    private TextView emptyStateText;
    private TextView configCountText;
    private FloatingActionButton fabAdd;
    private SyncConfigService apiService;
    private SyncConfigManager configManager;
    
    private static final int REQUEST_ADD_CONFIG = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_config_list);
        
        apiService = ApiClient.getClient().create(SyncConfigService.class);
        configManager = new SyncConfigManager(this);
        
        initializeViews();
        setupRecyclerView();
        loadConfigurations();
    }
    
    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);
        configCountText = findViewById(R.id.configCountText);
        fabAdd = findViewById(R.id.fabAdd);
        
        fabAdd.setOnClickListener(v -> {
            if (configManager.hasReachedMaxLimit()) {
                showSnackbar(getString(R.string.max_configs_reached));
            } else {
                Intent intent = new Intent(this, FolderSyncConfigActivity.class);
                startActivityForResult(intent, REQUEST_ADD_CONFIG);
            }
        });
    }
    
    private void setupRecyclerView() {
        adapter = new SyncConfigAdapter(new SyncConfigAdapter.OnConfigClickListener() {
            @Override
            public void onConfigClick(SyncConfig config) {
                // Show config details or edit
                showConfigOptions(config);
            }
            
            @Override
            public void onConfigLongClick(SyncConfig config) {
                // Show delete confirmation
                showDeleteConfirmation(config);
            }
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    
    private void loadConfigurations() {
        List<SyncConfig> configs = configManager.getAllConfigs();
        updateUI(configs);
    }
    
    private void updateUI(List<SyncConfig> configs) {
        if (configs == null || configs.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateText.setVisibility(View.VISIBLE);
            configCountText.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateText.setVisibility(View.GONE);
            configCountText.setVisibility(View.VISIBLE);
            configCountText.setText(getString(R.string.config_count, configs.size(), 10));
            adapter.setConfigs(configs);
        }
    }
    
    private void showConfigOptions(SyncConfig config) {
        String[] options = {"View Details", "Edit", "Toggle Enable/Disable", "Delete"};
        
        new AlertDialog.Builder(this)
                .setTitle(config.getSyncMode().getDisplayName())
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            showConfigDetails(config);
                            break;
                        case 1:
                            editConfig(config);
                            break;
                        case 2:
                            toggleConfigEnabled(config);
                            break;
                        case 3:
                            showDeleteConfirmation(config);
                            break;
                    }
                })
                .show();
    }
    
    private void showConfigDetails(SyncConfig config) {
        String details = String.format(
            "Local: %s\nCloud: %s\nProvider: %s\nMode: %s\nDelay: %d days\nStatus: %s",
            config.getLocalFolderPath(),
            config.getCloudFolderPath(),
            config.getProvider(),
            config.getSyncMode().getDisplayName(),
            config.getDeleteDelayDays(),
            config.isEnabled() ? "Enabled" : "Disabled"
        );
        
        new AlertDialog.Builder(this)
                .setTitle("Configuration Details")
                .setMessage(details)
                .setPositiveButton("OK", null)
                .show();
    }
    
    private void editConfig(SyncConfig config) {
        showSnackbar("Edit functionality coming soon");
    }
    
    private void toggleConfigEnabled(SyncConfig config) {
        config.setEnabled(!config.isEnabled());
        adapter.notifyDataSetChanged();
        showSnackbar(config.isEnabled() ? "Configuration enabled" : "Configuration disabled");
    }
    
    private void showDeleteConfirmation(SyncConfig config) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Configuration")
                .setMessage("Are you sure you want to delete this sync configuration?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteConfig(config);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    private void deleteConfig(SyncConfig config) {
        boolean deleted = configManager.deleteConfig(config.getId());
        if (deleted) {
            showSnackbar("Configuration deleted");
            loadConfigurations();
        } else {
            showSnackbar("Failed to delete configuration");
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == REQUEST_ADD_CONFIG && resultCode == RESULT_OK && data != null) {
            // Create new config from result
            String localFolder = data.getStringExtra("local_folder");
            String cloudFolder = data.getStringExtra("cloud_folder");
            String syncModeValue = data.getStringExtra("sync_mode");
            String provider = data.getStringExtra("provider");
            int deleteDelayDays = data.getIntExtra("delete_delay_days", 0);
            
            // Convert sync mode string to enum
            SyncMode syncMode = SyncMode.fromValue(syncModeValue);
            
            SyncConfig config = new SyncConfig(localFolder, cloudFolder, provider, syncMode, deleteDelayDays);
            
            boolean added = configManager.addConfig(config);
            if (added) {
                showSnackbar("Configuration saved successfully");
                loadConfigurations();
            } else {
                showSnackbar(getString(R.string.max_configs_reached));
            }
        }
    }
    
    private void showSnackbar(String message) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }
}
