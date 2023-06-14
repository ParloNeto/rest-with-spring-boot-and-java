import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';

import Login from './pages/Login';
import Books from './pages/Books';
import NewBook from './pages/NewBook';

function AppRoutes() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" exact element={<Login/>}/>
                <Route path="/books" element={<Books/>}/>
                <Route path="/books/book/new" element={<NewBook/>}/>
            </Routes>
        </BrowserRouter>
    );
}


export default AppRoutes;