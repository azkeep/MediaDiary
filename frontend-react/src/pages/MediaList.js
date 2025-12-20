import React, { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
// import { Edit, Trash2, Calendar } from 'lucide-react';
import { Edit, Trash2 } from 'lucide-react';

const MediaList = () => {
    const [entries, setEntries] = useState([]);
    const [days, setDays] = useState(30);
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 20;

    // We wrap this in useCallback so it doesn't change on every render
    const loadEntries = useCallback(async () => {
        try {
            const response = await axios.get(`/api/entries/${days}`);
            setEntries(response.data || []);
            setCurrentPage(1);
        } catch (error) {
            console.error("Failed to load entries", error);
        }
    }, [days]); // It only recreates if 'days' changes

    useEffect(() => {
        loadEntries();
    }, [loadEntries]); // Now loadEntries is a safe dependency

    const handleDelete = async (id) => {
        if (window.confirm("Are you sure you want to delete this entry?")) {
            await axios.delete(`/api/entries/${id}`);
            loadEntries(); // Refresh list
        }
    };

    // Simple Pagination Logic
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentItems = entries.slice(indexOfFirstItem, indexOfLastItem);

    return (
        <div className="container">
            <h2>Media Entries</h2>
            <div className="filters">
                {[3, 7, 30, 180].map(d => (
                    <button key={d} onClick={() => setDays(d)} className={days === d ? 'active' : ''}>
                        Last {d} Days
                    </button>
                ))}
                <button onClick={() => setDays(36500)}>View All</button>
            </div>

            <table>
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Date</th>
                    <th>Type</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {currentItems.map(item => (
                    <tr key={item.id}>
                        <td>{item.title}</td>
                        <td>{item.date}</td>
                        <td>{item.type}</td>
                        <td>
                            <button onClick={() => window.location.href=`/edit/${item.id}`}><Edit size={16}/></button>
                            <button onClick={() => handleDelete(item.id)}><Trash2 size={16}/></button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            {/* Pagination Controls */}
            <div className="pagination">
                <button disabled={currentPage === 1} onClick={() => setCurrentPage(p => p - 1)}>Prev</button>
                <span>Page {currentPage}</span>
                <button disabled={indexOfLastItem >= entries.length} onClick={() => setCurrentPage(p => p + 1)}>Next</button>
            </div>
        </div>
    );
};

export default MediaList;