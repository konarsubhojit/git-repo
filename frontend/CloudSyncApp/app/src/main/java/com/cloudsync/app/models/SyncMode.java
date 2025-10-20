package com.cloudsync.app.models;

public enum SyncMode {
    UPLOAD_ONLY("upload_only", "Upload Only"),
    UPLOAD_THEN_DELETE("upload_then_delete", "Upload then Delete"),
    DOWNLOAD_ONLY("download_only", "Download Only"),
    DOWNLOAD_THEN_DELETE("download_then_delete", "Download then Delete"),
    TWO_WAY("two_way", "Two-Way Sync");

    private final String value;
    private final String displayName;

    SyncMode(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static SyncMode fromValue(String value) {
        for (SyncMode mode : values()) {
            if (mode.value.equals(value)) {
                return mode;
            }
        }
        return UPLOAD_ONLY; // default
    }

    @Override
    public String toString() {
        return displayName;
    }
}
