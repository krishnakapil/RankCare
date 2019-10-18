import React, { Component } from 'react';
import { PageHeader, Table, Button, Popconfirm, Icon, Form, notification } from 'antd';
import { getSites, deleteToxicity } from '../util/APIUtils';
import NewSite from './NewSite';

class SiteData extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentUser: props.currentUser,
            isLoading: false,
            modalVisible: false,
            selectedSite: null,
            sitesResponse: null,
            sitesData: [],
            currentPage: 0,
            pageSize : 20,
            totalRecords : 0,
            columns: [
                {
                    title: 'Site Name',
                    dataIndex: 'site_name',
                    key: 'site_name',
                    render: (text, record) => <a className="user-list-name-link" onClick={() => this.handleSiteClick(record)}>{text}</a>,
                    sorter: (a, b) => a.site_name.length - b.site_name.length,
                    sortDirections: ['descend', 'ascend'],
                },
                {
                    title: 'Site Id',
                    dataIndex: 'site_id',
                    key: 'site_id',
                },
                {
                    title: 'Site Location',
                    dataIndex: 'site_location',
                    key: 'site_location',
                },
                {
                    title: 'Site State',
                    dataIndex: 'site_state',
                    key: 'site_state',
                },
                {
                    title: 'Site Org',
                    dataIndex: 'site_org',
                    key: 'site_org',
                },
                {
                    title: 'Action',
                    dataIndex: 'id',
                    key: 'id',
                    render: (text, record) =>
                        <Popconfirm title="Sure to delete?" onConfirm={() => this.handleDelete(record.id)}>
                            <a className="user-list-delete-link">
                                <Icon type="delete" className="nav-icon" />
                            </a>
                        </Popconfirm>,
                },
            ]
        }

        this.handleBackClick = this.handleBackClick.bind(this)
        this.handleAddNewDataClick = this.handleAddNewDataClick.bind(this)
        this.handleDelete = this.handleDelete.bind(this)
        this.handleAddUserSubmit = this.handleAddUserSubmit.bind(this)
        this.handleAddUserCancel = this.handleAddUserCancel.bind(this)
        this.handleSiteClick = this.handleSiteClick.bind(this)
        this.onPageChanged = this.onPageChanged.bind(this);
    }

    componentDidMount() {
        this.loadData(0);
    }

    loadData(page) {
        this.setState({
            isLoading: true,
            currentPage: page
        });
        getSites(page, this.state.pageSize)
            .then(response => {
                this.setState({
                    isLoading: false,
                    sitesResponse: response,
                    sitesData: response.data,
                    totalRecords: (response.pageCnt * this.state.pageSize)
                });
            }).catch(error => {
                this.setState({
                    isLoading: false,
                    sitesResponse: null,
                    totalRecords: 0,
                    sitesData: []
                });
            });
    }

    saveFormRef = formRef => {
        this.formRef = formRef;
    };

    onPageChanged(pagination, filters, sorter) {
        this.loadData(pagination.current - 1)
    }

    render() {
        const selectedSite = this.state.selectedSite

        const CollectionCreateForm = Form.create(
            {
                name: 'form_in_modal',
                mapPropsToFields(props) {
                    if (selectedSite) {
                        return {
                            site_name: Form.createFormField({
                                ...props.site_name,
                                value: selectedSite.site_name
                            }),
                            site_location: Form.createFormField({
                                ...props.site_location,
                                value: selectedSite.site_location
                            }),
                            site_state: Form.createFormField({
                                ...props.site_state,
                                value: selectedSite.site_state
                            }),
                            site_org: Form.createFormField({
                                ...props.site_org,
                                value: selectedSite.site_org
                            }),
                        };
                    } else {
                        return {};
                    }
                },
            }
        )(NewSite)

        return (
            <div>
                <div className="user-home-container">
                    <PageHeader className="user-list-page-title" title="Sites Data" onBack={this.handleBackClick} extra={this.renderNavigationButtons()} />
                    <Table
                        rowKey={record => record.id}
                        columns={this.state.columns}
                        dataSource={this.state.sitesData}
                        onChange={this.onPageChanged}
                        pagination={{total: this.state.totalRecords, defaultPageSize: this.state.pageSize, current: this.state.currentPage}}
                    />
                </div>
                <CollectionCreateForm
                    wrappedComponentRef={this.saveFormRef}
                    visible={this.state.modalVisible}
                    onCancel={this.handleAddUserCancel}
                    onCreate={this.handleAddUserSubmit}
                    isEdit={this.state.selectedSite != null}
                    id={this.state.selectedSite ? this.state.selectedSite.id : null}
                />
            </div>
        );
    }

    renderNavigationButtons() {
        return (
            [
                <Button key="1" onClick={this.handleAddNewDataClick}>Add New Row</Button>,
            ]
        )
    }

    handleDelete(id) {
        this.setState({
            isLoading: true
        });

        deleteToxicity(id)
            .then(response => {
                notification.success({
                    message: 'rankCare',
                    description: "Toxicity deleted successfully!",
                });
                this.loadData(this.state.currentPage);
            }).catch(error => {
                this.setState({ isLoading: false });
                notification.error({
                    message: 'rankCare',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            });
    }

    handleSiteClick(user) {
        this.setState({
            modalVisible: true,
            selectedSite: user
        });
    }

    handleAddNewDataClick() {
        this.setState({
            modalVisible: true,
            selectedSite: null
        });
    }

    handleBackClick() {
        this.props.history.goBack()
    }

    handleAddUserSubmit() {
        this.setState({
            modalVisible: false,
            selectedSite: null
        });

        this.loadData(this.state.currentPage)
    }

    handleAddUserCancel() {
        this.setState({
            modalVisible: false,
            selectedSite: null
        });
    }
}

export default SiteData;