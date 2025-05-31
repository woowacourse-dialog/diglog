import React from 'react';
import { Link } from 'react-router-dom';
import dialogIcon from '../../assets/favicon_navy.ico'
import './Header.css';

const Header = () => {

  return (
    <header className="header">
      <div className="header-container">
        <div className="header-logo">
          <Link to="/">
            <img src={dialogIcon} alt="Dialog Icon" className="header-icon" />
            <span>Dialog</span>
          </Link>
        </div>
      </div>
    </header>
  );
};

export default Header; 