import React, { useState } from 'react';
import { addVenda } from '../services/api';

const AddVenda = ({ onVendaAdded, onClose }) => {
  const [nomeProduto, setNomeProduto] = useState('');
  const [quantidadeVendida, setQuantidadeVendida] = useState('');
  const [dataVenda, setDataVenda] = useState('');
  const [valorTotal, setValorTotal] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    const vendaData = {
      nomeProduto,
      quantidadeVendida: parseInt(quantidadeVendida, 10),
      dataVenda,
      valorTotal: parseFloat(valorTotal),
    };

    try {
      await addVenda(vendaData);
      onVendaAdded(); 
    } catch (err) {
      setError(err.message || 'Ocorreu um erro ao salvar a venda.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <form onSubmit={handleSubmit}>
          <h2>Adicionar Nova Venda</h2>
          {error && <p className="error-message">{error}</p>}

          <div className="form-group">
            <label htmlFor="nomeProduto">Nome do Produto</label>
            <input type="text" id="nomeProduto" value={nomeProduto} onChange={(e) => setNomeProduto(e.target.value)} required />
          </div>
          <div className="form-group">
            <label htmlFor="quantidadeVendida">Quantidade Vendida</label>
            <input type="number" id="quantidadeVendida" value={quantidadeVendida} onChange={(e) => setQuantidadeVendida(e.target.value)} required />
          </div>
          <div className="form-group">
            <label htmlFor="dataVenda">Data da Venda</label>
            <input type="date" id="dataVenda" value={dataVenda} onChange={(e) => setDataVenda(e.target.value)} required />
          </div>
          <div className="form-group">
            <label htmlFor="valorTotal">Valor Total (R$)</label>
            <input type="number" step="0.01" id="valorTotal" value={valorTotal} onChange={(e) => setValorTotal(e.target.value)} required />
          </div>

          <div className="modal-actions">
            <button type="button" onClick={onClose} className="button-secondary" disabled={loading}>Cancelar</button>
            <button type="submit" className="form-button" disabled={loading}>
              {loading ? 'Salvando...' : 'Salvar Venda'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddVenda;