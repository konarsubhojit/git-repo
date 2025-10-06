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
}

module.exports = GoogleDriveService;
