import React, { Component } from 'react';
import { PageHeader, Table, Button, Popconfirm, Icon, Form, notification } from 'antd';
import { getSites, deleteSite, getAllChemicals } from '../util/APIUtils';
import NewSite from './NewSite';

class SiteData extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentUser: props.currentUser,
            isAdmin: props.currentUser.isAdmin,
            isLoading: false,
            modalVisible: false,
            selectedSite: null,
            sitesResponse: null,
            chemicals: [],
            sitesData: [],
            currentPage: 0,
            pageSize: 20,
            totalRecords: 0,
            selectedRowKeys: [],
            selectedRows: [],
            columns: props.currentUser.isAdmin ? [
                {
                    title: 'Site Name',
                    dataIndex: 'siteName',
                    key: 'siteName',
                    render: (text, record) => <a className="user-list-name-link" onClick={() => this.handleSiteClick(record)}>{text}</a>,
                    sorter: (a, b) => a.siteName.length - b.siteName.length,
                    sortDirections: ['descend', 'ascend'],
                },
                {
                    title: 'Site Location',
                    dataIndex: 'siteLocation',
                    key: 'siteLocation',
                },
                {
                    title: 'Site State',
                    dataIndex: 'siteState',
                    key: 'siteState',
                },
                {
                    title: 'Site Org',
                    dataIndex: 'siteOrg',
                    key: 'siteOrg',
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
            ] :
                [
                    {
                        title: 'Site Name',
                        dataIndex: 'siteName',
                        key: 'siteName',
                        render: (text, record) => <a className="user-list-name-link" onClick={() => this.handleSiteClick(record)}>{text}</a>,
                        sorter: (a, b) => a.siteName.length - b.siteName.length,
                        sortDirections: ['descend', 'ascend'],
                    },
                    {
                        title: 'Site Location',
                        dataIndex: 'siteLocation',
                        key: 'siteLocation',
                    },
                    {
                        title: 'Site State',
                        dataIndex: 'siteState',
                        key: 'siteState',
                    },
                    {
                        title: 'Site Org',
                        dataIndex: 'siteOrg',
                        key: 'siteOrg',
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
        this.onSelectChange = this.onSelectChange.bind(this);
        this.compareSites = this.compareSites.bind(this);
    }

    componentDidMount() {
        this.loadData(0);
        this.loadAllChemicals()
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
                    selectedRowKeys: [],
                    selectedRows: [],
                    totalRecords: (response.pageCnt * this.state.pageSize)
                });
            }).catch(error => {
                this.setState({
                    isLoading: false,
                    sitesResponse: null,
                    totalRecords: 0,
                    selectedRowKeys: [],
                    selectedRows: [],
                    sitesData: []
                });
            });
    }

    loadAllChemicals() {
        getAllChemicals()
            .then(response => {
                this.setState({
                    chemicals: response
                });
            }).catch(error => {
                this.setState({
                    chemicals: []
                });
            });
    }

    saveFormRef = formRef => {
        this.formRef = formRef;
    };

    onPageChanged(pagination, filters, sorter) {
        this.loadData(pagination.current - 1)
    }

    onSelectChange(selectedRowKeys, selectedRows) {
        this.setState({ selectedRowKeys, selectedRows });
    };

    render() {
        const selectedSite = this.state.selectedSite
        const { loading, selectedRowKeys } = this.state;
        const rowSelection = {
            selectedRowKeys,
            onChange: this.onSelectChange,
        };


        const CollectionCreateForm = Form.create(
            {
                name: 'form_in_modal',
                mapPropsToFields(props) {
                    if (selectedSite) {
                        return {
                            siteName: Form.createFormField({
                                ...props.siteName,
                                value: selectedSite.siteName
                            }),
                            siteLocation: Form.createFormField({
                                ...props.siteLocation,
                                value: selectedSite.siteLocation
                            }),
                            siteState: Form.createFormField({
                                ...props.siteState,
                                value: selectedSite.siteState
                            }),
                            siteOrg: Form.createFormField({
                                ...props.siteOrg,
                                value: selectedSite.siteOrg
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
                        rowSelection={rowSelection}
                        rowKey={record => record.id}
                        columns={this.state.columns}
                        dataSource={this.state.sitesData}
                        onChange={this.onPageChanged}
                        pagination={{ total: this.state.totalRecords, defaultPageSize: this.state.pageSize, current: this.state.currentPage }}
                    />
                </div>
                <CollectionCreateForm
                    wrappedComponentRef={this.saveFormRef}
                    visible={this.state.modalVisible}
                    onCancel={this.handleAddUserCancel}
                    onCreate={this.handleAddUserSubmit}
                    isEdit={this.state.selectedSite != null}
                    chemicals={this.state.chemicals}
                    id={this.state.selectedSite ? this.state.selectedSite.id : null}
                />
            </div>
        );
    }

    renderNavigationButtons() {
        const hasSelected = this.state.selectedRowKeys.length > 0;
        const isAdmin = this.state.isAdmin;

        if (isAdmin) {
            return (
                [
                    <Button key="2" type="primary" onClick={this.compareSites} disabled={!hasSelected} loading={this.state.loading}>
                        Compare
                    </Button>,
                    <Button key="1" onClick={this.handleAddNewDataClick}>Add New Row</Button>,
                ]
            )
        } else {
            return (
                [
                    <Button key="2" type="primary" onClick={this.compareSites} disabled={!hasSelected} loading={this.state.loading}>
                        Compare
                    </Button>,
                ]
            )
        }
    }

    handleDelete(id) {
        this.setState({
            isLoading: true
        });

        deleteSite(id)
            .then(response => {
                notification.success({
                    message: 'rankCare',
                    description: "Site deleted successfully!",
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

    handleSiteClick(site) {
        this.props.history.push("/site-details?sites=" + site.id)
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

    compareSites() {
        const selectedRows = this.state.selectedRows

        const selectedIds = selectedRows.map((site) => site.id)

        this.props.history.push("/site-compare?sites=" + selectedIds)
    }
}

export default SiteData;