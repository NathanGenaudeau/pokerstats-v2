import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

export const getTournaments = async (pseudo?: string) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/tournaments`, {
      params: { pseudo },
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching tournaments:', error);
    throw error;
  }
};

export const getTournamentById = async (id: string) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/tournaments/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching tournament with ID ${id}:`, error);
    throw error;
  }
};

export const createTournament = async (tournamentData: any) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/tournaments`, tournamentData);
    return response.data;
  } catch (error) {
    console.error('Error creating tournament:', error);
    throw error;
  }
};

export const updateTournament = async (id: string, tournamentData: any) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/tournaments/${id}`, tournamentData);
    return response.data;
  } catch (error) {
    console.error(`Error updating tournament with ID ${id}:`, error);
    throw error;
  }
};

export const deleteTournament = async (id: string) => {
  try {
    const response = await axios.delete(`${API_BASE_URL}/tournaments/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error deleting tournament with ID ${id}:`, error);
    throw error;
  }
};

export const getTournamentsBuyin = async () => {
  try {
    console.log('getTournamentsBuyIn');
    const response = await axios.get(`${API_BASE_URL}/tournaments/buyin`);
    return response.data;
  } catch (error) {
    console.error('Error fetching tournaments buy-in:', error);
    throw error;
  }
}