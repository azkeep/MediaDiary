import React, {useState, useEffect} from 'react';
import axios from "axios";
import MediaList from "./MediaList";

const StatsDisplay = ({ title, onClose }) => {
    const [stats, setStats] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!title) return;

        // Call the Go service on port 8181
        axios.get(`http://localhost:8181/stats?title=${encodeURIComponent(title)}`)
            .then(res => {
                setStats(res.data);
                setError(null);
            })
            .catch(err => {
                if (err.response && err.response.status === 404) {
                    setError("No historical data found.");
                } else {
                    setError("Stats service unreachable.");
                }
                setStats(null);
            });
    }, [title]);

    return (
        <div className="stats-sidebar" style={{ padding: '15px', border: '1px solid #ccc', borderRadius: '8px', background: '#f9f9f9', marginTop: '20px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <h3>Quick Stats: {title}</h3>
                <button onClick={onClose}>&times;</button>
            </div>
            {error && <p style={{ color: 'orange' }}>{error}</p>}
            {stats && (
                <div style={{ display: 'flex', gap: '20px' }}>
                    <div>All Time: <strong>{stats.total}</strong></div>
                    <div>Last 30 Days: <strong>{stats.last_30_days}</strong></div>
                    <div>Last 180 Days: <strong>{stats.last_180_days}</strong></div>
                </div>
            )}
        </div>
    );
};

export default StatsDisplay;