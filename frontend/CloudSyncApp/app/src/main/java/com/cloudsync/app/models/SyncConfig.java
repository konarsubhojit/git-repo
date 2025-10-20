package com.cloudsync.app.models;

public class SyncConfig {
    private String id;
    private String userId;
    private String localFolderPath;
    private String cloudFolderPath;
    private String provider;
    private SyncMode syncMode;
    private int deleteDelayDays;
    private boolean enabled;
    private String lastSyncTime;
    private String createdAt;
    private String updatedAt;

    public SyncConfig() {
        // Default constructor
    }

    public SyncConfig(String localFolderPath, String cloudFolderPath, String provider, 
                      SyncMode syncMode, int deleteDelayDays) {
        this.localFolderPath = localFolderPath;
        this.cloudFolderPath = cloudFolderPath;
        this.provider = provider;
        this.syncMode = syncMode;
        this.deleteDelayDays = deleteDelayDays;
        this.enabled = true;
    }

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getLocalFolderPath() { return localFolderPath; }
    public String getCloudFolderPath() { return cloudFolderPath; }
    public String getProvider() { return provider; }
    public SyncMode getSyncMode() { return syncMode; }
    public int getDeleteDelayDays() { return deleteDelayDays; }
    public boolean isEnabled() { return enabled; }
    public String getLastSyncTime() { return lastSyncTime; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setLocalFolderPath(String localFolderPath) { this.localFolderPath = localFolderPath; }
    public void setCloudFolderPath(String cloudFolderPath) { this.cloudFolderPath = cloudFolderPath; }
    public void setProvider(String provider) { this.provider = provider; }
    public void setSyncMode(SyncMode syncMode) { this.syncMode = syncMode; }
    public void setDeleteDelayDays(int deleteDelayDays) { this.deleteDelayDays = deleteDelayDays; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setLastSyncTime(String lastSyncTime) { this.lastSyncTime = lastSyncTime; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
