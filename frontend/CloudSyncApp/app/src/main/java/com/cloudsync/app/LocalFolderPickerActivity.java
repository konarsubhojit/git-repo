package com.cloudsync.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LocalFolderPickerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FolderAdapter adapter;
    private TextView currentPathText;
    private TextView emptyStateText;
    private MaterialButton selectButton;
    private MaterialButton cancelButton;
    private MaterialToolbar toolbar;
    
    private File currentFolder;
    private List<File> folders = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_folder_picker);
        
        initializeViews();
        setupRecyclerView();
        setupClickListeners();
        
        // Start at external storage root
        File startFolder = Environment.getExternalStorageDirectory();
        navigateToFolder(startFolder);
    }
    
    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        recyclerView = findViewById(R.id.recyclerView);
        currentPathText = findViewById(R.id.currentPathText);
        emptyStateText = findViewById(R.id.emptyStateText);
        selectButton = findViewById(R.id.selectButton);
        cancelButton = findViewById(R.id.cancelButton);
    }
    
    private void setupRecyclerView() {
        adapter = new FolderAdapter(new FolderAdapter.OnFolderClickListener() {
            @Override
            public void onFolderClick(File folder) {
                navigateToFolder(folder);
            }
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    
    private void setupClickListeners() {
        toolbar.setNavigationOnClickListener(v -> finish());
        
        selectButton.setOnClickListener(v -> {
            if (currentFolder != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("folder_path", currentFolder.getAbsolutePath());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        
        cancelButton.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
    }
    
    private void navigateToFolder(File folder) {
        if (folder == null || !folder.exists() || !folder.isDirectory()) {
            showSnackbar("Cannot access this folder");
            return;
        }
        
        currentFolder = folder;
        currentPathText.setText(folder.getAbsolutePath());
        
        // Load subfolders
        loadFolders();
    }
    
    private void loadFolders() {
        folders.clear();
        
        // Add parent folder if not at root
        File parent = currentFolder.getParentFile();
        if (parent != null && parent.canRead()) {
            folders.add(parent);
        }
        
        // List subdirectories
        File[] files = currentFolder.listFiles();
        if (files != null) {
            List<File> subFolders = new ArrayList<>();
            for (File file : files) {
                if (file.isDirectory() && !file.isHidden() && file.canRead()) {
                    subFolders.add(file);
                }
            }
            
            // Sort folders by name
            Collections.sort(subFolders, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
            folders.addAll(subFolders);
        }
        
        // Update UI
        if (folders.isEmpty() || (folders.size() == 1 && folders.get(0).equals(parent))) {
            recyclerView.setVisibility(View.GONE);
            emptyStateText.setVisibility(View.VISIBLE);
            emptyStateText.setText("No accessible folders in this directory");
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateText.setVisibility(View.GONE);
        }
        
        adapter.setFolders(folders, parent);
    }
    
    @Override
    public void onBackPressed() {
        File parent = currentFolder.getParentFile();
        if (parent != null && parent.canRead()) {
            navigateToFolder(parent);
        } else {
            super.onBackPressed();
        }
    }
    
    private void showSnackbar(String message) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }
}
