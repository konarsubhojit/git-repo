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
}

module.exports = OneDriveService;
