const axios = require('axios');

/**
 * OneDrive Service
 * Handles file operations with Microsoft OneDrive API
 */
class OneDriveService {
  constructor(accessToken) {
    this.accessToken = accessToken;
    this.baseUrl = 'https://graph.microsoft.com/v1.0';
    this.headers = {
      'Authorization': `Bearer ${accessToken}`,
      'Content-Type': 'application/json'
    };
  }

  /**
   * Upload a file to OneDrive
   */
  async uploadFile(filename, content) {
    try {
      const url = `${this.baseUrl}/me/drive/root:/${filename}:/content`;
      
      const response = await axios.put(url, content, {
        headers: {
          'Authorization': `Bearer ${this.accessToken}`,
          'Content-Type': 'application/json'
        }
      });

      return {
        success: true,
        file: {
          id: response.data.id,
          name: response.data.name,
          size: response.data.size,
          createdDateTime: response.data.createdDateTime,
          lastModifiedDateTime: response.data.lastModifiedDateTime,
          webUrl: response.data.webUrl
        }
      };
    } catch (error) {
      console.error('Error uploading file to OneDrive:', error.response?.data || error.message);
      throw new Error(`Failed to upload file: ${error.response?.data?.error?.message || error.message}`);
    }
  }

  /**
   * Download a file from OneDrive
   */
  async downloadFile(fileId) {
    try {
      const url = `${this.baseUrl}/me/drive/items/${fileId}/content`;
      
      const response = await axios.get(url, {
        headers: this.headers,
        responseType: 'text'
      });

      return {
        success: true,
        content: response.data
      };
    } catch (error) {
      console.error('Error downloading file from OneDrive:', error.response?.data || error.message);
      throw new Error(`Failed to download file: ${error.response?.data?.error?.message || error.message}`);
    }
  }

  /**
   * Download a file from OneDrive by name
   */
  async downloadFileByName(filename) {
    try {
      const url = `${this.baseUrl}/me/drive/root:/${filename}:/content`;
      
      const response = await axios.get(url, {
        headers: this.headers,
        responseType: 'text'
      });

      return {
        success: true,
        content: response.data
      };
    } catch (error) {
      console.error('Error downloading file from OneDrive:', error.response?.data || error.message);
      throw new Error(`Failed to download file: ${error.response?.data?.error?.message || error.message}`);
    }
  }

  /**
   * List files in OneDrive
   */
  async listFiles() {
    try {
      const url = `${this.baseUrl}/me/drive/root/children`;
      
      const response = await axios.get(url, {
        headers: this.headers
      });

      const files = response.data.value.map(file => ({
        id: file.id,
        name: file.name,
        size: file.size,
        createdDateTime: file.createdDateTime,
        lastModifiedDateTime: file.lastModifiedDateTime,
        webUrl: file.webUrl,
        mimeType: file.file?.mimeType || 'folder'
      }));

      return {
        success: true,
        files: files
      };
    } catch (error) {
      console.error('Error listing files from OneDrive:', error.response?.data || error.message);
      throw new Error(`Failed to list files: ${error.response?.data?.error?.message || error.message}`);
    }
  }

  /**
   * Delete a file from OneDrive
   */
  async deleteFile(fileId) {
    try {
      const url = `${this.baseUrl}/me/drive/items/${fileId}`;
      
      await axios.delete(url, {
        headers: this.headers
      });

      return {
        success: true,
        message: 'File deleted successfully'
      };
    } catch (error) {
      console.error('Error deleting file from OneDrive:', error.response?.data || error.message);
      throw new Error(`Failed to delete file: ${error.response?.data?.error?.message || error.message}`);
    }
  }

  /**
   * Search files by name
   */
  async searchFilesByName(filename) {
    try {
      const url = `${this.baseUrl}/me/drive/root/search(q='${filename}')`;
      
      const response = await axios.get(url, {
        headers: this.headers
      });

      const files = response.data.value.map(file => ({
        id: file.id,
        name: file.name,
        size: file.size,
        createdDateTime: file.createdDateTime,
        lastModifiedDateTime: file.lastModifiedDateTime,
        webUrl: file.webUrl
      }));

      return {
        success: true,
        files: files
      };
    } catch (error) {
      console.error('Error searching files in OneDrive:', error.response?.data || error.message);
      throw new Error(`Failed to search files: ${error.response?.data?.error?.message || error.message}`);
    }
  }

