import React, { useState } from 'react';
import axios from 'axios';

const AddMedia = () => {
    const [rows, setRows] = useState([{ title: '', date: new Date().toISOString().split('T')[0], type: '', genre: '', isFinished: false }]);

    const addRow = () => {
        setRows([...rows, { title: '', date: new Date().toISOString().split('T')[0], type: '', genre: '', isFinished: false }]);
    };

    const handleChange = (index, field, value) => {
        const newRows = [...rows];
        newRows[index][field] = value;
        setRows(newRows);
    };

    const handleSubmit = async () => {
        // We loop through and send each one, or you can update your API to accept a List
        for (let row of rows) {
            await axios.post('/api/entries', row);
        }
        alert("All entries saved!");
        window.location.href = '/';
    };

    return (
        <div className="container">
            <h2>Add Multiple Entries</h2>
            {rows.map((row, index) => (
                <div key={index} className="form-row">
                    <input type="text" placeholder="Title" value={row.title} onChange={(e) => handleChange(index, 'title', e.target.value)} />
                    <input type="date" value={row.date} onChange={(e) => handleChange(index, 'date', e.target.value)} />
                    <select value={row.type} onChange={(e) => handleChange(index, 'type', e.target.value)}>
                        <option value="">Type</option>
                        <option value="Movie">Movie</option>
                        <option value="Book">Book</option>
                    </select>
                    <label>
                        Finished:
                        <input type="checkbox" checked={row.isFinished} onChange={(e) => handleChange(index, 'isFinished', e.target.checked)} />
                    </label>
                </div>
            ))}
            <button onClick={addRow}>+ Add Another Row</button>
            <button onClick={handleSubmit} className="save-btn">Save All Entries</button>
        </div>
    );
};

export default AddMedia;