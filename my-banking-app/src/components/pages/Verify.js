
import React, { useState,useEffect } from 'react';
import './Auth.css'; // Import CSS file
import { useNavigate } from 'react-router-dom';

const Verify = () => {
  const navigate = useNavigate();
  const [inputCode, setCode] = useState('');
  const [email, setEmail] = useState('');
  const [error, setError] = useState('');
  //const [csrftoken, setCsrfToken] = useState('');


 


  const handleLogin = async (e) => { // Declare handleLogin as async
    e.preventDefault();
    
    try {
      const response = await fetch('https://localhost:8443/api/client/verify',{
        method: 'POST',
        headers: { 
          'Content-Type': 'application/json' ,
          //'X-CSRF-TOKEN': csrftoken, // Include CSRF token as header
         // 'Authorization': `Bearer ${localStorage.getItem('token')}` 
        },
        body: JSON.stringify({ inputCode, email }), // Use accountNumber and password as form data
      }); 

      const data = await response.json(); // Parse response JSON

      if (data.responseCode === "Verification Success") {
        localStorage.setItem('token', data.responseMessage);

        navigate('/Profile');
      } else {
        setError(data.responseCode);
      }
      
    } catch (error) {
      console.error(error);
      //setError(error.message);
    }
  };

  return (
    <>
      <div className="auth-container">
      <div className='photo1-container'>
        <h1>Verification</h1>
      </div>
        
        <form className="auth-form" onSubmit={handleLogin}>
        <h2>Pour confirmer votre opération, entrez le code de vérification pour l'authentification.</h2>
        <h3>Le code de vérification est envoyé à votre email</h3>
       
        <div>
            <label htmlFor="email">Email</label>
            <input type="email" id="email" value={email} onChange={(e) => setEmail(e.target.value)} />
          </div>
         
          <div>
            <label htmlFor="code">Code</label>
            <input type="text" id="code" value={inputCode} onChange={(e) => setCode(e.target.value)} />
          </div>

          {error && <p className="error-message">{error}</p>}
          <br></br>
          <button type="submit">OK</button>
        </form>
        
      </div>


    </>
  );
};

export default Verify;
