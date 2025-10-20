/**
 * Sync Configuration Model
 * Since we don't have a database, we'll use in-memory storage with session persistence
 */

// In-memory storage for sync configurations (in production, use a database)
const syncConfigurations = new Map();

/**
 * Sync Mode Enum
 */
const SyncMode = {
  UPLOAD_ONLY: 'upload_only',
  UPLOAD_THEN_DELETE: 'upload_then_delete',
  DOWNLOAD_ONLY: 'download_only',
  DOWNLOAD_THEN_DELETE: 'download_then_delete',
  TWO_WAY: 'two_way'
};

/**
 * Sync Configuration class
 */
class SyncConfig {
  constructor(data) {
    this.id = data.id || this.generateId();
    this.userId = data.userId; // User session ID
    this.localFolderPath = data.localFolderPath;
    this.cloudFolderPath = data.cloudFolderPath;
    this.provider = data.provider; // 'google' or 'microsoft'
    this.syncMode = data.syncMode;
    this.deleteDelayDays = data.deleteDelayDays || 0;
    this.enabled = data.enabled !== undefined ? data.enabled : true;
    this.lastSyncTime = data.lastSyncTime || null;
    this.createdAt = data.createdAt || new Date().toISOString();
    this.updatedAt = data.updatedAt || new Date().toISOString();
  }

  generateId() {
    return `sync_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
  }

  toJSON() {
    return {
      id: this.id,
      userId: this.userId,
      localFolderPath: this.localFolderPath,
      cloudFolderPath: this.cloudFolderPath,
      provider: this.provider,
      syncMode: this.syncMode,
      deleteDelayDays: this.deleteDelayDays,
      enabled: this.enabled,
      lastSyncTime: this.lastSyncTime,
      createdAt: this.createdAt,
      updatedAt: this.updatedAt
    };
  }
}

/**
 * SyncConfig Repository
 */
class SyncConfigRepository {
  /**
   * Create a new sync configuration
   */
  static create(data) {
    const config = new SyncConfig(data);
    const userConfigs = syncConfigurations.get(config.userId) || [];
    userConfigs.push(config);
    syncConfigurations.set(config.userId, userConfigs);
    return config;
  }

  /**
   * Get all sync configurations for a user
   */
  static findByUserId(userId) {
    return syncConfigurations.get(userId) || [];
  }

  /**
   * Get a specific sync configuration by ID
   */
  static findById(userId, configId) {
    const userConfigs = syncConfigurations.get(userId) || [];
    return userConfigs.find(config => config.id === configId);
  }

  /**
   * Update a sync configuration
   */
  static update(userId, configId, updateData) {
    const userConfigs = syncConfigurations.get(userId) || [];
    const configIndex = userConfigs.findIndex(config => config.id === configId);
    
    if (configIndex === -1) {
      return null;
    }

    const existingConfig = userConfigs[configIndex];
    const updatedConfig = new SyncConfig({
      ...existingConfig.toJSON(),
      ...updateData,
      id: configId,
      userId: userId,
      updatedAt: new Date().toISOString()
    });

    userConfigs[configIndex] = updatedConfig;
    syncConfigurations.set(userId, userConfigs);
    return updatedConfig;
  }

  /**
   * Delete a sync configuration
   */
  static delete(userId, configId) {
    const userConfigs = syncConfigurations.get(userId) || [];
    const filteredConfigs = userConfigs.filter(config => config.id !== configId);
    
    if (filteredConfigs.length === userConfigs.length) {
      return false; // Config not found
    }

    syncConfigurations.set(userId, filteredConfigs);
    return true;
  }

  /**
   * Update last sync time
   */
  static updateLastSyncTime(userId, configId) {
    return this.update(userId, configId, {
      lastSyncTime: new Date().toISOString()
    });
  }
}

module.exports = {
  SyncMode,
  SyncConfig,
  SyncConfigRepository
};
