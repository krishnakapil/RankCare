import React, { Component } from 'react';
import { PageHeader, Table, Button, Popconfirm, Icon, Form, notification } from 'antd';
import { getConsumptionData, deleteConsumption } from '../util/APIUtils';
import NewConsumption from './NewConsumption';

class ConsumptionData extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentUser: props.currentUser,
            isAdmin: props.currentUser.isAdmin,
            isLoading: false,
            modalVisible: false,
            selectedConsumption: null,
            consumptionResponse: null,
            consumptionData: [],
            currentPage: 0,
            pageSize : 20,
            totalRecords : 0,
            columns: props.currentUser.isAdmin ? [
                {
                    title: 'Age Group',
                    dataIndex: 'ageGrp',
                    key: 'ageGrp',
                    render: (text, record) => <a className="user-list-name-link" onClick={() => this.handleConsumptionClick(record)}>{text}</a>,
                    sorter: (a, b) => a.ageGrp.length - b.ageGrp.length,
                    sortDirections: ['descend', 'ascend'],
                },
                {
                    title: 'Body Weight Mean',
                    dataIndex: 'bodyWtMean',
                    key: 'bodyWtMean',
                },
                {
                    title: 'Body Weight SD',
                    dataIndex: 'bodyWtSd',
                    key: 'bodyWtSd',
                },
                {
                    title: 'Soil Ingestion Avg (mg/day)',
                    dataIndex: 'soilInvAvg',
                    key: 'soilInvAvg',
                },
                {
                    title: 'Confidence Limit 95% of soil',
                    dataIndex: 'ciData1',
                    key: 'ciData1',
                },
                {
                    title: 'Soil Ingestion Geo Mean',
                    dataIndex: 'soilInvGomMean',
                    key: 'soilInvGomMean',
                },
                {
                    title: 'Soil Ingestion Geo SD',
                    dataIndex: 'soilInvGomSd',
                    key: 'soilInvGomSd',
                },
                {
                    title: 'Water Consumption Avg (ltr/day)',
                    dataIndex: 'waterConsAvg',
                    key: 'waterConsAvg',
                },
                {
                    title: 'Confidence Limit 95% of water',
                    dataIndex: 'ciData2',
                    key: 'ciData2',
                },
                {
                    title: 'Water Consumption Geo Mean',
                    dataIndex: 'waterInvGomMean',
                    key: 'waterInvGomMean',
                },
                {
                    title: 'Water Consumption Geo SD',
                    dataIndex: 'waterInvGomSd',
                    key: 'waterInvGomSd',
                },
            ] :
            [
                {
                    title: 'Age Group',
                    dataIndex: 'ageGrp',
                    key: 'ageGrp',
                    sorter: (a, b) => a.ageGrp.length - b.ageGrp.length,
                    sortDirections: ['descend', 'ascend'],
                },
                {
                    title: 'Body Weight Mean',
                    dataIndex: 'bodyWtMean',
                    key: 'bodyWtMean',
                },
                {
                    title: 'Body Weight SD',
                    dataIndex: 'bodyWtSd',
                    key: 'bodyWtSd',
                },
                {
                    title: 'Soil Ingestion Avg (mg/day)',
                    dataIndex: 'soilInvAvg',
                    key: 'soilInvAvg',
                },
                {
                    title: 'Confidence Limit 95% of soil',
                    dataIndex: 'ciData1',
                    key: 'ciData1',
                },
                {
                    title: 'Soil Ingestion Geo Mean',
                    dataIndex: 'soilInvGomMean',
                    key: 'soilInvGomMean',
                },
                {
                    title: 'Soil Ingestion Geo SD',
                    dataIndex: 'soilInvGomSd',
                    key: 'soilInvGomSd',
                },
                {
                    title: 'Avg Water Consumption (ltr/day)',
                    dataIndex: 'waterConsAvg',
                    key: 'waterConsAvg',
                },
                {
                    title: 'Confidence Limit 95% of water',
                    dataIndex: 'ciData2',
                    key: 'ciData2',
                },
                {
                    title: 'Water Consumption Geo Mean',
                    dataIndex: 'waterInvGomMean',
                    key: 'waterInvGomMean',
                },
                {
                    title: 'Water Consumption Geo SD',
                    dataIndex: 'waterInvGomSd',
                    key: 'waterInvGomSd',
                },
            ]
        }

        this.handleBackClick = this.handleBackClick.bind(this)
        this.handleAddNewDataClick = this.handleAddNewDataClick.bind(this)
        this.handleDelete = this.handleDelete.bind(this)
        this.handleAddUserSubmit = this.handleAddUserSubmit.bind(this)
        this.handleAddUserCancel = this.handleAddUserCancel.bind(this)
        this.handleConsumptionClick = this.handleConsumptionClick.bind(this)
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
        getConsumptionData(page, this.state.pageSize)
            .then(response => {
                this.setState({
                    isLoading: false,
                    consumptionResponse: response,
                    consumptionData: response.data,
                    totalRecords: (response.pageCnt * this.state.pageSize)
                });
            }).catch(error => {
                this.setState({
                    isLoading: false,
                    consumptionResponse: null,
                    totalRecords: 0,
                    consumptionData: []
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
        const selectedConsumption = this.state.selectedConsumption

        const CollectionCreateForm = Form.create(
            {
                name: 'form_in_modal',
                mapPropsToFields(props) {
                    if (selectedConsumption) {
                        return {
                            ageGrp: Form.createFormField({
                                ...props.ageGrp,
                                value: selectedConsumption.ageGrp
                            }),
                            bodyWtMean: Form.createFormField({
                                ...props.bodyWtMean,
                                value: selectedConsumption.bodyWtMean
                            }),
                            bodyWtSd: Form.createFormField({
                                ...props.bodyWtSd,
                                value: selectedConsumption.bodyWtSd
                            }),
                            soilInvAvg: Form.createFormField({
                                ...props.soilInvAvg,
                                value: selectedConsumption.soilInvAvg
                            }),
                            ciData1: Form.createFormField({
                                ...props.ciData1,
                                value: selectedConsumption.ciData1
                            }),
                            soilInvGomMean: Form.createFormField({
                                ...props.soilInvGomMean,
                                value: selectedConsumption.soilInvGomMean
                            }),
                            soilInvGomSd: Form.createFormField({
                                ...props.soilInvGomSd,
                                value: selectedConsumption.soilInvGomSd
                            }),
                            waterConsAvg: Form.createFormField({
                                ...props.waterConsAvg,
                                value: selectedConsumption.waterConsAvg
                            }),
                            ciData2: Form.createFormField({
                                ...props.ciData2,
                                value: selectedConsumption.ciData2
                            }),
                            waterInvGomMean: Form.createFormField({
                                ...props.waterInvGomMean,
                                value: selectedConsumption.waterInvGomMean
                            }),
                            waterInvGomSd: Form.createFormField({
                                ...props.waterInvGomSd,
                                value: selectedConsumption.waterInvGomSd
                            }),
                        };
                    } else {
                        return {};
                    }
                },
            }
        )(NewConsumption)

        return (
            <div>
                <div className="user-home-container">
                    <PageHeader className="user-list-page-title" title="Consumption Data" onBack={this.handleBackClick} extra={this.renderNavigationButtons()} />
                    <Table
                        rowKey={record => record.id}
                        columns={this.state.columns}
                        dataSource={this.state.consumptionData}
                        onChange={this.onPageChanged}
                        pagination={{total: this.state.totalRecords, defaultPageSize: this.state.pageSize, current: this.state.currentPage}}
                    />
                </div>
                <CollectionCreateForm
                    wrappedComponentRef={this.saveFormRef}
                    visible={this.state.modalVisible}
                    onCancel={this.handleAddUserCancel}
                    onCreate={this.handleAddUserSubmit}
                    isEdit={this.state.selectedConsumption != null}
                    id={this.state.selectedConsumption ? this.state.selectedConsumption.id : null}
                />
            </div>
        );
    }

    renderNavigationButtons() {
        if(this.state.isAdmin) {
            return (
                [
                    <Button key="1" onClick={this.handleAddNewDataClick}>Add New Row</Button>,
                ]
            )
        }
    }

    handleDelete(id) {
        this.setState({
            isLoading: true
        });

        deleteConsumption(id)
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

    handleConsumptionClick(consumption) {
        this.setState({
            modalVisible: true,
            selectedConsumption: consumption
        });
    }

    handleAddNewDataClick() {
        this.setState({
            modalVisible: true,
            selectedConsumption: null
        });
    }

    handleBackClick() {
        this.props.history.goBack()
    }

    handleAddUserSubmit() {
        this.setState({
            modalVisible: false,
            selectedConsumption: null
        });

        this.loadData(this.state.currentPage)
    }

    handleAddUserCancel() {
        this.setState({
            modalVisible: false,
            selectedConsumption: null
        });
    }
}

export default ConsumptionData;