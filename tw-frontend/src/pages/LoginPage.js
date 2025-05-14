
import LoginForm from '../components/LoginForm';
import '../css/LoginPage.css';

const LoginPage = ({ onLoginSuccess }) => {
  return (
    <div className="login-page">
      <div className="login-container">
        <div className="login-logo">
          <img src="/twitter-logo.png" alt="Twitter Logo" className="twitter-logo" />
        </div>
        <h1>Twitter'a giriş yap</h1>
        <LoginForm onLoginSuccess={onLoginSuccess} />
      </div>
    </div>
  );
};

export default LoginPage;