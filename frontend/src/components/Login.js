import React, { useState } from 'react';
import { login } from '../services/api';

// O componente recebe uma função 'onLoginSuccess' como prop
// para notificar o App.js quando o login for bem-sucedido.
const Login = ({ onLoginSuccess }) => {
  const [usuario, setUsuario] = useState('');
  const [senha, setSenha] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault(); // Impede o recarregamento da página
    setLoading(true);
    setError('');

    try {
      // Chama a função de login da nossa api.js
      await login({ usuario, senha });
      // Se chegou aqui, o login funcionou
      onLoginSuccess();
    } catch (err) {
      // Se a API retornar um erro (ex: 401 Unauthorized), ele será capturado aqui
      console.error('Falha no login:', err);
      setError('Usuário ou senha inválidos. Por favor, tente novamente.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <form onSubmit={handleSubmit} className="login-form">
        <h2>Login</h2>
        {error && <p className="error-message">{error}</p>}
        <div className="form-group">
          <label htmlFor="usuario">Usuário</label>
          <input
            type="text"
            id="usuario"
            value={usuario}
            onChange={(e) => setUsuario(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="senha">Senha</label>
          <input
            type="password"
            id="senha"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            required
          />
        </div>
        <button type="submit" disabled={loading} className="form-button">
          {loading ? 'Entrando...' : 'Entrar'}
        </button>
      </form>
    </div>
  );
};

export default Login;