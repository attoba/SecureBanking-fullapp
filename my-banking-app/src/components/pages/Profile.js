

import React, { useState,useEffect } from 'react';
import Logout from './Logout';
import Button from '../Button';


import './Profile.css';
import bankImage from '../images/bankf.jpg';
import { useNavigate } from 'react-router-dom';




const Profile = () => {
  //const [sourceAccountNumber, setAccountNumber] = useState('');
  const [destinationAccountNumber, setDestinationAccount] = useState('');
  const [amount, setAmount] = useState('');
  const [balance, setBalance] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(true);
  const [formDisabled, setFormDisabled] = useState(true);
 // const [csrftoken, setCsrfToken] = useState('');
 
 const navigate = useNavigate();


 useEffect(() => {
  const fetchBalance = async () => {
    try {
      const response = await fetch('https://localhost:8443/api/client/balance', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'application/json',
        },
      });

      const data = await response.json();
      if (data.responseCode === "SUCCESS") {
        setBalance(data.accountInfo.accountBalance);
       
      } else {
        setError(data.responseMessage);
      }
     
    } catch (error) {
      console.error(error);
      setError('acces non authorisee');
    }
  };
  fetchBalance();
}, []);



  const handleSubmit = async (e) => {
    e.preventDefault();
   
    try {
      const response = await fetch('https://localhost:8443/api/client/transfer', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
         // 'X-CSRF-TOKEN': csrftoken, // Include CSRF token as header
          'Authorization': `Bearer ${localStorage.getItem('token')}` // Add token to request headers
        },
        body: JSON.stringify({
           // sourceAccountNumber,
            destinationAccountNumber,
            amount
        }),
      });
      const data = await response.json();

      if (data.responseMessage === "Transfer Successful") {
        setBalance(data.accountInfo.accountBalance); // Update the balance
        setSuccess(true);
        setAmount('');
        setDestinationAccount('');

      } else {
        setError(data.responseMessage);
      }
    

    } catch (error) {
      console.error(error);
    }
  };


 


  return (
    <>
    <div className='nspace'></div>
   <div className="profile-header">
     
      <div className='logout-button'>
          <Logout />
      </div>
      <br></br> <br></br> <br></br>
      <h1>Bienvenue dans votre espace en  ligne</h1>  
  
        <div className="balance-section">
       
        <div className="balance-display"> <h2>Votre Balance est : {balance} DH</h2></div>
      </div>
  </div>


      <div className="profile-container">
        <div className="photo-container">  
       
        </div>
            

        <form className="profile-form" onSubmit={handleSubmit}>

       
            <h1>Transferer avec toute securite </h1>  

            <div>
              <label htmlFor="destinationAccount">Transferer au compte :</label>
              <input type="text" id="destinationAccount" value={destinationAccountNumber} onChange={(e) => setDestinationAccount(e.target.value)} required />
            </div>


          <div>
            <label htmlFor="amount">Somme a Transferer</label>
            <input type="text" id="amount" value={amount} onChange={(e) => setAmount(e.target.value)} />
          </div>


          {error && <p className="error-message">{error}</p>}
          <button type="submit" >Transfer</button>


        </form>
      </div>


     
    </>
  );
};


export default Profile;


