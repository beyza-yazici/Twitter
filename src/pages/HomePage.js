import { useState } from 'react';
import Tweets from '../components/Tweets';
import axiosInstance from '../utils/axiosConfig';
import '../css/HomePage.css';

const HomePage = ({ user }) => {
  const [tweetContent, setTweetContent] = useState('');
  const [refreshTweets, setRefreshTweets] = useState(false);

  const handleTweetSubmit = async (e) => {
    e.preventDefault();
    if (!tweetContent.trim()) return;
    
    try {
      await axiosInstance.post('/tweet/create', { 
        content: tweetContent,
        userId: user.id 
      }, {
        withCredentials: true
      });
      
      setTweetContent('');
      
      setRefreshTweets(!refreshTweets);
    } catch (error) {
      console.error('Tweet oluşturma hatası:', error);
    }
  };

  return (
    <div className="home-page">
      <div className="home-container">
        
        <div className="sidebar">
          <div className="logo">
            <img src="/twitter-logo.png" alt="Twitter Logo" />
          </div>
          
          <nav className="menu">
            <ul>
              <li className="active">Anasayfa</li>
              <li>Keşfet</li>
              <li>Bildirimler</li>
              <li>Mesajlar</li>
              <li>Yer İşaretleri</li>
              <li>Listeler</li>
              <li>Profil</li>
              <li>Daha fazla</li>
            </ul>
          </nav>
          
          <button className="tweet-button">Tweetle</button>
        </div>
        
        <div className="main-content">
          <div className="header">
            <h2>Anasayfa</h2>
            <div className="tabs">
              <div className="tab active">Sana özel</div>
              <div className="tab">Takip edilenler</div>
            </div>
          </div>
          
          <div className="tweet-compose">
            <img src={user?.profileImage || "https://via.placeholder.com/40"} alt="Profil" className="avatar" />
            <div className="tweet-form">
              <textarea
                value={tweetContent}
                onChange={(e) => setTweetContent(e.target.value)}
                placeholder="Neler oluyor?"
                maxLength={280}
              />
              <div className="tweet-actions">
                <div className="tweet-icons">
                  <span>🖼️</span>
                  <span>📊</span>
                  <span>😊</span>
                  <span>📅</span>
                  <span>📍</span>
                </div>
                <button 
                  onClick={handleTweetSubmit} 
                  className="tweet-submit"
                  disabled={!tweetContent.trim()}
                >
                  Tweetle
                </button>
              </div>
            </div>
          </div>
          
          <Tweets userId={user?.id} refresh={refreshTweets} />
        </div>
      </div>
    </div>
  );
};

export default HomePage;