const express = require('express');
const router = express.Router();
const { ensureAuthenticated } = require('../middleware/auth');
const GoogleDriveService = require('../services/googleDrive');
const OneDriveService = require('../services/oneDrive');
const { SyncMode, SyncConfigRepository } = require('../models/syncConfig');

/**
 * Upload data to cloud storage
 * POST /api/sync/upload
 * Body: { filename: string, content: string }
 */
router.post('/upload', ensureAuthenticated, async (req, res, next) => {
  try {
    const { filename, content } = req.body;

    if (!filename || !content) {
      return res.status(400).json({
        error: {
          message: 'Filename and content are required',
          status: 400
        }
      });
    }

    const provider = req.user.provider;
    let result;

    if (provider === 'google') {
      const driveService = new GoogleDriveService(req.user.accessToken);
      result = await driveService.uploadFile(filename, content);
    } else if (provider === 'microsoft') {
      const driveService = new OneDriveService(req.user.accessToken);
      result = await driveService.uploadFile(filename, content);
    } else {
      return res.status(400).json({
        error: {
          message: 'Unsupported provider',
          status: 400
        }
      });
    }

    res.json({
      success: true,
      message: 'File uploaded successfully',
      provider: provider,
      file: result.file
    });
  } catch (error) {
    next(error);
  }
});

/**
 * Download data from cloud storage
 * GET /api/sync/download?fileId=xxx or ?filename=xxx
 */
router.get('/download', ensureAuthenticated, async (req, res, next) => {
  try {
    const { fileId, filename } = req.query;

    if (!fileId && !filename) {
      return res.status(400).json({
        error: {
          message: 'Either fileId or filename is required',
          status: 400
        }
      });
    }

    const provider = req.user.provider;
    let result;

    if (provider === 'google') {
      const driveService = new GoogleDriveService(req.user.accessToken);
      
      if (fileId) {
        result = await driveService.downloadFile(fileId);
      } else {
        // Search for file by name first
        const searchResult = await driveService.searchFilesByName(filename);
        if (searchResult.files && searchResult.files.length > 0) {
          result = await driveService.downloadFile(searchResult.files[0].id);
        } else {
          return res.status(404).json({
            error: {
              message: 'File not found',
              status: 404
            }
          });
        }
      }
    } else if (provider === 'microsoft') {
      const driveService = new OneDriveService(req.user.accessToken);
      
      if (fileId) {
        result = await driveService.downloadFile(fileId);
      } else {
        result = await driveService.downloadFileByName(filename);
      }
    } else {
      return res.status(400).json({
        error: {
          message: 'Unsupported provider',
          status: 400
        }
      });
    }

    res.json({
      success: true,
      provider: provider,
      content: result.content
    });
  } catch (error) {
    next(error);
  }
});

/**
 * List files in cloud storage
 * GET /api/sync/list
 */
router.get('/list', ensureAuthenticated, async (req, res, next) => {
  try {
    const provider = req.user.provider;
    let result;

    if (provider === 'google') {
      const driveService = new GoogleDriveService(req.user.accessToken);
      result = await driveService.listFiles();
    } else if (provider === 'microsoft') {
      const driveService = new OneDriveService(req.user.accessToken);
      result = await driveService.listFiles();
    } else {
      return res.status(400).json({
        error: {
          message: 'Unsupported provider',
          status: 400
        }
      });
    }

    res.json({
      success: true,
      provider: provider,
      files: result.files
    });
  } catch (error) {
    next(error);
  }
});

/**
 * Delete file from cloud storage
 * DELETE /api/sync/delete/:fileId
 */
router.delete('/delete/:fileId', ensureAuthenticated, async (req, res, next) => {
  try {
    const { fileId } = req.params;

    if (!fileId) {
      return res.status(400).json({
        error: {
          message: 'File ID is required',
          status: 400
        }
      });
    }

    const provider = req.user.provider;
    let result;

    if (provider === 'google') {
      const driveService = new GoogleDriveService(req.user.accessToken);
      result = await driveService.deleteFile(fileId);
    } else if (provider === 'microsoft') {
      const driveService = new OneDriveService(req.user.accessToken);
      result = await driveService.deleteFile(fileId);
    } else {
      return res.status(400).json({
        error: {
          message: 'Unsupported provider',
          status: 400
        }
      });
    }

    res.json({
      success: true,
      provider: provider,
      message: result.message
    });
  } catch (error) {
    next(error);
  }
});

/**
 * Upload file to a specific folder
 * POST /api/sync/folder/upload
 * Body: { folderPath: string, filename: string, content: string }
 */
router.post('/folder/upload', ensureAuthenticated, async (req, res, next) => {
  try {
    const { folderPath, filename, content } = req.body;

    if (!folderPath || !filename || !content) {
      return res.status(400).json({
        error: {
          message: 'folderPath, filename, and content are required',
          status: 400
        }
      });
    }

    const provider = req.user.provider;
    let result;

    if (provider === 'google') {
      const driveService = new GoogleDriveService(req.user.accessToken);
      const folderResult = await driveService.getOrCreateFolder(folderPath);
      result = await driveService.uploadFileToFolder(folderResult.folder.id, filename, content);
    } else if (provider === 'microsoft') {
      const driveService = new OneDriveService(req.user.accessToken);
      const folderResult = await driveService.getOrCreateFolder(folderPath);
      result = await driveService.uploadFileToFolder(folderResult.folder.id, filename, content);
    } else {
      return res.status(400).json({
        error: {
          message: 'Unsupported provider',
          status: 400
        }
      });
    }

    res.json({
      success: true,
      message: 'File uploaded to folder successfully',
      provider: provider,
      file: result.file
    });
  } catch (error) {
    next(error);
  }
});

