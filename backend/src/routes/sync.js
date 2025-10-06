const express = require('express');
const router = express.Router();
const { ensureAuthenticated } = require('../middleware/auth');
const GoogleDriveService = require('../services/googleDrive');
const OneDriveService = require('../services/oneDrive');

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

module.exports = router;
