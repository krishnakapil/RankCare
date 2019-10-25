import React, { Component } from 'react';
import {
    Form,
    Button,
    Modal,
    Input,
    Icon,
    notification
} from 'antd';
import ChemicalInput from './ChemicalInput';
import { createSite, updateSite } from '../util/APIUtils';

let id = 0;

class NewSite extends Component {
    constructor(props) {
        super(props);
        this.state = {
            confirmLoading: false,
            isLoading: false
        }
        this.handleOkClick = this.handleOkClick.bind(this);
    }

    handleOkClick = () => {
        const isEdit = this.props.isEdit

        this.props.form.validateFields((err, values) => {
            if (err) {
                return;
            }

            this.setState({
                confirmLoading: true,
                isLoading: true
            });

            const nonNullChemicals = values.chemicals.filter(function (chemical) {
                return chemical
            });

            const siteRequest = {
                "id": this.props.id,
                "siteName": values.siteName,
                "siteLocation": values.siteLocation,
                "state": values.siteState,
                "orgName": values.siteOrg,
                "siteContaminant": nonNullChemicals
            }

            console.log("Form values " + JSON.stringify(siteRequest));

            // const consumptionRequest = Object.assign({}, values);
            // consumptionRequest.id = this.props.id

            if (isEdit) {
                // updateSite(consumptionRequest)
                //     .then(response => {
                //         this.setState({
                //             confirmLoading: false,
                //             isLoading: false
                //         });
                //         this.props.form.resetFields();
                //         this.props.onCreate();
                //     }).catch(error => {
                //         this.setState({ isLoading: false });
                //         notification.error({
                //             message: 'rankCare',
                //             description: error.message || 'Sorry! Something went wrong. Please try again!'
                //         });
                //     });
            } else {
                createSite(siteRequest)
                    .then(response => {
                        this.setState({
                            confirmLoading: false,
                            isLoading: false
                        });
                        this.props.form.resetFields();
                        this.props.onCreate();
                    }).catch(error => {
                        this.setState({ isLoading: false });
                        notification.error({
                            message: 'rankCare',
                            description: error.message || 'Sorry! Something went wrong. Please try again!'
                        });
                    });
            }
        });
    }

    checkNumber = (rule, value, callback) => {
        if (value == null || value.contaminationValue || value.contaminationValue > 0) {
            callback();
            return;
        }
        callback('This field should be number!');
    };

    componentDidMount() {
        const keys = this.props.form.getFieldValue('keys');
        if (keys.length === 0) {
            this.add();
        }
    }

    remove = k => {
        const { form } = this.props;
        const keys = form.getFieldValue('keys');

        // can use data-binding to set
        form.setFieldsValue({
            keys: keys.filter(key => key !== k),
        });
    };

    add = () => {
        const { form } = this.props;
        // can use data-binding to get
        const keys = form.getFieldValue('keys');
        const nextKeys = keys.concat(id++);
        // can use data-binding to set
        // important! notify form to detect changes
        form.setFieldsValue({
            keys: nextKeys,
        });
    };

    render() {
        const formItemLayout = {
            labelCol: {
                xs: { span: 24 },
                sm: { span: 8 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
            },
        };

        const formItemLayoutWithOutLabel = {
            wrapperCol: {
                xs: { span: 24, offset: 0 },
                sm: { span: 20, offset: 4 },
            },
        };

        const { visible, onCancel, form } = this.props;
        const { getFieldDecorator, getFieldValue } = form;
        getFieldDecorator('keys', { initialValue: [] });
        const keys = getFieldValue('keys');
        const formItems = keys.map((k, index) => (
            <Form.Item
                {...formItemLayout}
                label={'Chemical ' + (index + 1)}
                required={false}
                key={k}
            >
                {getFieldDecorator(`chemicals[${k}]`, {
                    validateTrigger: ['onChange', 'onBlur'],
                    rules: [
                        {
                            required: true,
                            message: index == 0 ? "Please input chemical contamination value" : "Please input chemical contamination value or delete the field",
                        },
                        { validator: this.checkNumber },
                    ],
                })(<ChemicalInput showRemove={index > 0} chemicals={this.props.chemicals} style={{ width: '60%', marginRight: 8 }} />)}
                {
                    index > 0 ? <Icon
                        className="dynamic-delete-button"
                        type="minus-circle-o"
                        onClick={() => this.remove(k)}
                    /> : null
                }
            </Form.Item>
        ));

        return (
            <Modal
                width="800px"
                title={this.props.isEdit ? "Edit Site Data" : "Add Site Data"}
                visible={visible}
                onOk={this.handleOkClick}
                onCancel={onCancel}
                confirmLoading={this.state.confirmLoading}
                footer={[
                    <Button key="back" onClick={onCancel}>
                        Cancel
                    </Button>,
                    <Button type="primary" key="submit" loading={this.state.isLoading} onClick={this.handleOkClick}>
                        Submit
                    </Button>,
                ]}>
                <Form {...formItemLayout}>
                    <Form.Item
                        label="Site Name">
                        {getFieldDecorator('siteName', {
                            rules: [
                                { required: true, message: 'Please input site name!' },
                            ],
                        })(<Input />)}
                    </Form.Item>
                    <Form.Item
                        label="Site Location">
                        {getFieldDecorator('siteLocation', {
                            rules: [
                                { required: true, message: 'Please input Site Location!' },
                            ],
                        })(<Input />)}
                    </Form.Item>
                    <Form.Item
                        label="Site State">
                        {getFieldDecorator('siteState', {
                            rules: [
                                { required: true, message: 'Please input Site State!' },
                            ],
                        })(<Input />)}
                    </Form.Item>
                    <Form.Item label="Site Org">
                        {getFieldDecorator('siteOrg', {
                            rules: [
                                { required: true, message: 'Please input Site Org!' },
                            ]
                        })(<Input />)}
                    </Form.Item>
                    {formItems}
                    <Form.Item {...formItemLayoutWithOutLabel}>
                        <Button type="dashed" onClick={this.add} style={{ width: '60%' }}>
                            <Icon type="plus" /> Add Chemical
                        </Button>
                    </Form.Item>
                </Form>
            </Modal>
        )
    }
}

export default NewSite;