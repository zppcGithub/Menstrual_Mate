
import axios from 'axios';

const apiClient = axios.create({
  baseURL: '/api', // The base URL for all API requests
  headers: {
    'Content-Type': 'application/json',
  },
});

// You can add interceptors here for request and response handling
// For example, to handle loading states or global error notifications

apiClient.interceptors.request.use(
  (config) => {
    // You can add a loading indicator start here
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

apiClient.interceptors.response.use(
  (response) => {
    // You can add a loading indicator stop here
    return response.data;
  },
  (error) => {
    // Handle global errors here, e.g., show a notification
    console.error('API Error:', error.response?.data || error.message);
    return Promise.reject(error);
  }
);

export default apiClient;
