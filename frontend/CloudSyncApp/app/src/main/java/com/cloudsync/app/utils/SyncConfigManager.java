package com.cloudsync.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.cloudsync.app.models.SyncConfig;
import com.cloudsync.app.models.SyncMode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SyncConfigManager {
    private static final String PREFS_NAME = "sync_configs";
    private static final String KEY_CONFIGS = "configs";
    private static final int MAX_CONFIGS = 10;
    
    private final SharedPreferences prefs;
    private final Gson gson;
    
    public SyncConfigManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }
    
    public List<SyncConfig> getAllConfigs() {
        String json = prefs.getString(KEY_CONFIGS, null);
        if (json == null) {
            return new ArrayList<>();
        }
        
        Type listType = new TypeToken<List<SyncConfig>>(){}.getType();
        List<SyncConfig> configs = gson.fromJson(json, listType);
        return configs != null ? configs : new ArrayList<>();
    }
    
    public boolean addConfig(SyncConfig config) {
        List<SyncConfig> configs = getAllConfigs();
        
        // Check max limit
        if (configs.size() >= MAX_CONFIGS) {
            return false;
        }
        
        // Generate ID if not present
        if (config.getId() == null || config.getId().isEmpty()) {
            config.setId(generateId());
        }
        
        configs.add(config);
        saveConfigs(configs);
        return true;
    }
    
    public boolean updateConfig(SyncConfig config) {
        List<SyncConfig> configs = getAllConfigs();
        
        for (int i = 0; i < configs.size(); i++) {
            if (configs.get(i).getId().equals(config.getId())) {
                configs.set(i, config);
                saveConfigs(configs);
                return true;
            }
        }
        
        return false;
    }
    
    public boolean deleteConfig(String configId) {
        List<SyncConfig> configs = getAllConfigs();
        boolean removed = configs.removeIf(c -> c.getId().equals(configId));
        
        if (removed) {
            saveConfigs(configs);
        }
        
        return removed;
    }
    
    public SyncConfig getConfig(String configId) {
        List<SyncConfig> configs = getAllConfigs();
        
        for (SyncConfig config : configs) {
            if (config.getId().equals(configId)) {
                return config;
            }
        }
        
        return null;
    }
    
    public int getConfigCount() {
        return getAllConfigs().size();
    }
    
    public boolean hasReachedMaxLimit() {
        return getConfigCount() >= MAX_CONFIGS;
    }
    
    public int getRemainingSlots() {
        return MAX_CONFIGS - getConfigCount();
    }
    
    private void saveConfigs(List<SyncConfig> configs) {
        String json = gson.toJson(configs);
        prefs.edit().putString(KEY_CONFIGS, json).apply();
    }
    
    private String generateId() {
        return "config_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
}
