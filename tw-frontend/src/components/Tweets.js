import React, { useState, useEffect } from 'react';
import axiosInstance from '../utils/axiosConfig';

const Tweets = ({ userId, refresh = false }) => {
  const [tweets, setTweets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchTweets = async () => {
      try {
        setLoading(true);
        const response = await axiosInstance.get(`/tweet/findByUserId?userId=${userId}`, {
          withCredentials: true
        });
        setTweets(response.data);
        setError(null);
      } catch (error) {
        setError("Tweet'leri yüklerken bir hata oluştu: " + error.message);
        console.error("Hata detayı:", error);
      } finally {
        setLoading(false);
      }
    };
    
    if (userId) {
      fetchTweets();
    }
  }, [userId, refresh]);

  if (loading) return <div className="loading-spinner">Yükleniyor...</div>;
  if (error) return <div className="error-message">{error}</div>;

  return (
    <div className="tweets-container">
      {tweets.length === 0 ? (
        <div className="no-tweets-message">
          <p>Henüz tweet bulunmamaktadır.</p>
          <p>İlk tweetinizi oluşturarak başlayın!</p>
        </div>
      ) : (
        <div className="tweets-list">
          {tweets.map((tweet) => (
            <div key={tweet.id} className="tweet-item">
              <div className="tweet-avatar">
                <img src={tweet.user?.profileImage || "https://via.placeholder.com/40"} alt="Avatar" />
              </div>
              
              <div className="tweet-content">
                <div className="tweet-header">
                  <span className="tweet-name">{tweet.user?.name || "Kullanıcı"}</span>
                  <span className="tweet-username">@{tweet.user?.username || "kullanici"}</span>
                  <span className="tweet-date">{new Date(tweet.createdAt).toLocaleString()}</span>
                </div>
                
                <div className="tweet-text">{tweet.content}</div>
                
                <div className="tweet-actions">
                  <div className="tweet-action">
                    <span>💬</span>
                  </div>
                  <div className="tweet-action">
                    <span>🔄</span>
                  </div>
                  <div className="tweet-action">
                    <span>❤️</span>
                  </div>
                  <div className="tweet-action">
                    <span>📊</span>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Tweets;