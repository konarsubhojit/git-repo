package com.cloudsync.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudsync.app.api.ApiClient;
import com.cloudsync.app.models.CloudFolder;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class CloudFolderPickerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CloudFolderAdapter adapter;
    private TextView currentPathText;
    private TextView emptyStateText;
    private ProgressBar progressBar;
    private MaterialButton selectButton;
    private MaterialButton cancelButton;
    private MaterialToolbar toolbar;
    
    private String currentPath = "";
    private String provider = "google";
    private List<CloudFolder> folders = new ArrayList<>();
    
    public interface CloudFolderService {
        @GET("sync/folders/list")
        Call<ResponseBody> listFolders(@Query("folderPath") String folderPath, @Query("provider") String provider);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_folder_picker);
        
        // Get provider from intent
        provider = getIntent().getStringExtra("provider");
        if (provider == null || provider.isEmpty()) {
            provider = "google";
        }
        
        initializeViews();
        setupRecyclerView();
        setupClickListeners();
        
        // Load root folders
        loadFolders(currentPath);
    }
    
    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(provider.equals("google") ? "Google Drive Folders" : "OneDrive Folders");
        }
        
        recyclerView = findViewById(R.id.recyclerView);
        currentPathText = findViewById(R.id.currentPathText);
        emptyStateText = findViewById(R.id.emptyStateText);
        progressBar = findViewById(R.id.progressBar);
        selectButton = findViewById(R.id.selectButton);
        cancelButton = findViewById(R.id.cancelButton);
    }
    
    private void setupRecyclerView() {
        adapter = new CloudFolderAdapter(new CloudFolderAdapter.OnFolderClickListener() {
            @Override
            public void onFolderClick(CloudFolder folder) {
                // Navigate into subfolder
                String newPath = currentPath.isEmpty() ? folder.getName() : currentPath + "/" + folder.getName();
                loadFolders(newPath);
            }
            
            @Override
            public void onParentClick() {
                // Navigate to parent
                navigateToParent();
            }
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    
    private void setupClickListeners() {
        toolbar.setNavigationOnClickListener(v -> finish());
        
        selectButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("folder_path", currentPath.isEmpty() ? "/" : currentPath);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
        
        cancelButton.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
    }
    
    private void loadFolders(String path) {
        showLoading(true);
        
        CloudFolderService service = ApiClient.getClient().create(CloudFolderService.class);
        Call<ResponseBody> call = service.listFolders(path, provider);
        
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                showLoading(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonString = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonString);
                        
                        if (jsonObject.getBoolean("success")) {
                            currentPath = path;
                            currentPathText.setText(currentPath.isEmpty() ? "/" : currentPath);
                            
                            JSONArray foldersArray = jsonObject.getJSONArray("folders");
                            folders.clear();
                            
                            for (int i = 0; i < foldersArray.length(); i++) {
                                JSONObject folderObj = foldersArray.getJSONObject(i);
                                CloudFolder folder = new CloudFolder(
                                    folderObj.getString("id"),
                                    folderObj.getString("name")
                                );
                                folders.add(folder);
                            }
                            
                            updateUI();
                        } else {
                            showSnackbar("Failed to load folders");
                        }
                    } catch (Exception e) {
                        showSnackbar("Error parsing response: " + e.getMessage());
                    }
                } else {
                    showSnackbar("Failed to load folders: " + response.message());
                }
            }
            
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showLoading(false);
                showSnackbar("Network error: " + t.getMessage());
            }
        });
    }
    
    private void navigateToParent() {
        if (currentPath.isEmpty()) {
            // Already at root
            return;
        }
        
        int lastSlash = currentPath.lastIndexOf('/');
        String parentPath = lastSlash > 0 ? currentPath.substring(0, lastSlash) : "";
        loadFolders(parentPath);
    }
    
    private void updateUI() {
        boolean hasParent = !currentPath.isEmpty();
        
        if (folders.isEmpty() && !hasParent) {
            recyclerView.setVisibility(View.GONE);
            emptyStateText.setVisibility(View.VISIBLE);
            emptyStateText.setText("No folders found");
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateText.setVisibility(View.GONE);
        }
        
        adapter.setFolders(folders, hasParent);
    }
    
    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
    
    @Override
    public void onBackPressed() {
        if (!currentPath.isEmpty()) {
            navigateToParent();
        } else {
            super.onBackPressed();
        }
    }
    
    private void showSnackbar(String message) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }
}
