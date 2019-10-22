import React, { Component } from 'react';
import './Home.css';
import { PageHeader, Table, Button, Popconfirm, Icon, Descriptions } from 'antd';
import { getSitesWithData } from '../util/APIUtils';
import TierOneChart from './TierOneChart';
import TierTwoSingleChart from './TierTwoSingleChart';

class SiteDetails extends Component {
    constructor(props) {
        super(props);
        const queryString = require('query-string');
        const parsedQuery = queryString.parse(props.location.search, { arrayFormat: 'comma' }).sites;
        const idArray = typeof parsedQuery === 'string' ? [parseInt(parsedQuery, 10)] : parsedQuery.map(v => parseInt(v, 10));

        console.log("SiteDetails " + props.location.search + " Parsed to " + JSON.stringify(idArray));
        this.state = {
            currentUser: props.currentUser,
            isLoading: false,
            siteIds: idArray,
            isSingleSite: idArray.length === 1,
            singleSiteData: null,
            sitesData: [],
            columns: [
                {
                    title: 'Contamination Type',
                    dataIndex: 'contaminationType',
                    key: 'contaminationType',
                    sorter: (a, b) => a.contaminationType.length - b.contaminationType.length,
                    sortDirections: ['descend', 'ascend'],
                },
                {
                    title: 'Chemical Name',
                    dataIndex: 'chemicalName',
                    key: 'chemicalName',
                    sorter: (a, b) => a.chemicalName.length - b.chemicalName.length,
                    sortDirections: ['descend', 'ascend'],
                },
                {
                    title: 'Contamination Value',
                    dataIndex: 'contaminationValue',
                    key: 'contaminationValue',
                },
            ]
        }

        this.handleBackClick = this.handleBackClick.bind(this)
    }

    componentDidMount() {
        this.loadData();
    }

    loadData() {
        getSitesWithData(this.state.siteIds)
            .then(response => {
                this.setState({
                    sitesData: response,
                    singleSiteData: response.length === 1 ? response[0] : null
                });
            }).catch(error => {
                this.setState({
                    sitesData: [],
                    singleSiteData: null
                });
            });
    }

    saveFormRef = formRef => {
        this.formRef = formRef;
    };

    render() {
        return (
            <div className="user-home-container">
                <PageHeader className="user-list-page-title" title={this.state.isSingleSite ? "Site Details" : "Compare Sites"} onBack={this.handleBackClick} />
                {
                    this.renderSiteDetails()
                }
                <h3 style={{ marginBottom: '32px', marginTop: '48px' }}><b>Tier 1 Values</b></h3>
                {this.renderTierOneChart()}
                <h3 style={{ marginBottom: '32px', marginTop: '48px' }}><b>Tier 2 Values</b></h3>
                {this.renderTierTwoChart()}
            </div>
        );
    }

    renderTierOneChart() {
        const sitesData = this.state.sitesData;
        if (sitesData && sitesData.length > 0) {
            return(
                <TierOneChart sitesData={sitesData} />
            )
        }
    }

    renderTierTwoChart() {
        const sitesData = this.state.sitesData;
        if (sitesData && sitesData.length === 1) {
            return(
                <TierTwoSingleChart sitesData={sitesData} />
            )
        } else if(sitesData && sitesData.length > 1) {
            
        }
    }

    renderSiteDetails() {
        const siteData = this.state.singleSiteData
        if (siteData && this.state.isSingleSite) {
            return (
                <div>
                    <Descriptions bordered className="site-details-box">
                        <Descriptions.Item label="Site Name">{siteData.siteName}</Descriptions.Item>
                        <Descriptions.Item label="Site Location" span={2}>
                            {siteData.siteLocation}
                        </Descriptions.Item>
                        <Descriptions.Item label="Site State">{siteData.state}</Descriptions.Item>
                        <Descriptions.Item label="site Org">{siteData.orgName}</Descriptions.Item>
                    </Descriptions>
                    <h3 style={{ marginBottom: '32px' }}><b>Site Contaminants</b></h3>
                    <Table
                        rowKey={record => record.id}
                        columns={this.state.columns}
                        dataSource={siteData.siteContaminant}
                        pagination={false}
                    />
                </div>
            )
        }
    }

    handleBackClick() {
        this.props.history.goBack()
    }
}

export default SiteDetails;