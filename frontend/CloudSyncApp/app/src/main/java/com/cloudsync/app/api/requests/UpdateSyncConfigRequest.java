package com.cloudsync.app.api.requests;

public class UpdateSyncConfigRequest {
    private String localFolderPath;
    private String cloudFolderPath;
    private String syncMode;
    private Integer deleteDelayDays;
    private Boolean enabled;

    public UpdateSyncConfigRequest() {
        // Empty constructor
    }

    // Setters
    public void setLocalFolderPath(String localFolderPath) { 
        this.localFolderPath = localFolderPath; 
    }
    
    public void setCloudFolderPath(String cloudFolderPath) { 
        this.cloudFolderPath = cloudFolderPath; 
    }
    
    public void setSyncMode(String syncMode) { 
        this.syncMode = syncMode; 
    }
    
    public void setDeleteDelayDays(Integer deleteDelayDays) { 
        this.deleteDelayDays = deleteDelayDays; 
    }
    
    public void setEnabled(Boolean enabled) { 
        this.enabled = enabled; 
    }

    // Getters
    public String getLocalFolderPath() { return localFolderPath; }
    public String getCloudFolderPath() { return cloudFolderPath; }
    public String getSyncMode() { return syncMode; }
    public Integer getDeleteDelayDays() { return deleteDelayDays; }
    public Boolean isEnabled() { return enabled; }
}
