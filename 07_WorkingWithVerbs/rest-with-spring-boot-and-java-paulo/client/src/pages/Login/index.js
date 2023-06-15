import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom'
import './styles.scss';

import api from '../../services/api';

import logo from '../../assets/logo.png';
import logoSecurity from '../../assets/cad.png';

function Login() {

    const[username, setUsername] = useState('');
    const[password, setPassword] = useState('');

    const navigate = useNavigate();

    async function login(e) {
        e.preventDefault();

        const data = {
            username,
            password,
        };

        try {
            const response = await api.post('auth/signin', data);

            localStorage.setItem('username', username);
            localStorage.setItem('accessToken', response.data.accessToken);

            navigate('/books');
        } catch(err) {
            alert("Login failed! Try Again!");
        }
    };

    return (
       <div className="login-container">
        <section className="form">
        <img id="logo" src= {logo} alt="Paulo Logo" />
        <form onSubmit={login}>
            <h1>Access your Account</h1>
                <input
                placeholder="Username"
                value={username}
                onChange={e => setUsername(e.target.value)}
                />
                <input
                type="password" placeholder="Password"
                value={password}
                onChange={e => setPassword(e.target.value)}
                />
            <button className="button" type="submit">Login</button>
        </form>
        </section>

        <img id="logoSecurity" src= {logoSecurity} alt="Login" />
       </div>
    )
}

export default Login;