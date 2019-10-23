import React, { Component } from 'react';
import './Home.css';
import { Button, PageHeader, Row, Col } from 'antd';
import SiteList from './SiteList';

class Home extends Component {
    constructor(props) {
        super(props);
        this.renderTitle = this.renderTitle.bind(this)
        this.handleManagerUsersClick = this.handleManagerUsersClick.bind(this)
        this.handleToxicityClick = this.handleToxicityClick.bind(this);
        this.handleConsumptionClick = this.handleConsumptionClick.bind(this);
        this.handleSitesClicked = this.handleSitesClicked.bind(this);
    }

    render() {
        return (
            <div className="home-container">
                {this.renderTitle()}
                {this.renderHomeView()}
                {this.renderAboutPoints()}
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

    renderAboutPoints() {
        return (
            <div className="about-points-box">
                <ol>
                    <li><strong><a href="https://www.crccare.com/our-research/program-1-policy" target="_blank">Best Practice Policy</a></strong><strong>: </strong><em>More
                    effective, efficient and certain national policy for assessing and remediating
contamination</em></li>
                    <li style={{marginTop:'16px'}}><a href="https://www.crccare.com/our-research/program-2-measurement" target="_blank"><strong>Better Measurement</strong></a>: <em>More
accurate, rapid, reliable and cost-effective measurement and assessment</em></li>
                    <li style={{marginTop:'16px'}}><strong><a href="https://www.crccare.com/our-research/program-3-risk-assessment" target="_blank">Minimising Uncertainty in Risk Assessment</a>: </strong><em>New
                    technology, methods and knowledge for assessing risks to human health and the
environment</em></li>
                    <li style={{marginTop:'16px'}}><strong><a href="https://www.crccare.com/our-research/program-4-remediation" target="_blank">Cleaning Up</a>: </strong><em>Innovative clean-up technologies and a wider
range of effective management options</em></li>
                </ol>
            </div>
        )
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
                                <Button icon="table" className="gutter-button" onClick={this.handleSitesClicked}>Sites List</Button>
                            </div>
                        </Col>
                        <Col className="gutter-row" span={6}>
                            <div className="gutter-box">
                                <Button icon="database" className="gutter-button" onClick={this.handleConsumptionClick}>Consumption Data</Button>
                            </div>
                        </Col>
                        <Col className="gutter-row" span={6}>
                            <div className="gutter-box">
                                <Button icon="database" className="gutter-button" onClick={this.handleToxicityClick}>Toxicity Data</Button>
                            </div>
                        </Col>
                    </Row>
                </div>
            )
        } else {
            return (
                <div className="gutter-container">
                    <Row gutter={16}>
                        <Col className="gutter-row" span={8}>
                            <div className="gutter-box">
                                <Button icon="table" className="gutter-button" onClick={this.handleSitesClicked}>Sites List</Button>
                            </div>
                        </Col>
                        <Col className="gutter-row" span={8}>
                            <div className="gutter-box">
                                <Button icon="database" className="gutter-button" onClick={this.handleConsumptionClick}>Consumption Data</Button>
                            </div>
                        </Col>
                        <Col className="gutter-row" span={8}>
                            <div className="gutter-box">
                                <Button icon="database" className="gutter-button" onClick={this.handleToxicityClick}>Toxicity Data</Button>
                            </div>
                        </Col>
                    </Row>
                </div>
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
                subTitle={role}>
            </PageHeader>
        )
    }

    handleManagerUsersClick() {
        this.props.history.push("/manage-users")
    }

    handleToxicityClick() {
        this.props.history.push("/toxicity")
    }

    handleConsumptionClick() {
        this.props.history.push("/consumption")
    }

    handleSitesClicked() {
        this.props.history.push("/sites")
    }
}

export default Home;