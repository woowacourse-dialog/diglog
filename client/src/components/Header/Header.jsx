import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import dialogIcon from '../../assets/favicon_navy.ico'
import './Header.css';

const Header = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    checkLoginStatus();
  }, []);

  const checkLoginStatus = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/login/check', {
        credentials: 'include'
      });
      const responseData = await response.json();
      setIsLoggedIn(responseData.data.isLoggedIn);
    } catch (error) {
      console.error('Failed to check login status:', error);
      setIsLoggedIn(false);
    }
  };

  const handleGithubLogin = () => {
    const githubURL = `http://localhost:8080/oauth2/authorization/github`;
    window.location.href = githubURL;
  };

  const handleMyPage = () => {
    navigate('/mypage');
  };

  const handleLogout = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/logout', {
        method: 'DELETE',
        credentials: 'include'
      });
      if (response.ok) {
        setIsLoggedIn(false);
        navigate('/');
      }
    } catch (error) {
      console.error('Failed to logout:', error);
    }
  };

  return (
    <header className="header">
      <div className="header-container">
        <div className="header-logo">
          <Link to="/">
            <img src={dialogIcon} alt="Dialog Icon" className="header-icon" />
            <span>Dialog</span>
          </Link>
        </div>
        <div className="header-nav">
          {isLoggedIn ? (
            <div className="nav-buttons">
              <button className="nav-button mypage-button" onClick={handleMyPage}>
                My Page
              </button>
              <button className="nav-button logout-button" onClick={handleLogout}>
                Logout
              </button>
            </div>
          ) : (
            <button className="nav-button login-button" onClick={handleGithubLogin}>
              Sign in with GitHub
            </button>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header; 