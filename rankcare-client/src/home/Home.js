import React, { Component } from 'react';
import './Home.css';

class Home extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="home-container">
                {this.renderTitle()}
            </div>
        );
    }

    renderTitle() {
        if(this.props.currentUser) {
            return(
                <h1 className="page-title">Welcome {this.props.currentUser.name} ({this.props.currentUser.isAdmin ? 'Admin' : 'Client'})</h1>
            )
        } else {
            return(
                <h1 className="page-title">Home</h1>
            )
        }
    }
}

export default Home;