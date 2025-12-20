import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import MediaList from './pages/MediaList';
import AddMedia from './pages/AddMedia';
import Stats from './pages/Stats';

function App() {
  return (
      <Router>
        <div className="App">
          <nav style={{ padding: "1rem", background: "#f4f4f4", marginBottom: "1rem" }}>
            <Link to="/" style={{ marginRight: "1rem" }}>Dashboard</Link>
            <Link to="/add" style={{ marginRight: "1rem" }}>Add Entries</Link>
            <Link to="/stats">Statistics</Link>
          </nav>

          <Routes>
            <Route path="/" element={<MediaList />} />
            <Route path="/add" element={<AddMedia />} />
            <Route path="/stats" element={<Stats />} />
            {/* We will add the /edit/:id route later */}
          </Routes>
        </div>
      </Router>
  );
}

export default App;