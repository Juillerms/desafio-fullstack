import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080'; 

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export const fetchVendas = async (params) => {
  try {
    const response = await apiClient.get('/vendas', { params });
    return response.data; 
  } catch (error) {
    throw error;
  }
};

export const login = async (credentials) => {
  try {
    const response = await apiClient.post('/auth/login', credentials); // Certifique-se que o endpoint é /auth/login
    if (response.data && response.data.token) { // Ajuste conforme a estrutura da sua resposta de login
      localStorage.setItem('authToken', response.data.token);
      // Não precisa mais setar o header default aqui se você usa o interceptor de request
    }
    return response.data;
  } catch (error) {
    localStorage.removeItem('authToken'); // Limpa token em caso de falha no login
    throw error;
  }
};

export const logout = () => {
    localStorage.removeItem('authToken');
};

export const fetchVendaById = async (idVenda) => {
  if (!idVenda) {
    return Promise.reject(new Error('ID da venda não fornecido.'));
  }
  try {
    const response = await apiClient.get(`/vendas/${idVenda}`);
    return response.data; 
  } catch (error) {
    if (error.response && error.response.status === 404) {
      return null;
    }
    console.error(`Erro ao buscar venda com ID ${idVenda}:`, error.response?.data || error.message);
    throw error.response?.data || { message: error.message || 'Erro desconhecido ao buscar venda.' };
  }
};

export const addVenda = async (vendaData) => {
  try {
    const response = await apiClient.post('/vendas', vendaData);
    return response.data;
  } catch (error) {
    console.error('Erro ao adicionar venda:', error.response?.data || error.message);
    throw error.response?.data || { message: 'Falha ao criar nova venda.' };
  }
};

export const deleteVenda = async (idVenda) => {
  try {
    const response = await apiClient.delete(`/vendas/${idVenda}`);
    return response.data;
  } catch (error) {
    console.error(`Erro ao deletar venda com ID ${idVenda}:`, error.response?.data || error.message);
    throw error.response?.data || { message: 'Falha ao deletar a venda.' };
  }
};

export default apiClient;