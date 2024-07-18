import React from 'react';
import './Home.css';

function Home() {
    const contentBlocks = [
        {
            imageClass: "home-block1",
            title: "Accédez de manière simple et rapide",
            text: "à votre compte bancaire partout où vous êtes, depuis votre Smartphone ou votre tablette."
        },
        {
            imageClass: "home-block2",
            title: "Ouvrez un compte de manière simple et rapide",
            text: "partout où vous êtes, depuis votre Smartphone ou votre tablette."
        },
        {
            imageClass: "home-block3",
            title: "Virement real time",
            text: "Le virement real time est une opération de transfert d’argent d'un compte à un autre. Le compte bénéficiaire est crédité immédiatement."
        },
        {
            imageClass: "home-block4",
            title: "Deposez vos argents",
            text: "à votre compte bancaire partout où vous êtes, depuis votre Smartphone ou votre tablette."
        },
        {
            imageClass: "home-block5",
            title: "Retirez vos argents",
            text: "partout où vous êtes depuis votre Smartphone ou votre tablette."
        }
    ];

    return (
        <div className="home-container">
            <div className="home-div">
                <h1>BIENVENU</h1>
                <h2>Gérez vos comptes bancaires à tout moment et sans déplacement avec BANK ONLINE</h2>
            </div>
            {contentBlocks.map((block, index) => (
                <div key={index} className={`home-block ${block.imageClass}`}>
                    <div className="home-block-image"></div>
                    <div className="home-block-text">
                        <h3>{block.title}</h3>
                        <p>{block.text}</p>
                    </div>
                </div>
            ))}
        </div>
    );
}

export default Home;
