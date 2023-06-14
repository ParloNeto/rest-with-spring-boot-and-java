import React from "react";
import { Link } from "react-router-dom";
import { FiArrowLeft } from "react-icons/fi"

import './styles.scss';

import logo from '../../assets/logo.png';

function NewBook() {
    return(
        <div className="new-book-container">
           <div className="content">
            <section className="form">
                <img src={logo} alt="Paulo Logo" />
                <h1>Add New Book</h1>
                <p>Enter the book information and click on 'Add'!</p>
                <Link className="back-link" to="/books">
                    <FiArrowLeft size={16} color="#251FC5"/>
                    Home
                </Link>
            </section>
            <form>
                <input placeholder="Title" />
                <input placeholder="Author" />
                <input type="date" />
                <input placeholder="Price" />

                <button className="button" type="submit">Add</button>
            </form>
            </div> 
        </div>
    );
}

export default NewBook;