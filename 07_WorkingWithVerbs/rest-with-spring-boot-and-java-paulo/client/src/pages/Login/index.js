import React from 'react';
import './styles.scss';

import logo from '../../assets/logo.png';
import logoSecurity from '../../assets/cad.png';

function Login() {
    return (
       <div className="login-container">
        <section className="form">
        <img id="logo" src= {logo} alt="Paulo Logo" />
        <form>
            <h1>Access your Account</h1>
            <input placeholder="Username" />
            <input type="password" placeholder="Password" />
            <button className="button" type="submit">Login</button>
        </form>
        </section>

        <img id="logoSecurity" src= {logoSecurity} alt="Login" />
       </div>
    )
}

export default Login;