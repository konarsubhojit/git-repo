package com.cloudsync.app.api.responses;

import com.cloudsync.app.models.SyncConfig;
import java.util.List;

public class SyncConfigListResponse {
    private boolean success;
    private List<SyncConfig> configs;

    public boolean isSuccess() { return success; }
    public List<SyncConfig> getConfigs() { return configs; }
}
