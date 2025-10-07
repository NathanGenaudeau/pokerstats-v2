import axios from 'axios';

const API_BASE_URL = (import.meta as any).env?.VITE_API_BASE_URL ?? 'http://localhost:8080';

export const getHands = async (pseudo?: string) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/hands`, {
      params: { pseudo },
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching hands:', error);
    throw error;
  }
};

export const getHandById = async (id: string) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/hands/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching hand with ID ${id}:`, error);
    throw error;
  }
};

export const createHand = async (handData: any) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/hands`, handData);
    return response.data;
  } catch (error) {
    console.error('Error creating hand:', error);
    throw error;
  }
};

export const updateHand = async (id: string, handData: any) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/hands/${id}`, handData);
    return response.data;
  } catch (error) {
    console.error(`Error updating hand with ID ${id}:`, error);
    throw error;
  }
};

export const deleteHand = async (id: string) => {
  try {
    const response = await axios.delete(`${API_BASE_URL}/hands/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error deleting hand with ID ${id}:`, error);
    throw error;
  }
};