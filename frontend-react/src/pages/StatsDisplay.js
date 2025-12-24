import React, {useState, useEffect, useRef} from 'react';
import axios from 'axios';
import {PieChart, Pie, Cell, ResponsiveContainer} from 'recharts';
import styles from './StatsDisplay.module.css';

const StatsDisplay = ({title}) => {
    const [stats, setStats] = useState(null);
    const containerRef = useRef(null);

    useEffect(() => {
        axios.get(`http://localhost:8181/stats?title=${encodeURIComponent(title)}`)
            .then(res => setStats(res.data))
            .catch(err => console.error(err));
    }, [title]);

    if (!stats) return <div className="loading-small">...</div>;

    // Prepare data for the Doughnut
    // We calculate "Older" by subtracting 30-day count from Total
    const data = [
        {name: '7d', value: stats.last_7_days},
        {name: '30d', value: stats.last_30_days - stats.last_7_days},
        {name: 'Older', value: stats.total - stats.last_30_days},
    ];

// These strings must match the variable names in your CSS file
    const colors = [
        'var(--color-7d)',
        'var(--color-30d)',
        'var(--color-older)'
    ];

    return (
        <div ref={containerRef} className={styles.chartContainer}>
            <span style={{fontSize: '0.8rem', fontWeight: 'bold'}}>{title} Activity</span>

            <div style={{width: '100%', height: 120, position: 'relative'}}>
                <ResponsiveContainer>
                    <PieChart>
                        <Pie
                            data={data}
                            innerRadius={35}
                            outerRadius={50}
                            paddingAngle={5}
                            dataKey="value"
                            stroke="none"
                        >
                            {data.map((entry, index) => (
                                // Recharts can actually resolve 'var()' strings in many browsers!
                                <Cell key={`cell-${index}`} fill={colors[index]}/>
                            ))}
                        </Pie>
                    </PieChart>
                </ResponsiveContainer>

                {/* Center Text */}
                <div style={{
                    position: 'absolute',
                    top: '50%',
                    left: '50%',
                    transform: 'translate(-50%, -50%)',
                    fontSize: '1rem',
                    fontWeight: 'bold'
                }}>
                    {stats.total}
                </div>
            </div>

            <div className={styles.legend}>
                <span><b className={styles.dot7d}>●</b> 7d</span>
                <span><b className={styles.dot30d}>●</b> 30d</span>
                <span><b className={styles.dotOlder}>●</b> Older</span>
            </div>
        </div>
    );
};

export default StatsDisplay;