import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

export const getResults = async (pseudo?: string) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/results`, {
      params: { pseudo },
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching results:', error);
    throw error;
  }
};

export const getResultById = async (id: string) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/results/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching result with ID ${id}:`, error);
    throw error;
  }
};

export const createResult = async (resultData: any) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/results`, resultData);
    return response.data;
  } catch (error) {
    console.error('Error creating result:', error);
    throw error;
  }
};

export const updateResult = async (id: string, resultData: any) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/results/${id}`, resultData);
    return response.data;
  } catch (error) {
    console.error(`Error updating result with ID ${id}:`, error);
    throw error;
  }
};

export const deleteResult = async (id: string) => {
  try {
    const response = await axios.delete(`${API_BASE_URL}/results/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error deleting result with ID ${id}:`, error);
    throw error;
  }
};