import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import axios from 'axios';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import Home from './components/pages/Home';
import Auth from './components/pages/Auth';
import Verify from './components/pages/Verify';
import AuthEmploye from './components/pages/AuthEmploye';
import CreateAccount from './components/pages/CreateAccount';
import Profile from './components/pages/Profile';
import Employe from './components/pages/employe';

function App() {
  const [csrfToken, setCsrfToken] = useState('');

 /* useEffect(() => {
    const fetchCsrfToken = async () => {
      try {
        // Check if a CSRF token already exists in local storage
        const storedCsrfToken = localStorage.getItem('csrfToken');
        if (storedCsrfToken) {
          setCsrfToken(storedCsrfToken);
          return; // Skip fetching a new CSRF token
        }
  
        // Fetch CSRF token from the backend if it doesn't exist in local storage
        const response = await axios.get('http://localhost:8080/csrf');
        const csrfToken = response.data.token;
        localStorage.setItem('csrfToken', csrfToken); // Store CSRF token in local storage
        console.log('CSRF token stored in local storage:', csrfToken); // Log CSRF token

        setCsrfToken(csrfToken);
      } catch (error) {
        console.error('Failed to fetch CSRF token:', error);
      }
    };
  
    fetchCsrfToken();
  }, []);
  
  */
 
  

  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/Auth" element={<Auth />} />
        <Route path="/AuthEmploye" element={<AuthEmploye />} />
        <Route path="/Verify" element={<Verify />} />
        <Route path="/Profile" element={<Profile />} />
        <Route path="/Employe" element={<Employe />} />
        <Route path="/CreateAccount" element={<CreateAccount />} />
      </Routes>
      <Footer />
    </Router>
  );
}

export default App;
