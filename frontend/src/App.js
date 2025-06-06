import React, { useState, useEffect, useCallback } from 'react';
import './App.css';
import BarChart from './components/BarChart';
import LineChart from './components/LineChart';
import Filters from './components/Filters';
import Login from './components/Login';
import AddVenda from './components/Addvenda';
import DeleteVendaModal from './components/DeleteVendaModal';
import { fetchVendas, fetchVendaById, logout, deleteVenda } from './services/api';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem('authToken'));
  const [vendas, setVendas] = useState([]);
  const [filteredVendas, setFilteredVendas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentFilters, setCurrentFilters] = useState({});
  const [vendaParaDeletar, setVendaParaDeletar] = useState(null);

  const [vendaIdInput, setVendaIdInput] = useState('');
  const [encontradaVenda, setEncontradaVenda] = useState(null);
  const [loadingEncontradaVenda, setLoadingEncontradaVenda] = useState(false);
  const [errorEncontradaVenda, setErrorEncontradaVenda] = useState(null);
  const [buscaRealizada, setBuscaRealizada] = useState(false);
  const [showAddVendaForm, setShowAddVendaForm] = useState(false);

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

  const handleBuscarVendaPorId = async () => {
    if (!vendaIdInput.trim()) {
      alert('Por favor, insira um ID de venda.');
      setEncontradaVenda(null);
      setErrorEncontradaVenda(null);
      setBuscaRealizada(false);
      return;
    }
    setLoadingEncontradaVenda(true);
    setErrorEncontradaVenda(null);
    setEncontradaVenda(null);
    setBuscaRealizada(true);

    try {
      const data = await fetchVendaById(vendaIdInput);
      setEncontradaVenda(data);
      if (data === null) {
        setErrorEncontradaVenda(`Nenhuma venda encontrada com o ID: ${vendaIdInput}`);
      }
    } catch (err) {
      console.error("Erro ao buscar venda por ID no App.js:", err);
      setErrorEncontradaVenda(err.detalhe || err.message || `Falha ao buscar venda com ID: ${vendaIdInput}.`);
    } finally {
      setLoadingEncontradaVenda(false);
    }
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

      <main className="App-main">
        <div className="main-actions">
          <button onClick={() => setShowAddVendaForm(true)} className="add-venda-button">
            + Adicionar Nova Venda
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
      
      <section className="buscar-venda-section">
        <h2>Consultar Venda por ID</h2>
        <div className="form-group">
          <input
            type="number"
            placeholder="Digite o ID da Venda"
            value={vendaIdInput}
            onChange={(e) => {
              setVendaIdInput(e.target.value);
              setBuscaRealizada(false);
              setEncontradaVenda(null);
              setErrorEncontradaVenda(null);
            }}
            className="form-input"
          />
          <button
            onClick={handleBuscarVendaPorId}
            disabled={loadingEncontradaVenda || !vendaIdInput.trim()}
            className="form-button"
          >
            {loadingEncontradaVenda ? 'Buscando...' : 'Buscar Venda'}
          </button>
        </div>

        {loadingEncontradaVenda && <p className="App-loading">Consultando venda...</p>}

        {errorEncontradaVenda && !loadingEncontradaVenda && (
          <p className="App-error">Erro: {errorEncontradaVenda}</p>
        )}

        {buscaRealizada && !loadingEncontradaVenda && !errorEncontradaVenda && encontradaVenda && (
          <div className="venda-detalhes">
            <h3>Detalhes da Venda ID: {encontradaVenda.id}</h3>
            <p><strong>Nome do Produto:</strong> {encontradaVenda.nomeProduto}</p>
            <p><strong>Quantidade Vendida:</strong> {encontradaVenda.quantidadeVendida}</p>
            <p><strong>Data da Venda:</strong> {new Date(encontradaVenda.dataVenda + 'T00:00:00').toLocaleDateString('pt-BR')}</p>
            <p><strong>Valor Total:</strong> {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(encontradaVenda.valorTotal)}</p>
          </div>
        )}

        {buscaRealizada && !loadingEncontradaVenda && !errorEncontradaVenda && !encontradaVenda && (
           <p>Nenhuma venda encontrada com o ID: {vendaIdInput}.</p>
        )}
      </section>
    </div>
  );
}

export default App;