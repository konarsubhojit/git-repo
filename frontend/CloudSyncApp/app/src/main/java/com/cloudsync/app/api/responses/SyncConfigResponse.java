package com.cloudsync.app.api.responses;

import com.cloudsync.app.models.SyncConfig;

public class SyncConfigResponse {
    private boolean success;
    private String message;
    private SyncConfig config;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public SyncConfig getConfig() { return config; }
}
