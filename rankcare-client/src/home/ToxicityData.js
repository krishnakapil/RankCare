import React, { Component } from 'react';
import { PageHeader, Table, Button, Popconfirm, Icon, Form, notification } from 'antd';
import { getToxicityData, deleteToxicity } from '../util/APIUtils';
import NewToxicity from './NewToxicity';

class ToxicityData extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentUser: props.currentUser,
            isLoading: false,
            modalVisible: false,
            selectedToxicity: null,
            toxicityResponse: null,
            toxicityData: [],
            currentPage: 0,
            pageSize : 20,
            totalRecords : 0,
            columns: [
                {
                    title: 'Chemical Name',
                    dataIndex: 'chemicalName',
                    key: 'chemicalName',
                    render: (text, record) => <a className="user-list-name-link" onClick={() => this.handleToxicityClick(record)}>{text}</a>,
                    sorter: (a, b) => a.chemicalName.length - b.chemicalName.length,
                    sortDirections: ['descend', 'ascend'],
                },
                {
                    title: 'Formula',
                    dataIndex: 'chemicalFormula',
                    key: 'chemicalFormula',
                },
                {
                    title: 'Soil Guideline',
                    dataIndex: 'soilGuideline',
                    key: 'soilGuideline',
                },
                {
                    title: 'Soil Ref',
                    dataIndex: 'soilRef',
                    key: 'soilRef',
                },
                {
                    title: 'Water Guideline',
                    dataIndex: 'waterGuideline',
                    key: 'waterGuideline',
                },
                {
                    title: 'Water Ref',
                    dataIndex: 'waterRef',
                    key: 'waterRef',
                },
                {
                    title: 'Dosage Ref',
                    dataIndex: 'dosageRef',
                    key: 'dosageRef'
                },
                {
                    title: 'Reference',
                    dataIndex: 'reference',
                    key: 'reference'
                },
                {
                    title: 'Cancer Slope Factor',
                    dataIndex: 'cancerSlopeFactor',
                    key: 'cancerSlopeFactor'
                },
                {
                    title: 'Cancer Slope Ref',
                    dataIndex: 'cancerSlopeRef',
                    key: 'cancerSlopeRef'
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
        this.handleToxicityClick = this.handleToxicityClick.bind(this)
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
        getToxicityData(page, this.state.pageSize)
            .then(response => {
                this.setState({
                    isLoading: false,
                    toxicityResponse: response,
                    toxicityData: response.data,
                    totalRecords: (response.pageCnt * this.state.pageSize)
                });
            }).catch(error => {
                this.setState({
                    isLoading: false,
                    toxicityResponse: null,
                    totalRecords: 0,
                    toxicityData: []
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
        const selectedToxicity = this.state.selectedToxicity

        const CollectionCreateForm = Form.create(
            {
                name: 'form_in_modal',
                mapPropsToFields(props) {
                    if (selectedToxicity) {
                        return {
                            chemicalName: Form.createFormField({
                                ...props.chemicalName,
                                value: selectedToxicity.chemicalName
                            }),
                            chemicalFormula: Form.createFormField({
                                ...props.chemicalFormula,
                                value: selectedToxicity.chemicalFormula
                            }),
                            soilGuideline: Form.createFormField({
                                ...props.soilGuideline,
                                value: selectedToxicity.soilGuideline
                            }),
                            soilRef: Form.createFormField({
                                ...props.soilRef,
                                value: selectedToxicity.soilRef
                            }),
                            waterGuideline: Form.createFormField({
                                ...props.waterGuideline,
                                value: selectedToxicity.waterGuideline
                            }),
                            waterRef: Form.createFormField({
                                ...props.dosageRef,
                                value: selectedToxicity.waterRef
                            }),
                            dosageRef: Form.createFormField({
                                ...props.dosageRef,
                                value: selectedToxicity.dosageRef
                            }),
                            reference: Form.createFormField({
                                ...props.reference,
                                value: selectedToxicity.reference
                            }),
                            cancerSlopeFactor: Form.createFormField({
                                ...props.cancerSlopeFactor,
                                value: selectedToxicity.cancerSlopeFactor
                            }),
                            cancerSlopeRef: Form.createFormField({
                                ...props.cancerSlopeRef,
                                value: selectedToxicity.cancerSlopeRef
                            }),
                        };
                    } else {
                        return {};
                    }
                },
            }
        )(NewToxicity)

        return (
            <div>
                <div className="user-home-container">
                    <PageHeader className="user-list-page-title" title="Toxicity Data" onBack={this.handleBackClick} extra={this.renderNavigationButtons()} />
                    <Table
                        rowKey={record => record.id}
                        columns={this.state.columns}
                        dataSource={this.state.toxicityData}
                        onChange={this.onPageChanged}
                        pagination={{total: this.state.totalRecords, defaultPageSize: this.state.pageSize, current: this.state.currentPage}}
                    />
                </div>
                <CollectionCreateForm
                    wrappedComponentRef={this.saveFormRef}
                    visible={this.state.modalVisible}
                    onCancel={this.handleAddUserCancel}
                    onCreate={this.handleAddUserSubmit}
                    isEdit={this.state.selectedToxicity != null}
                    id={this.state.selectedToxicity ? this.state.selectedToxicity.id : null}
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

    handleToxicityClick(user) {
        this.setState({
            modalVisible: true,
            selectedToxicity: user
        });
    }

    handleAddNewDataClick() {
        this.setState({
            modalVisible: true,
            selectedToxicity: null
        });
    }

    handleBackClick() {
        this.props.history.goBack()
    }

    handleAddUserSubmit() {
        this.setState({
            modalVisible: false,
            selectedToxicity: null
        });

        this.loadData(this.state.currentPage)
    }

    handleAddUserCancel() {
        this.setState({
            modalVisible: false,
            selectedToxicity: null
        });
    }
}

export default ToxicityData;