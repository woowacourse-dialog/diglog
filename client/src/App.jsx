import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Signup from './pages/Signup/Signup';
import MyPage from './pages/MyPage/MyPage';
import './App.css';
import DiscussionCreateFormPage from './pages/discussion/create/DiscussionCreateFormPage';

function App() {
  return (
    <Router>
      <div className="app">
        <main className="main-content">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/signup" element={<Signup />} />
            <Route path="/mypage" element={<MyPage />} />
            <Route path="/discussion/new" element={<DiscussionCreateFormPage />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
