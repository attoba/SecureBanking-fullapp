import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css';

function Navbar() {
  const [click, setClick] = useState(false);
  const [scrolling, setScrolling] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      if (window.scrollY > 0) {
        setScrolling(true);
      } else {
        setScrolling(false);
      }
    };

    window.addEventListener('scroll', handleScroll);
    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, []);

  const handleClick = () => setClick(!click);
  const closeMobileMenu = () => setClick(false);

  return (
    <nav className={`navbar ${scrolling ? 'opaque' : ''}`}>
      <div className='navbar-container'>
        <Link to='/' className='navbar-logo' onClick={closeMobileMenu}>
          BSM BANK
          <i className='fab fa-typo3' />
        </Link>
        <div className='menu-icon' onClick={handleClick}>
          <i className={click ? 'fas fa-times' : 'fas fa-bars'} />
        </div>
        <ul className={click ? 'nav-menu active' : 'nav-menu'}>
          <li className='nav-item'>
            <Link to='/Auth' className='nav-links' onClick={closeMobileMenu}>
              Accédez à vos comptes
            </Link>
          </li>
          <li className='nav-item'>
            <Link to='/CreateAccount' className='nav-links' onClick={closeMobileMenu}>
              Ouvrir un compte
            </Link>
          </li>
          <li className='nav-item'>
            <Link to='/AuthEmploye' className='nav-links' onClick={closeMobileMenu}>
              Employe
            </Link>
          </li>
        </ul>
      </div>
    </nav>
  );
}

export default Navbar;
