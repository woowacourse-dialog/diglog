import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login/Login';
import Signup from './pages/Signup/Signup';
import './App.css';

function App() {
  return (
    <Router>
      <div className="app">
        <main className="main-content">
          <Routes>
            <Route path="/" element={<Home />} />
          </Routes>

            <Routes>
              <Route path="/login" element={<Login />} />
            </Routes>

            <Routes>
              <Route path="/signup" element={<Signup />} />
            </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
