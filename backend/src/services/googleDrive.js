const { google } = require('googleapis');

/**
 * Google Drive Service
 * Handles file operations with Google Drive API
 */
class GoogleDriveService {
  constructor(accessToken) {
    this.accessToken = accessToken;
    this.oauth2Client = new google.auth.OAuth2();
    this.oauth2Client.setCredentials({ access_token: accessToken });
    this.drive = google.drive({ version: 'v3', auth: this.oauth2Client });
  }

  /**
   * Upload a file to Google Drive
   */
  async uploadFile(filename, content, mimeType = 'application/json') {
    try {
      const fileMetadata = {
        name: filename,
        mimeType: mimeType
      };

      const media = {
        mimeType: mimeType,
        body: content
      };

      const response = await this.drive.files.create({
        requestBody: fileMetadata,
        media: media,
        fields: 'id, name, mimeType, createdTime, modifiedTime, size'
      });

      return {
        success: true,
        file: response.data
      };
    } catch (error) {
      console.error('Error uploading file to Google Drive:', error);
      throw new Error(`Failed to upload file: ${error.message}`);
    }
  }

  /**
   * Download a file from Google Drive
   */
  async downloadFile(fileId) {
    try {
      const response = await this.drive.files.get({
        fileId: fileId,
        alt: 'media'
      }, {
        responseType: 'stream'
      });

      return new Promise((resolve, reject) => {
        let data = '';
        response.data.on('data', chunk => {
          data += chunk;
        });
        response.data.on('end', () => {
          resolve({
            success: true,
            content: data
          });
        });
        response.data.on('error', err => {
          reject(new Error(`Failed to download file: ${err.message}`));
        });
      });
    } catch (error) {
      console.error('Error downloading file from Google Drive:', error);
      throw new Error(`Failed to download file: ${error.message}`);
    }
  }

  /**
   * List files in Google Drive
   */
  async listFiles(query = null, pageSize = 10) {
    try {
      const params = {
        pageSize: pageSize,
        fields: 'files(id, name, mimeType, createdTime, modifiedTime, size)',
        orderBy: 'modifiedTime desc'
      };

      if (query) {
        params.q = query;
      }

      const response = await this.drive.files.list(params);

      return {
        success: true,
        files: response.data.files || []
      };
    } catch (error) {
      console.error('Error listing files from Google Drive:', error);
      throw new Error(`Failed to list files: ${error.message}`);
    }
  }

  /**
   * Delete a file from Google Drive
   */
  async deleteFile(fileId) {
    try {
      await this.drive.files.delete({
        fileId: fileId
      });

      return {
        success: true,
        message: 'File deleted successfully'
      };
    } catch (error) {
      console.error('Error deleting file from Google Drive:', error);
      throw new Error(`Failed to delete file: ${error.message}`);
    }
  }

  /**
   * Search files by name
   */
  async searchFilesByName(filename) {
    try {
      const query = `name='${filename}' and trashed=false`;
      return await this.listFiles(query);
    } catch (error) {
      console.error('Error searching files in Google Drive:', error);
      throw new Error(`Failed to search files: ${error.message}`);
    }
  }

  /**
   * Create or get folder by path
   */
  async getOrCreateFolder(folderPath) {
    try {
      const folderName = folderPath.replace(/^\/+|\/+$/g, ''); // Remove leading/trailing slashes
      
      // Search for existing folder
      const query = `name='${folderName}' and mimeType='application/vnd.google-apps.folder' and trashed=false`;
      const response = await this.drive.files.list({
        q: query,
        fields: 'files(id, name)',
        pageSize: 1
      });

      if (response.data.files && response.data.files.length > 0) {
        return {
          success: true,
          folder: response.data.files[0]
        };
      }

      // Create folder if it doesn't exist
      const fileMetadata = {
        name: folderName,
        mimeType: 'application/vnd.google-apps.folder'
      };

      const folderResponse = await this.drive.files.create({
        requestBody: fileMetadata,
        fields: 'id, name'
      });

      return {
        success: true,
        folder: folderResponse.data
      };
    } catch (error) {
      console.error('Error creating/getting folder in Google Drive:', error);
      throw new Error(`Failed to create/get folder: ${error.message}`);
    }
  }

  /**
   * Upload file to a specific folder
   */
  async uploadFileToFolder(folderId, filename, content, mimeType = 'application/json') {
    try {
      const fileMetadata = {
        name: filename,
        parents: [folderId],
        mimeType: mimeType
      };

      const media = {
        mimeType: mimeType,
        body: content
      };

      const response = await this.drive.files.create({
        requestBody: fileMetadata,
        media: media,
        fields: 'id, name, mimeType, createdTime, modifiedTime, size'
      });

      return {
        success: true,
        file: response.data
      };
    } catch (error) {
      console.error('Error uploading file to folder in Google Drive:', error);
      throw new Error(`Failed to upload file to folder: ${error.message}`);
    }
  }

  /**
   * List files in a specific folder
   */
  async listFilesInFolder(folderId, pageSize = 100) {
    try {
      const query = `'${folderId}' in parents and trashed=false`;
      return await this.listFiles(query, pageSize);
    } catch (error) {
      console.error('Error listing files in folder from Google Drive:', error);
      throw new Error(`Failed to list files in folder: ${error.message}`);
    }
  }

  /**
   * Get file metadata
   */
  async getFileMetadata(fileId) {
    try {
      const response = await this.drive.files.get({
        fileId: fileId,
        fields: 'id, name, mimeType, createdTime, modifiedTime, size'
      });

      return {
        success: true,
        file: response.data
      };
    } catch (error) {
      console.error('Error getting file metadata from Google Drive:', error);
      throw new Error(`Failed to get file metadata: ${error.message}`);
    }
  }
}

module.exports = GoogleDriveService;
