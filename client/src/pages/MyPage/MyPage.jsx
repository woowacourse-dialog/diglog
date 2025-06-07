import React from 'react';
import Header from '../../components/Header/Header';
import './MyPage.css';

const MyPage = () => {
  return (
    <div className="mypage-container">
      <Header />
      <div className="mypage-content">
        <h1>My Page</h1>
        {/* Add your MyPage content here */}
      </div>
    </div>
  );
};

export default MyPage; 