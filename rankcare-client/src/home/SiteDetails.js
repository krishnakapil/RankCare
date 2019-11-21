import React, { Component } from 'react';
import './Home.css';
import { PageHeader, Table, Descriptions } from 'antd';
import { getSite } from '../util/APIUtils';
import RankChart from './RankChart';

class SiteDetails extends Component {
    constructor(props) {
        super(props);
        const queryString = require('query-string');
        const siteId = queryString.parse(this.props.location.search).sites

        this.state = {
            currentUser: props.currentUser,
            isLoading: false,
            siteId: siteId,
            siteData: {},
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
                    dataIndex: 'valueWithUnit',
                    key: 'valueWithUnit',
                },
            ]
        }

        this.handleBackClick = this.handleBackClick.bind(this);
    }

    componentDidMount() {
        this.loadData();
    }

    loadData() {
        getSite(this.state.siteId)
            .then(response => {
                this.setState({
                    siteData: response
                });
            }).catch(error => {
                this.setState({
                    siteData: {}
                });
            });
    }

    saveFormRef = formRef => {
        this.formRef = formRef;
    };

    render() {
        return (
            <div className="user-home-container">
                <PageHeader className="user-list-page-title" title={"Site Details"} onBack={this.handleBackClick} />
                {
                    this.renderSiteDetails()
                }
                <h3 style={{ marginBottom: '32px', marginTop: '48px' }}><b>Tier 1 Values</b></h3>
                {this.renderTierOneChart()}
                {this.renderTierTwoChart()}
                {this.renderTierThreeChart()}
                {this.renderChecmicalContaminents()}
            </div>
        );
    }

    renderTierOneChart() {
        const siteData = this.state.siteData;
        if (siteData && siteData.t1) {
            const fields = siteData.siteName;
            const types = ["Water", "Soil"];
            const data = [];

            for (const [index, type] of types.entries()) {
                var typeData = {}
                typeData.name = type
                typeData[siteData.siteName] = siteData.t1[type]
                data.push(typeData)
            }

            return (
                <RankChart fields={fields} data={data}/>
            )
        }
    }

    renderTierTwoChart() {
        const siteData = this.state.siteData;
        if (siteData && siteData.t2) {
            const t2Data = siteData.t2;
            const fields = Object.keys(t2Data);
            const types = ["CR", "NCR"]
            const data = [];

            for (const [index, type] of types.entries()) {
                var typeData = {}
                typeData.name = type

                for (const [index, ageGroup] of fields.entries()) {
                    typeData[ageGroup] = t2Data[ageGroup][type];
                }

                data.push(typeData)
            }

            return (
                <div>
                    <h3 style={{ marginBottom: '32px', marginTop: '48px' }}><b>Tier 2 Values</b></h3>
                    <RankChart fields={fields} data={data} />
                </div>
            )
        }
    }

    renderTierThreeChart() {
        const siteData = this.state.siteData;
        if (siteData && siteData.t3) {
            const t3Data = siteData.t3;
            const fields = Object.keys(t3Data);
            const types = ["CR", "NCR"]
            const data = [];

            for (const [index, type] of types.entries()) {
                var typeData = {}
                typeData.name = type

                for (const [index, ageGroup] of fields.entries()) {
                    typeData[ageGroup] = t3Data[ageGroup][type];
                }

                data.push(typeData)
            }

            return (
                <div>
                    <h3 style={{ marginBottom: '32px', marginTop: '48px' }}><b>Tier 3 Values</b></h3>
                    <RankChart fields={fields} data={data} />
                </div>
            )
        }
    }

    renderSiteDetails() {
        const siteData = this.state.siteData
        if (siteData) {
            return (
                <div>
                    <Descriptions bordered className="site-details-box">
                        <Descriptions.Item label="Site Name">{siteData.siteName}</Descriptions.Item>
                        <Descriptions.Item label="site Org"  span={2}>{siteData.orgName}</Descriptions.Item>
                        <Descriptions.Item label="Site Location">
                            {siteData.siteLocation}
                        </Descriptions.Item>
                    </Descriptions>
                </div>
            )
        }
    }

    renderChecmicalContaminents() {
        const siteData = this.state.siteData
        if (siteData) {
            return (
                <div>
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