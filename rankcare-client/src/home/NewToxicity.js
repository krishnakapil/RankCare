import React, { Component } from 'react';
import {
    Form,
    Input,
    Button,
    Modal,
    notification,
    InputNumber
} from 'antd';
import { createToxicity, updateToxicity } from '../util/APIUtils';

class NewToxicity extends Component {
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

            const toxicityRequest = Object.assign({}, values);
            toxicityRequest.id = this.props.id

            if (isEdit) {
                updateToxicity(toxicityRequest)
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
            } else {
                createToxicity(toxicityRequest)
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
        console.log("Value " + value)
        if (value > 0) {
          callback();
          return;
        }
        callback('This field should be number!');
    };

    render() {
        const { visible, onCancel, form } = this.props;
        const { getFieldDecorator } = form;

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

        return (
            <Modal
                width="600px"
                title={this.props.isEdit ? "Edit Toxicity Data" : "Add Toxicity Data"}
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
                        label="Chemical Name">
                        {getFieldDecorator('chemicalName', {
                            rules: [
                                { required: true, message: 'Please input chemical name!' },
                            ],
                        })(<Input />)}
                    </Form.Item>
                    <Form.Item
                        label="Chemical Formula">
                        {getFieldDecorator('chemicalFormula', {
                            rules: [
                                { required: true, message: 'Please input Chemical Formula!' },
                            ],
                        })(<Input />)}
                    </Form.Item>
                    <Form.Item
                        label="Soil Guideline (mg/kg)">
                        {getFieldDecorator('soilGuideline', {
                            rules: [
                                { required: true, message: 'Please input Soil Guideline!' },
                                { validator: this.checkNumber },
                            ],
                        })(<InputNumber />)}
                    </Form.Item>
                    <Form.Item
                        label="Soil Reference">
                        {getFieldDecorator('soilRef', {
                            rules: [
                                { required: true, message: 'Please input Soil Reference!' },
                            ],
                        })(<Input />)}
                    </Form.Item>
                    <Form.Item label="Water Guideline (mg/ltr)">
                        {getFieldDecorator('waterGuideline', {
                            rules: [
                                { required: true, message: 'Please input Water Guideline!' },
                                { validator: this.checkNumber },
                            ]
                        })(<InputNumber />)}
                    </Form.Item>
                    <Form.Item
                        label="Water Reference">
                        {getFieldDecorator('waterRef', {
                            rules: [{ required: true, message: 'Please input Water Reference!' }],
                        })(<Input />)}
                    </Form.Item>
                    <Form.Item label="Dosage Reference">
                        {getFieldDecorator('dosageRef', {
                            rules: [
                                { required: true, message: 'Please input Dosage Reference!' },
                                { validator: this.checkNumber },
                            ]
                        })(<InputNumber />)}
                    </Form.Item>
                    <Form.Item
                        label="Reference">
                        {getFieldDecorator('reference', {
                            rules: [{ required: true, message: 'Please input Reference!' }],
                        })(<Input />)}
                    </Form.Item>
                    <Form.Item label="Cancer Slope Factor">
                        {getFieldDecorator('cancerSlopeFactor', {
                            rules: [
                                { required: true, message: 'Please input Cancer Slope Factor!' },
                                { validator: this.checkNumber },
                            ]
                        })(<InputNumber />)}
                    </Form.Item>
                    <Form.Item
                        label="Cancer Slope Reference">
                        {getFieldDecorator('cancerSlopeRef', {
                            rules: [{ required: true, message: 'Please input Cancer Slope Reference!' }],
                        })(<Input />)}
                    </Form.Item>
                </Form>
            </Modal>
        )
    }
}

export default NewToxicity;