/**
 * List files in a specific folder
 * GET /api/sync/folder/list?folderPath=xxx
 */
router.get('/folder/list', ensureAuthenticated, async (req, res, next) => {
  try {
    const { folderPath } = req.query;

    if (!folderPath) {
      return res.status(400).json({
        error: {
          message: 'folderPath is required',
          status: 400
        }
      });
    }

    const provider = req.user.provider;
    let result;

    if (provider === 'google') {
      const driveService = new GoogleDriveService(req.user.accessToken);
      const folderResult = await driveService.getOrCreateFolder(folderPath);
      result = await driveService.listFilesInFolder(folderResult.folder.id);
    } else if (provider === 'microsoft') {
      const driveService = new OneDriveService(req.user.accessToken);
      const folderResult = await driveService.getOrCreateFolder(folderPath);
      result = await driveService.listFilesInFolder(folderResult.folder.id);
    } else {
      return res.status(400).json({
        error: {
          message: 'Unsupported provider',
          status: 400
        }
      });
    }

    res.json({
      success: true,
      provider: provider,
      files: result.files
    });
  } catch (error) {
    next(error);
  }
});

/**
 * Execute sync based on configuration
 * POST /api/sync/execute/:configId
 * Body: { files: [{ filename: string, content: string }] } (for upload modes)
 */
router.post('/execute/:configId', ensureAuthenticated, async (req, res, next) => {
  try {
    const userId = req.user.id;
    const { configId } = req.params;
    const { files } = req.body;

    const config = SyncConfigRepository.findById(userId, configId);

    if (!config) {
      return res.status(404).json({
        error: {
          message: 'Sync configuration not found',
          status: 404
        }
      });
    }

    if (!config.enabled) {
      return res.status(400).json({
        error: {
          message: 'Sync configuration is disabled',
          status: 400
        }
      });
    }

    const provider = config.provider;
    let driveService;

    if (provider === 'google') {
      driveService = new GoogleDriveService(req.user.accessToken);
    } else if (provider === 'microsoft') {
      driveService = new OneDriveService(req.user.accessToken);
    } else {
      return res.status(400).json({
        error: {
          message: 'Unsupported provider',
          status: 400
        }
      });
    }

    const folderResult = await driveService.getOrCreateFolder(config.cloudFolderPath);
    const folderId = folderResult.folder.id;

    let syncResults = {
      uploaded: [],
      downloaded: [],
      deleted: [],
      errors: []
    };

    // Handle different sync modes
    switch (config.syncMode) {
      case SyncMode.UPLOAD_ONLY:
      case SyncMode.UPLOAD_THEN_DELETE:
        if (!files || !Array.isArray(files) || files.length === 0) {
          return res.status(400).json({
            error: {
              message: 'Files array is required for upload sync modes',
              status: 400
            }
          });
        }

        for (const fileData of files) {
          try {
            const result = await driveService.uploadFileToFolder(
              folderId,
              fileData.filename,
              fileData.content
            );
            syncResults.uploaded.push(result.file);
          } catch (error) {
            syncResults.errors.push({
              filename: fileData.filename,
              error: error.message
            });
          }
        }
        break;

      case SyncMode.DOWNLOAD_ONLY:
      case SyncMode.DOWNLOAD_THEN_DELETE:
        const listResult = await driveService.listFilesInFolder(folderId);
        
        for (const file of listResult.files) {
          try {
            const downloadResult = await driveService.downloadFile(file.id);
            syncResults.downloaded.push({
              ...file,
              content: downloadResult.content
            });
          } catch (error) {
            syncResults.errors.push({
              filename: file.name,
              error: error.message
            });
          }
        }
        break;

      case SyncMode.TWO_WAY:
        // Upload provided files
        if (files && Array.isArray(files) && files.length > 0) {
          for (const fileData of files) {
            try {
              const result = await driveService.uploadFileToFolder(
                folderId,
                fileData.filename,
                fileData.content
              );
              syncResults.uploaded.push(result.file);
            } catch (error) {
              syncResults.errors.push({
                filename: fileData.filename,
                error: error.message
              });
            }
          }
        }

        // Download all files from cloud
        const cloudFiles = await driveService.listFilesInFolder(folderId);
        for (const file of cloudFiles.files) {
          try {
            const downloadResult = await driveService.downloadFile(file.id);
            syncResults.downloaded.push({
              ...file,
              content: downloadResult.content
            });
          } catch (error) {
            syncResults.errors.push({
              filename: file.name,
              error: error.message
            });
          }
        }
        break;

      default:
        return res.status(400).json({
          error: {
            message: 'Invalid sync mode',
            status: 400
          }
        });
    }

    // Update last sync time
    SyncConfigRepository.updateLastSyncTime(userId, configId);

    res.json({
      success: true,
      message: 'Sync executed successfully',
      syncMode: config.syncMode,
      results: syncResults
    });
  } catch (error) {
    next(error);
  }
});

module.exports = router;