  /**
   * Create or get folder by path
   */
  async getOrCreateFolder(folderPath) {
    try {
      // Sanitize folder path to prevent ReDoS
      let folderName = folderPath;
      while (folderName.startsWith('/')) {
        folderName = folderName.substring(1);
      }
      while (folderName.endsWith('/')) {
        folderName = folderName.substring(0, folderName.length - 1);
      }

      // Validate folder name to prevent path traversal
      if (!folderName || folderName.includes('..') || folderName.includes('\\')) {
        throw new Error('Invalid folder path');
      }
      
      // Try to get existing folder
      try {
        const url = `${this.baseUrl}/me/drive/root:/${encodeURIComponent(folderName)}`;
        const response = await axios.get(url, {
          headers: this.headers
        });

        if (response.data.folder) {
          return {
            success: true,
            folder: {
              id: response.data.id,
              name: response.data.name
            }
          };
        }
      } catch (err) {
        // Folder doesn't exist, create it
      }

      // Create folder
      const url = `${this.baseUrl}/me/drive/root/children`;
      const response = await axios.post(url, {
        name: folderName,
        folder: {},
        '@microsoft.graph.conflictBehavior': 'rename'
      }, {
        headers: this.headers
      });

      return {
        success: true,
        folder: {
          id: response.data.id,
          name: response.data.name
        }
      };
    } catch (error) {
      console.error('Error creating/getting folder in OneDrive:', error.response?.data || error.message);
      throw new Error(`Failed to create/get folder: ${error.response?.data?.error?.message || error.message}`);
    }
  }

  /**
   * Upload file to a specific folder
   */
  async uploadFileToFolder(folderId, filename, content) {
    try {
      // Validate filename to prevent path traversal
      if (!filename || filename.includes('..') || filename.includes('/') || filename.includes('\\')) {
        throw new Error('Invalid filename');
      }

      const url = `${this.baseUrl}/me/drive/items/${folderId}:/${encodeURIComponent(filename)}:/content`;
      
      const response = await axios.put(url, content, {
        headers: {
          'Authorization': `Bearer ${this.accessToken}`,
          'Content-Type': 'application/json'
        }
      });

      return {
        success: true,
        file: {
          id: response.data.id,
          name: response.data.name,
          size: response.data.size,
          createdDateTime: response.data.createdDateTime,
          lastModifiedDateTime: response.data.lastModifiedDateTime,
          webUrl: response.data.webUrl
        }
      };
    } catch (error) {
      console.error('Error uploading file to folder in OneDrive:', error.response?.data || error.message);
      throw new Error(`Failed to upload file to folder: ${error.response?.data?.error?.message || error.message}`);
    }
  }

  /**
   * List files in a specific folder
   */
  async listFilesInFolder(folderId) {
    try {
      const url = `${this.baseUrl}/me/drive/items/${folderId}/children`;
      
      const response = await axios.get(url, {
        headers: this.headers
      });

      const files = response.data.value.map(file => ({
        id: file.id,
        name: file.name,
        size: file.size,
        createdDateTime: file.createdDateTime,
        lastModifiedDateTime: file.lastModifiedDateTime,
        webUrl: file.webUrl,
        mimeType: file.file?.mimeType || 'folder'
      }));

      return {
        success: true,
        files: files
      };
    } catch (error) {
      console.error('Error listing files in folder from OneDrive:', error.response?.data || error.message);
      throw new Error(`Failed to list files in folder: ${error.response?.data?.error?.message || error.message}`);
    }
  }

  /**
   * Get file metadata
   */
  async getFileMetadata(fileId) {
    try {
      const url = `${this.baseUrl}/me/drive/items/${fileId}`;
      
      const response = await axios.get(url, {
        headers: this.headers
      });

      return {
        success: true,
        file: {
          id: response.data.id,
          name: response.data.name,
          size: response.data.size,
          createdDateTime: response.data.createdDateTime,
          lastModifiedDateTime: response.data.lastModifiedDateTime,
          webUrl: response.data.webUrl,
          mimeType: response.data.file?.mimeType || 'folder'
        }
      };
    } catch (error) {
      console.error('Error getting file metadata from OneDrive:', error.response?.data || error.message);
      throw new Error(`Failed to get file metadata: ${error.response?.data?.error?.message || error.message}`);
    }
  }
}

module.exports = OneDriveService;
