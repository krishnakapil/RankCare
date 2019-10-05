import React, { Component } from 'react';
import './Home.css';
import { Button, PageHeader, Row, Col } from 'antd';
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
                {this.renderHomeView()}
            </div>
        );
    }

    renderTitle() {
        if (this.props.currentUser) {
            return (
                this.renderCurrentUserTitleBar(this.props.currentUser.isAdmin, this.props.currentUser.name, this.props.currentUser.isAdmin ? '(Admin)' : '(Client)')
            )
        } else {
            return (
                <h1 className="page-title">Home</h1>
            )
        }
    }

    renderHomeView() {
        if (this.props.currentUser && this.props.currentUser.isAdmin) {
            return (
                <div className="gutter-container">
                    <Row gutter={16}>
                        <Col className="gutter-row" span={6}>
                            <div className="gutter-box">
                                <Button icon="user" className="gutter-button" onClick={this.handleManagerUsersClick}>Manage Users</Button>
                            </div>
                        </Col>
                        <Col className="gutter-row" span={6}>
                            <div className="gutter-box">
                                <Button icon="table" className="gutter-button">Sites List</Button>
                            </div>
                        </Col>
                        <Col className="gutter-row" span={6}>
                            <div className="gutter-box">
                                <Button icon="database" className="gutter-button">Consumption Data</Button>
                            </div>
                        </Col>
                        <Col className="gutter-row" span={6}>
                            <div className="gutter-box">
                                <Button icon="database" className="gutter-button">Toxicity Data</Button>
                            </div>
                        </Col>
                    </Row>
                </div>
            )
        } else {

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
        if (!isAdmin) {
            return (
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