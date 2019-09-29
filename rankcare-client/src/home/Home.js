import React, { Component } from 'react';
import './Home.css';
import { Button, PageHeader } from 'antd';
import SiteList from './SiteList';

class Home extends Component {
    constructor(props) {
        super(props);
        this.renderTitle = this.renderTitle.bind(this)
        this.handleManagerUsersClick = this.handleManagerUsersClick.bind(this)
    }

    render() {
        return (
            <div className="home-container">
                {this.renderTitle()}
                {this.renderSiteList()}
            </div>
        );
    }

    renderTitle() {
        if (this.props.currentUser) {
            return(
                this.renderCurrentUserTitleBar(this.props.currentUser.isAdmin, this.props.currentUser.name, this.props.currentUser.isAdmin ? '(Admin)' : '(Client)')
            )
        } else {
            return (
                <h1 className="page-title">Home</h1>
            )
        }
    }

    renderSiteList() {
        return (
            <SiteList />
        )
    }

    renderCurrentUserTitleBar(isAdmin, name, role) {
        return (
            <PageHeader
                className="page-title"
                title={name}
                subTitle={role}
                extra={this.renderNavigationButtons(isAdmin)}>
            </PageHeader>
        )
    }

    renderNavigationButtons(isAdmin) {
        if(isAdmin) {
            return(
                [
                    <Button key="3" onClick={this.handleManagerUsersClick}>Manage Users</Button>,
                    <Button key="2">Add New Site</Button>,
                ]
            )
        } else {
            return(
                [
                    <Button key="2">Add New Site</Button>,
                ]
            )
        }
    }

    handleManagerUsersClick() {
        this.props.history.push("/manage-users")
    }
}

export default Home;