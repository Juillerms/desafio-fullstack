import React, { useState } from 'react';

const Filters = ({ onFilterChange }) => {
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');

  const handleApplyFilters = () => {
    onFilterChange({
      dataInicio: startDate || null, 
      dataFim: endDate || null,
    });
  };

  return (
    <div style={{ marginBottom: '20px', padding: '10px', border: '1px solid #ccc' }}>
      <h4>Filtrar Dados</h4>
      <div>
        <label htmlFor="startDate" style={{ marginRight: '10px' }}>Data Início:</label>
        <input
          type="date"
          id="startDate"
          value={startDate}
          onChange={(e) => setStartDate(e.target.value)}
          style={{ marginRight: '20px' }}
        />
        <label htmlFor="endDate" style={{ marginRight: '10px' }}>Data Fim:</label>
        <input
          type="date"
          id="endDate"
          value={endDate}
          onChange={(e) => setEndDate(e.target.value)}
          style={{ marginRight: '20px' }}
        />
      </div>
      <button onClick={handleApplyFilters} style={{ marginTop: '10px' ,padding: "8px 10px", backgroundColor:"#4BC0C0",color:'white', border:"none", borderRadius: "4px", cursor: "pointer", fontSize: "1rem"}}>
        Aplicar Filtros
      </button>
    </div>
  );
};

export default Filters;