import React, { useState } from 'react';
import './CreateAccount.css';
import { useNavigate } from 'react-router-dom';

function CreateAccount() {
  const navigate = useNavigate();
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [cin, setCin] = useState('');
  const [gender, setGender] = useState('');
  const [email, setEmail] = useState('');
  const [address, setAddress] = useState('');
  const [country, setCountry] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [password, setPassword] = useState('');
  const [Cpassword, setCpassword] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState({});

  const validateForm = () => {
    const errors = {};

    if (!firstName) {
      errors.firstName = 'Le prénom est requis';
    } else if (!/^[A-Za-z]+$/.test(firstName)) {
      errors.firstName = 'Le prénom ne doit contenir que des lettres';
    }

    if (!lastName) {
      errors.lastName = 'Le nom de famille est requis';
    } else if (!/^[A-Za-z]+$/.test(lastName)) {
      errors.lastName = 'Le nom de famille ne doit contenir que des lettres';
    }

    if (!cin) {
      errors.cin = 'Le CIN est requis';
    } else if (!/^[A-Za-z0-9]+$/.test(cin)) {
      errors.cin = 'Le CIN ne doit contenir que des lettres et des chiffres';
    }

    if (!email) {
      errors.email = 'L\'email est requis';
    } else if (!/\S+@\S+\.\S+/.test(email)) {
      errors.email = 'L\'adresse email est invalide';
    }

    if (!address) {
      errors.address = 'L\'adresse est requise';
    } else if (!/^[A-Za-z0-9 ,:]+$/.test(address)) {
      errors.address = 'L\'adresse ne doit contenir que des lettres, des chiffres, des espaces, des virgules et des deux-points';
    }

    if (!phoneNumber) {
      errors.phoneNumber = 'Le numéro de téléphone est requis';
    } else if (!/^\d{10}$/.test(phoneNumber)) {
      errors.phoneNumber = 'Le numéro de téléphone doit contenir 10 chiffres';
    }

    if (!password) {
      errors.password = 'Le mot de passe est requis';
    } else if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}/.test(password)) {
      errors.password = 'Le mot de passe doit contenir au moins 8 caracteres une lettre majuscule, une lettre minuscule, un chiffre et un caractère spécial';
    }

    if (password !== Cpassword) {
      errors.Cpassword = 'Les mots de passe ne correspondent pas';
    }

    setError(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    setSubmitting(true);
    setError({});

    try {
      const response = await fetch('https://localhost:8443/api/client', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ firstName, lastName, cin, gender, email, address, country, phoneNumber, password, Cpassword }),
      });

      const userData = await response.json();
      if (userData.responseCode === "SUCCESS") {
        setSuccess(true);
        navigate('/Authentification');
      } else {
        setError({ general: userData.message });
      }
    } catch (error) {
      setError({ general: error.message });
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className='big-container'>
      <div className="create-account-container">
        <h2>Ouvrir un compte, vérification des coordonnées</h2>
        <form className="create-account-form" onSubmit={handleSubmit}>
          <div className="create-account-fields">
            <div className="create-account-group">
              <div className="create-account-field ">
                <label htmlFor="firstName">First Name:</label>
                <input
                  type="text"
                  id="firstName"
                  name="firstName"
                  placeholder="votre prenom"
                  required
                  onChange={(e) => setFirstName(e.target.value)}
                />
                {error.firstName && <p className="error-message">{error.firstName}</p>}
              </div>
              <div className="create-account-field ">
                <label htmlFor="lastName">Last Name:</label>
                <input
                  type="text"
                  id="lastName"
                  name="lastName"
                  placeholder="votre nom"
                  required
                  onChange={(e) => setLastName(e.target.value)}
                />
                {error.lastName && <p className="error-message">{error.lastName}</p>}
              </div>
              <div className="create-account-field ">
                <label htmlFor="cin">Cin:</label>
                <input
                  type="text"
                  id="cin"
                  name="cin"
                  required
                  onChange={(e) => setCin(e.target.value)}
                />
                {error.cin && <p className="error-message">{error.cin}</p>}
              </div>
              <div className="create-account-field ">
                <label htmlFor="email">Email:</label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  required
                  onChange={(e) => setEmail(e.target.value)}
                />
                {error.email && <p className="error-message">{error.email}</p>}
              </div>
            </div>
            <div className="create-account-group">
              <div className="create-account-field ">
                <label htmlFor="address">Adresse:</label>
                <textarea
                  id="address"
                  name="address"
                  rows="2"
                  required
                  onChange={(e) => setAddress(e.target.value)}
                ></textarea>
                {error.address && <p className="error-message">{error.address}</p>}
              </div>
              <div className="create-account-field ">
                <label htmlFor="phoneNumber">Tel:</label>
                <input
                  type="text"
                  id="phoneNumber"
                  name="phoneNumber"
                  required
                  onChange={(e) => setPhoneNumber(e.target.value)}
                />
                {error.phoneNumber && <p className="error-message">{error.phoneNumber}</p>}
              </div>
              <div className="create-account-field ">
                <label htmlFor="password">Mot de Pass:</label>
                <input
                  type="password"
                  id="password"
                  name="password"
                  placeholder="Entrer un password"
                  required
                  onChange={(e) => setPassword(e.target.value)}
                />
                {error.password && <p className="error-message">{error.password}</p>}
              </div>
              <div className="create-account-field ">
                <label htmlFor="Cpassword">Confirmer le Mot de Pass:</label>
                <input
                  type="password"
                  id="Cpassword"
                  name="Cpassword"
                  placeholder="confirmer le password"
                  required
                  onChange={(e) => setCpassword(e.target.value)}
                />
                {error.Cpassword && <p className="error-message">{error.Cpassword}</p>}
              </div>
            </div>
          </div>
          <div className="create-account-submit">
            <button type="submit" disabled={submitting}>
              {submitting ? 'En cours...' : 'Confirmer'}
            </button>
          </div>
          {success && <p className="success-message">Account created successfully!</p>}
          {error.general && <p className="error-message">{error.general}</p>}
        </form>
      </div>
    </div>
  );
}

export default CreateAccount;
