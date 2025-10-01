import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

export const getPlayers = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/players`);
    return response.data;
  } catch (error) {
    console.error('Error fetching players:', error);
    throw error;
  }
};

export const getPlayerByPseudo = async (pseudo: string) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/players/${pseudo}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching player with pseudo ${pseudo}:`, error);
    throw error;
  }
};

export const createPlayer = async (playerData: any) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/players`, playerData);
    return response.data;
  } catch (error) {
    console.error('Error creating player:', error);
    throw error;
  }
};

export const updatePlayer = async (id: string, playerData: any) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/players/${id}`, playerData);
    return response.data;
  } catch (error) {
    console.error(`Error updating player with ID ${id}:`, error);
    throw error;
  }
};

export const deletePlayer = async (id: string) => {
  try {
    const response = await axios.delete(`${API_BASE_URL}/players/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error deleting player with ID ${id}:`, error);
    throw error;
  }
};