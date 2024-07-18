import React, { useState,useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Button from '../Button';


const Logout = () => {
  const navigate = useNavigate();
  const [error, setError] = useState('');
  const handleLogout = async () => {
   
    try {
        const response = await fetch('https://localhost:8443/api/client/logout',{
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`, // Include the JWT token in the Authorization header
            'Content-Type': 'application/json' // Specify the content type
        }
          
        }); 
  
        const data = await response.json(); // Parse response JSON
  
        if (data.responseMessage === "Logout successful") {
           // Perform logout action here
           localStorage.removeItem('token'); // Remove token from localStorage
            navigate('/Auth'); // Redirect to the login page
  
        } else {
          setError(data.responseMessage);
        }
        
      } catch (error) {
        console.error(error);
        setError(error.message);
      }
    
  };

  return (
    <Button onClick={handleLogout}>Déconnexion</Button>
  );
};

export default Logout;
