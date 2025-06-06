import React, { useState, useEffect, useCallback } from 'react';
import './App.css';
import BarChart from './components/BarChart';
import LineChart from './components/LineChart';
import Filters from './components/Filters';
import Login from './components/Login';
import AddVenda from './components/Addvenda';
import DeleteVendaModal from './components/DeleteVendaModal';
import FindVendaByIdModal from './components/FindVendaByIdModal'; 
import { fetchVendas, deleteVenda, logout } from './services/api'; 

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem('authToken'));
  const [vendas, setVendas] = useState([]);
  const [filteredVendas, setFilteredVendas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentFilters, setCurrentFilters] = useState({});
  const [vendaParaDeletar, setVendaParaDeletar] = useState(null);
  const [showAddVendaForm, setShowAddVendaForm] = useState(false);
  const [showFindVendaModal, setShowFindVendaModal] = useState(false); 
  
  const handleLogout = useCallback(() => {
    logout();
    setIsAuthenticated(false);
  }, []);

  const loadVendas = useCallback(async (filters = {}) => {
    setLoading(true);
    setError(null);
    try {
      const activeFilters = Object.entries(filters)
        .filter(([_, value]) => value !== null && value !== '')
        .reduce((obj, [key, value]) => {
          obj[key] = value;
          return obj;
        }, {});

      const data = await fetchVendas(activeFilters);
      setVendas(data || []);
      setFilteredVendas(data || []);
    } catch (err) {
      console.error("Erro ao buscar vendas no App.js:", err);
      if (err.status === 401 || err.status === 403) {
        handleLogout();
      } else {
        setError(err.detalhe || err.message || 'Falha ao carregar os dados das vendas.');
      }
    } finally {
      setLoading(false);
    }
  }, [handleLogout]); 

  const handleVendaAdded = useCallback(() => {
    setShowAddVendaForm(false);
    loadVendas(currentFilters);
  }, [currentFilters, loadVendas]); 

  const handleDeleteRequest = (venda) => {
    setVendaParaDeletar(venda);
  };

  const handleConfirmDelete = async () => {
    if (!vendaParaDeletar) return;

    try {
        await deleteVenda(vendaParaDeletar.id);
        setVendaParaDeletar(null);
        loadVendas(currentFilters);
    } catch (err) {
        setError(err.message || 'Falha ao deletar a venda.');
    }
  };

  const handleCancelDelete = () => {
    setVendaParaDeletar(null);
  };

  useEffect(() => {
    if (isAuthenticated) {
      loadVendas();
    } else {
      setLoading(false);
      setVendas([]);
      setFilteredVendas([]);
    }
  }, [isAuthenticated, loadVendas]);

  const handleLoginSuccess = () => {
    setIsAuthenticated(true);
  };

  const handleFilterChange = (filters) => {
    setCurrentFilters(filters);
    loadVendas(filters);
  };

  if (!isAuthenticated) {
    return <Login onLoginSuccess={handleLoginSuccess} />;
  }

  return (
    <div className="App">
      <header className="App-header">
        <h1>Dashboard de Vendas</h1>
        <button onClick={handleLogout} className="logout-button">Sair</button>
      </header>

      {showAddVendaForm && (
        <AddVenda
          onVendaAdded={handleVendaAdded}
          onClose={() => setShowAddVendaForm(false)}
        />
      )}

      {vendaParaDeletar && (
        <DeleteVendaModal
          venda={vendaParaDeletar}
          onConfirm={handleConfirmDelete}
          onClose={handleCancelDelete}
        />
      )}

      {showFindVendaModal && (
        <FindVendaByIdModal onClose={() => setShowFindVendaModal(false)} />
      )}

      <main className="App-main">
        <div className="main-actions">
          <button onClick={() => setShowAddVendaForm(true)} className="add-venda-button">
            + Adicionar Nova Venda
          </button>
          <button onClick={() => setShowFindVendaModal(true)} className="form-button" style={{marginLeft: '10px'}}>
            Consultar por ID
          </button>
        </div>

        <Filters onFilterChange={handleFilterChange} />
        
        {loading && <div className="App-loading">Carregando dados...</div>}
        {error && <div className="App-error">Erro: {error}</div>}
        
        {!loading && !error && (
          <>
            <section className="todas-vendas-section">
              <h2>Relatório de Vendas</h2>
              {filteredVendas.length > 0 ? (
                <div className="table-container">
                  <table>
                    <thead>
                      <tr>
                        <th>ID da Venda</th>
                        <th>Nome do Produto</th>
                        <th>Quantidade</th>
                        <th>Data da Venda</th>
                        <th>Valor Total</th>
                        <th>Ações</th>
                      </tr>
                    </thead>
                    <tbody>
                      {filteredVendas.map((venda) => (
                        <tr key={venda.id}>
                          <td>{venda.id}</td>
                          <td>{venda.nomeProduto}</td>
                          <td>{venda.quantidadeVendida}</td>
                          <td>{new Date(venda.dataVenda + 'T00:00:00').toLocaleDateString('pt-BR')}</td>
                          <td>{new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(venda.valorTotal)}</td>
                          <td>
                            <button onClick={() => handleDeleteRequest(venda)} className="delete-button">Deletar</button>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              ) : (
                <p>Nenhuma venda encontrada para os filtros aplicados.</p>
              )}
            </section>

            {filteredVendas.length > 0 && (
              <>
                <div className="chart-container">
                  <BarChart vendasData={filteredVendas} title="Vendas por Produto" />
                </div>
                <div className="chart-container">
                  <LineChart vendasData={filteredVendas} title="Evolução do Valor das Vendas" />
                </div>
              </>
            )}
          </>
        )}
      </main>
    </div>
  );
}

export default App;