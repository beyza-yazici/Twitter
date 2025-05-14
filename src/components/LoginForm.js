import React, { useState } from 'react';
import axiosInstance from '../utils/axiosConfig';

const LoginForm = ({ onLoginSuccess }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      const response = await axiosInstance.post('/auth/login', 
        { username, password },
        { headers: { 'Content-Type': 'application/json' }, withCredentials: true }
      );

      if (response.status === 200) {
        onLoginSuccess(response.data.user);
      }
    } catch (err) {
      console.error(err);
      setError('Giriş başarısız. Lütfen bilgilerinizi kontrol edin.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <form onSubmit={handleLogin} className="login-form">
      <div className="form-group">
        <input
          type="text"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
          placeholder="Telefon, e-posta veya kullanıcı adı"
          className="form-input"
        />
      </div>

      <div className="form-group">
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          placeholder="Şifre"
          className="form-input"
        />
      </div>

      {error && <p className="error-message">{error}</p>}

      <button 
        type="submit" 
        className="login-button" 
        disabled={isLoading}
      >
        {isLoading ? 'Giriş yapılıyor...' : 'Giriş yap'}
      </button>
    </form>
  );
};

export default LoginForm;