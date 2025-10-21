package com.cloudsync.app.api.requests;

public class CreateSyncConfigRequest {
    private String localFolderPath;
    private String cloudFolderPath;
    private String provider;
    private String syncMode;
    private int deleteDelayDays;
    private boolean enabled;

    public CreateSyncConfigRequest(String localFolderPath, String cloudFolderPath, 
                                   String provider, String syncMode, 
                                   int deleteDelayDays, boolean enabled) {
        this.localFolderPath = localFolderPath;
        this.cloudFolderPath = cloudFolderPath;
        this.provider = provider;
        this.syncMode = syncMode;
        this.deleteDelayDays = deleteDelayDays;
        this.enabled = enabled;
    }

    // Getters
    public String getLocalFolderPath() { return localFolderPath; }
    public String getCloudFolderPath() { return cloudFolderPath; }
    public String getProvider() { return provider; }
    public String getSyncMode() { return syncMode; }
    public int getDeleteDelayDays() { return deleteDelayDays; }
    public boolean isEnabled() { return enabled; }
}
