import React, { Component } from 'react';
import './Home.css';
import { PageHeader, Table, Button, Popconfirm, Icon, Descriptions } from 'antd';
import { getSitesWithData } from '../util/APIUtils';
import RankChart from './RankChart';

class CompareSites extends Component {
    constructor(props) {
        super(props);
        const queryString = require('query-string');
        const parsedQuery = queryString.parse(props.location.search, { arrayFormat: 'comma' }).sites;
        const idArray = typeof parsedQuery === 'string' ? [parseInt(parsedQuery, 10)] : parsedQuery.map(v => parseInt(v, 10));

        this.state = {
            currentUser: props.currentUser,
            isLoading: false,
            siteIds: idArray,
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

        this.handleBackClick = this.handleBackClick.bind(this);
        this.getSiteNameKey = this.getSiteNameKey.bind(this);
        this.handleOnSiteClicked = this.handleOnSiteClicked.bind(this);
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

    getSiteNameKey(site) {
        return site.siteName + " (id : " + site.id + ")";
    }

    render() {
        return (
            <div className="user-home-container">
                <PageHeader className="user-list-page-title" title={this.state.isSingleSite ? "Site Details" : "Compare Sites"} onBack={this.handleBackClick} />
                <h3 style={{ marginBottom: '32px', marginTop: '48px' }}><b>Tier 1 Values</b></h3>
                {this.renderTierOneChart()}
                {this.renderTierTwoChart()}
                {this.renderTierThreeChart()}
            </div>
        );
    }

    handleOnSiteClicked(siteNameKey) {
        const siteId = siteNameKey.substring(siteNameKey.indexOf("id : ") + 5, siteNameKey.length - 1);
        this.props.history.push("/site-details?sites=" + [siteId])
        console.log("Clicked site key " + siteId);
    }

    renderTierOneChart() {
        const sitesData = this.state.sitesData;
        if (sitesData && sitesData.length > 0) {
            const fields = ["Water", "Soil"];
            const types = sitesData.map((site) => this.getSiteNameKey(site));
            const data = [];

            for (const [index, site] of sitesData.entries()) {
                var typeData = {}
                typeData.name = this.getSiteNameKey(site);
                typeData["Water"] = site.t1["Water"]
                typeData["Soil"] = site.t1["Soil"]

                data.push(typeData)
            }

            return (
                <RankChart fields={fields} data={data} onSiteClicked={this.handleOnSiteClicked}/>
            )
        }
    }

    renderTierTwoChart() {
        const sitesData = this.state.sitesData;
        if (sitesData && sitesData.length > 1) {
            const ageGroups = ['0-3', '3-6', '6-10', '10-18', '18+'];
            const fields = ["NCR", "CR"];
            const dataByAgeGroup = {};

            for (const [i, ageGrp] of ageGroups.entries()) {
                var data = [];

                for (const [index, siteData] of sitesData.entries()) {
                    const siteNameKey = this.getSiteNameKey(siteData);
                    const t2Data = siteData.t2;
                    const ageGroupData = t2Data[ageGrp];

                    var siteInfo = {};
                    siteInfo.name = siteNameKey;

                    if(ageGroupData) {
                        siteInfo["NCR"] = ageGroupData.NCR;
                        siteInfo["CR"] = ageGroupData.CR;
                    }

                    data.push(siteInfo);
                }

                dataByAgeGroup[ageGrp] = data;
            }

            const views = []

            for (const [index, ageGrp] of ageGroups.entries()) {
                views.push(<h3 key={"tier2Header" + index} style={{ marginBottom: '32px', marginTop: '48px' }}><b>Tier 2 Values ({ageGrp})</b></h3>);
                views.push(<RankChart key={"tier2Views" + index} fields={fields} data={dataByAgeGroup[ageGrp]} onSiteClicked={this.handleOnSiteClicked}/>);
            }

            return (
                <div>
                    {views}
                </div>
            )
        }
    }

    renderTierThreeChart() {
        const sitesData = this.state.sitesData;
        if (sitesData && sitesData.length > 1) {
            const ageGroups = ['0-3', '3-6', '6-10', '10-18', '18+'];
            const fields = ["NCR", "CR"];
            const dataByAgeGroup = {};

            for (const [i, ageGrp] of ageGroups.entries()) {
                var data = [];

                for (const [index, siteData] of sitesData.entries()) {
                    const siteNameKey = this.getSiteNameKey(siteData);
                    const t3Data = siteData.t3;
                    const ageGroupData = t3Data[ageGrp];

                    var siteInfo = {};
                    siteInfo.name = siteNameKey;

                    if(ageGroupData) {
                        siteInfo["NCR"] = ageGroupData.NCR;
                        siteInfo["CR"] = ageGroupData.CR;
                    }

                    data.push(siteInfo);
                }

                dataByAgeGroup[ageGrp] = data;
            }
            
            const views = []

            for (const [index, ageGrp] of ageGroups.entries()) {
                views.push(<h3 key={"tier3Header" + index} style={{ marginBottom: '32px', marginTop: '48px' }}><b>Tier 3 Values ({ageGrp})</b></h3>);
                views.push(<RankChart key={"tier3Views" + index} fields={fields} data={dataByAgeGroup[ageGrp]} onSiteClicked={this.handleOnSiteClicked}/>);
            }

            return (
                <div>
                    {views}
                </div>
            )
        }
    }

    handleBackClick() {
        this.props.history.goBack()
    }
}

export default CompareSites;