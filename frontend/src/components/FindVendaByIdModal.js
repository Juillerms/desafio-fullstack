import React, { useState } from 'react';
import { fetchVendaById } from '../services/api';

const FindVendaByIdModal = ({ onClose }) => {
  const [vendaIdInput, setVendaIdInput] = useState('');
  const [encontradaVenda, setEncontradaVenda] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [buscaRealizada, setBuscaRealizada] = useState(false);

  const handleBuscarVenda = async () => {
    if (!vendaIdInput.trim()) {
      setError('Por favor, insira um ID de venda.');
      return;
    }
    setLoading(true);
    setError(null);
    setEncontradaVenda(null);
    setBuscaRealizada(true);

    try {
      const data = await fetchVendaById(vendaIdInput);
      setEncontradaVenda(data);
      if (data === null) {
        setError(`Nenhuma venda encontrada com o ID: ${vendaIdInput}`);
      }
    } catch (err) {
      setError(err.message || `Falha ao buscar venda com ID: ${vendaIdInput}.`);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    setVendaIdInput(e.target.value);
    // Limpa os resultados anteriores ao digitar um novo ID
    if (buscaRealizada) {
      setBuscaRealizada(false);
      setEncontradaVenda(null);
      setError(null);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2>Consultar Venda por ID</h2>
        <div className="form-group">
          <input
            type="number"
            placeholder="Digite o ID da Venda"
            value={vendaIdInput}
            onChange={handleInputChange}
            className="form-input"
          />
          <button
            onClick={handleBuscarVenda}
            disabled={loading || !vendaIdInput.trim()}
            className="form-button"
          >
            {loading ? 'Buscando...' : 'Buscar Venda'}
          </button>
        </div>

        {loading && <p className="App-loading">Consultando venda...</p>}
        {error && !loading && (
          <p className="App-error">{error}</p>
        )}

        {buscaRealizada && !loading && !error && encontradaVenda && (
          <div className="venda-detalhes">
            <h3>Detalhes da Venda ID: {encontradaVenda.id}</h3>
            <p><strong>Nome do Produto:</strong> {encontradaVenda.nomeProduto}</p>
            <p><strong>Quantidade Vendida:</strong> {encontradaVenda.quantidadeVendida}</p>
            <p><strong>Data da Venda:</strong> {new Date(encontradaVenda.dataVenda + 'T00:00:00').toLocaleDateString('pt-BR')}</p>
            <p><strong>Valor Total:</strong> {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(encontradaVenda.valorTotal)}</p>
          </div>
        )}
        
        <div className="modal-actions">
          <button onClick={onClose} className="button-secondary">
            Fechar
          </button>
        </div>
      </div>
    </div>
  );
};

export default FindVendaByIdModal;