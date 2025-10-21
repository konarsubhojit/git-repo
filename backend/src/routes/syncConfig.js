const express = require('express');
const router = express.Router();
const { ensureAuthenticated } = require('../middleware/auth');
const { SyncMode, SyncConfigRepository } = require('../models/syncConfig');

/**
 * Get all sync configurations for the authenticated user
 * GET /api/sync-config
 */
router.get('/', ensureAuthenticated, async (req, res, next) => {
  try {
    const userId = req.user.id;
    const configs = SyncConfigRepository.findByUserId(userId);
    
    res.json({
      success: true,
      configs: configs.map(config => config.toJSON())
    });
  } catch (error) {
    next(error);
  }
});

/**
 * Get a specific sync configuration
 * GET /api/sync-config/:configId
 */
router.get('/:configId', ensureAuthenticated, async (req, res, next) => {
  try {
    const userId = req.user.id;
    const { configId } = req.params;
    
    const config = SyncConfigRepository.findById(userId, configId);
    
    if (!config) {
      return res.status(404).json({
        error: {
          message: 'Sync configuration not found',
          status: 404
        }
      });
    }
    
    res.json({
      success: true,
      config: config.toJSON()
    });
  } catch (error) {
    next(error);
  }
});

/**
 * Create a new sync configuration
 * POST /api/sync-config
 * Body: {
 *   localFolderPath: string,
 *   cloudFolderPath: string,
 *   provider: 'google' | 'microsoft',
 *   syncMode: string,
 *   deleteDelayDays: number (optional, default: 0),
 *   enabled: boolean (optional, default: true)
 * }
 */
router.post('/', ensureAuthenticated, async (req, res, next) => {
  try {
    const userId = req.user.id;
    const { localFolderPath, cloudFolderPath, provider, syncMode, deleteDelayDays, enabled } = req.body;
    
    // Validation
    if (!localFolderPath || !cloudFolderPath || !provider || !syncMode) {
      return res.status(400).json({
        error: {
          message: 'localFolderPath, cloudFolderPath, provider, and syncMode are required',
          status: 400
        }
      });
    }
    
    // Validate sync mode
    const validSyncModes = Object.values(SyncMode);
    if (!validSyncModes.includes(syncMode)) {
      return res.status(400).json({
        error: {
          message: `Invalid sync mode. Valid modes are: ${validSyncModes.join(', ')}`,
          status: 400
        }
      });
    }
    
    // Validate provider
    if (provider !== 'google' && provider !== 'microsoft') {
      return res.status(400).json({
        error: {
          message: 'Provider must be either "google" or "microsoft"',
          status: 400
        }
      });
    }
    
    // Validate delete delay if provided
    if (deleteDelayDays !== undefined && (isNaN(deleteDelayDays) || deleteDelayDays < 0)) {
      return res.status(400).json({
        error: {
          message: 'deleteDelayDays must be a non-negative number',
          status: 400
        }
      });
    }
    
    const config = SyncConfigRepository.create({
      userId,
      localFolderPath,
      cloudFolderPath,
      provider,
      syncMode,
      deleteDelayDays: deleteDelayDays !== undefined ? deleteDelayDays : 0,
      enabled: enabled !== undefined ? enabled : true
    });
    
    res.status(201).json({
      success: true,
      message: 'Sync configuration created successfully',
      config: config.toJSON()
    });
  } catch (error) {
    next(error);
  }
});

/**
 * Update a sync configuration
 * PUT /api/sync-config/:configId
 * Body: {
 *   localFolderPath: string (optional),
 *   cloudFolderPath: string (optional),
 *   syncMode: string (optional),
 *   deleteDelayDays: number (optional),
 *   enabled: boolean (optional)
 * }
 */
router.put('/:configId', ensureAuthenticated, async (req, res, next) => {
  try {
    const userId = req.user.id;
    const { configId } = req.params;
    const updateData = req.body;
    
    // Validate sync mode if provided
    if (updateData.syncMode) {
      const validSyncModes = Object.values(SyncMode);
      if (!validSyncModes.includes(updateData.syncMode)) {
        return res.status(400).json({
          error: {
            message: `Invalid sync mode. Valid modes are: ${validSyncModes.join(', ')}`,
            status: 400
          }
        });
      }
    }
    
    // Validate delete delay if provided
    if (updateData.deleteDelayDays !== undefined && (isNaN(updateData.deleteDelayDays) || updateData.deleteDelayDays < 0)) {
      return res.status(400).json({
        error: {
          message: 'deleteDelayDays must be a non-negative number',
          status: 400
        }
      });
    }
    
    const updatedConfig = SyncConfigRepository.update(userId, configId, updateData);
    
    if (!updatedConfig) {
      return res.status(404).json({
        error: {
          message: 'Sync configuration not found',
          status: 404
        }
      });
    }
    
    res.json({
      success: true,
      message: 'Sync configuration updated successfully',
      config: updatedConfig.toJSON()
    });
  } catch (error) {
    next(error);
  }
});

/**
 * Delete a sync configuration
 * DELETE /api/sync-config/:configId
 */
router.delete('/:configId', ensureAuthenticated, async (req, res, next) => {
  try {
    const userId = req.user.id;
    const { configId } = req.params;
    
    const deleted = SyncConfigRepository.delete(userId, configId);
    
    if (!deleted) {
      return res.status(404).json({
        error: {
          message: 'Sync configuration not found',
          status: 404
        }
      });
    }
    
    res.json({
      success: true,
      message: 'Sync configuration deleted successfully'
    });
  } catch (error) {
    next(error);
  }
});

module.exports = router;
