

import React, { useState,useEffect } from 'react';


import './Profile.css';
import bankImage from '../images/bankf.jpg';
import Logout from './Logout';



const Employe = () => {
  const [accountNumberR, setAccountNumberR] = useState('');
  const [accountNumberD, setAccountNumberD] = useState('');
  const [accountNumberDelete, setAccountNumberDelete] = useState('');
  const [amountR, setAmountR] = useState('');
  const [amountD, setAmountD] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(true);
  const [formDisabled, setFormDisabled] = useState(true);
  //const [csrftoken, setCsrfToken] = useState('');


  const handleDeleteUser = async (accountNumber) => {
    accountNumber.preventDefault();
   
    try {
      const response = await fetch('https://localhost:8443/api/client/delete', {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        //  'X-CSRF-TOKEN': csrftoken, // Include CSRF token as header
          'Authorization': `Bearer ${localStorage.getItem('token')}` // Add token to request headers
        },
        body: JSON.stringify({
          accountNumberDelete,
        }),
      });

      const data = await response.json();

      if (data.responseCode === "SUCCESS") {
          console.log('User deleted successfully');
      } else {
          console.error(data.responseMessage);
      }
  } catch (error) {
      console.error('Error:', error);
  }
      
  };


  const handleSubmit = async (e) => {
    e.preventDefault();
   
    try {
      const response = await fetch('https://localhost:8443/api/client/credit', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        //  'X-CSRF-TOKEN': csrftoken, // Include CSRF token as header
          'Authorization': `Bearer ${localStorage.getItem('token')}` // Add token to request headers
        },
        body: JSON.stringify({
            accountNumberD,
          amountD
        }),
      });
      if (response.ok) {
        throw new Error('la somme a ete bien depose');
      }

      if (!response.ok) {
        throw new Error('Failed to deposit money');
      }


     
    } catch (error) {
      console.error(error);
      setError('Failed to deposit money');
    }
  };

  const handleSubmit1 = async (e) => {
    e.preventDefault();
   
    try {
      const response = await fetch('https://localhost:8443/api/client/debit', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        //  'X-CSRF-TOKEN': csrftoken, // Include CSRF token as header
          'Authorization': `Bearer ${localStorage.getItem('token')}` // Add token to request headers
        },
        body: JSON.stringify({
            accountNumberR,
          amountR
        }),
      });
      if (response.ok) {
        throw new Error('la somme a ete bien depose');
      }

      if (!response.ok) {
        throw new Error('Failed to deposit money');
      }


     
    } catch (error) {
      console.error(error);
      setError('Failed to deposit money');
    }
  };





  return (
    <>
        <div className='nspace'></div>
        <div className='space'>
          <div className='logout-button'>
            <br></br>
            <Logout />
          </div>
          <h1>Bienvenue dans votre espace en  ligne</h1>  
        </div>
      <div className="profile-container">
        <div className="photo-container1">              
        </div>
       
        <form className="profile-form" onSubmit={handleSubmit}>


            <h1>Déposer avec toute securite </h1>  
           
            <div>
            <label htmlFor="accountNumberD">Déposer au compte :</label>
            <input
              type="text"
              id="accountNumberD"
              value={accountNumberD}
              onChange={(e) => setAccountNumberD(e.target.value)}
              required
            />
          </div>

          <div>
            <label htmlFor="amountD">Somme à déposer</label>
            <input type="text" id="amountD" value={amountD} onChange={(e) => setAmountD(e.target.value)} />
          </div>


          {error && <p className="error-message">{error}</p>}
          <button type="submit" >Déposer</button>
        </form>
      </div>


    <div className='space'>

    </div>


      <div className="profile-container">
        

        <form className="profile-form" onSubmit={handleSubmit1}>


            <h1>Retirer avec toute securite </h1>  
           
            <div>
            <label htmlFor="accountNumberR">Retirer du compte :</label>
            <input
              type="text"
              id="accountNumberR"
              value={accountNumberR}
              onChange={(e) => setAccountNumberR(e.target.value)}
              required
            />
          </div>

          <div>
            <label htmlFor="amountR">Somme à Retirer</label>
            <input type="text" id="amountR" value={amountR} onChange={(e) => setAmountR(e.target.value)} />
          </div>


          {error && <p className="error-message">{error}</p>}
          <button type="submit" >Retirer</button>
        </form>

        <div className="photo-container2">              
        </div>
      </div>
      
      <div className='space'>
</div>
      
      <div className="profile-container">
      <div className="photo-container1">              
      </div>
        
        <form className="profile-form" onSubmit={handleDeleteUser}>
            <h1>Supprimer un compte </h1>  
           
            <div>
            <label htmlFor="accountNumber">Fermeture du compte bancaire :</label>
            <input
              type="text"
              id="accountNumber"
              value={accountNumberDelete}
              onChange={(e) => setAccountNumberDelete(e.target.value)}
              required
            />
          </div>

          {error && <p className="error-message">{error}</p>}
          <button type="submit" >Fermer</button>
        </form>

       
      </div>
     
    </>
  );
};


export default Employe;


