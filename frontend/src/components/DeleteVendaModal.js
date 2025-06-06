import React from 'react';

const DeleteVendaModal = ({ venda, onConfirm, onClose }) => {
  if (!venda) return null;

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2>Confirmar Exclusão</h2>
        <p>
          Você tem certeza que deseja deletar a venda do produto 
          <strong> {venda.nomeProduto}</strong> (ID: {venda.id})?
        </p>
        <p>Esta ação não pode ser desfeita.</p>
        <div className="modal-actions">
          <button onClick={onClose} className="button-secondary">
            Cancelar
          </button>
          <button onClick={onConfirm} className="delete-button">
            Confirmar Exclusão
          </button>
        </div>
      </div>
    </div>
  );
};

export default DeleteVendaModal;