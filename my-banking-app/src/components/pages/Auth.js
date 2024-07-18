// Auth.js

import React, { useState,useEffect } from 'react';

import './Auth.css'; // Import CSS file
import { useNavigate } from 'react-router-dom';

const Auth = () => {
  const navigate = useNavigate();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [storedCsrfToken, setCsrfToken] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
  
    try {
      // Check if a CSRF token already exists in local storage
     // const storedCsrfToken = localStorage.getItem('csrfToken');
     // if (storedCsrfToken) {
        // If token exists, set it in the component state
       // setCsrfToken(storedCsrfToken);
  
        // Make the login request with the CSRF token included in the headers
        const response = await fetch('https://localhost:8443/api/client/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
           // 'X-CSRF-TOKEN': storedCsrfToken, // Include CSRF token as header
          },
          body: JSON.stringify({ email, password }), // Use email and password as form data
        });
  
        // Log the response status to verify if the request is successful
        console.log('Response status:', response.status);
  
        const userData = await response.json(); // Parse response data
        if (userData.responseCode === "Login Success") {
          navigate('/Verify'); // Redirect to profile page on successful login
        } else {
          setError(userData.message);
        }
     // }
    } catch (error) {
      console.error('Failed to  login:', error);
    }
  };
  
  return (
    <>
      <div className="auth-container">
        <div className='photo1-container'>
        <h1>Authentification</h1>
        <h2>Bienvenue dans votre espace sécurisé ONLINE. Pour accéder à votre compte, merci de vous identifier.
</h2>
        
        </div>
       
        <form className="auth-form" onSubmit={handleLogin}>
        <h3>Pour accéder à votre espace, saisissez votre nom d'utilisateur, puis composez les chiffres de votre mot de passe</h3>
          <div>
            <label htmlFor="email">Email</label>
            <input type="email" id="email" value={email} onChange={(e) => setEmail(e.target.value)}  />
          </div>

          <div>
            <label htmlFor="password">Mot de Passe</label>
            <input type="password" id="password" value={password} onChange={(e) => setPassword(e.target.value)} />
          </div>

          {error && <p className="error-message">{error}</p>}
          <button type="submit">Login</button>
        </form>
        
      </div>


    </>
  );
};

export default Auth;
