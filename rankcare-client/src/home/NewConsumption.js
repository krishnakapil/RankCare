import React, { Component } from 'react';
import {
    Form,
    Button,
    Modal,
    notification,
    InputNumber
} from 'antd';
import { createConsumption, updateConsumtion } from '../util/APIUtils';

class NewConsumption extends Component {
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

            const consumptionRequest = Object.assign({}, values);
            consumptionRequest.id = this.props.id

            if (isEdit) {
                updateConsumtion(consumptionRequest)
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
                createConsumption(consumptionRequest)
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
        if ((rule.field == "ageTo" && value == null) || value > 0) {
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
                title={this.props.isEdit ? "Edit Consumption Data" : "Add Consumption Data"}
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
                        label="Age Group From">
                        {getFieldDecorator('ageFrom', {
                            rules: [
                                { required: true, message: 'Please input age group from!' },
                                { validator: this.checkNumber },
                            ],
                        })(<InputNumber />)}
                    </Form.Item>
                    <Form.Item
                        label="Age Group To">
                        {getFieldDecorator('ageTo', {
                            rules: [
                                { validator: this.checkNumber },
                            ],
                        })(<InputNumber />)}
                    </Form.Item>
                    <Form.Item
                        label="Body Weight Avg">
                        {getFieldDecorator('bodyWtAvg', {
                            rules: [
                                { required: true, message: 'Please input Bodu Weight Average!' },
                                { validator: this.checkNumber },
                            ],
                        })(<InputNumber />)}
                    </Form.Item>
                    <Form.Item
                        label="CI Data 1">
                        {getFieldDecorator('ciData1', {
                            rules: [
                                { required: true, message: 'Please input CI Data 1!' },
                                { validator: this.checkNumber },
                            ],
                        })(<InputNumber />)}
                    </Form.Item>
                    <Form.Item label="CI Data 2">
                        {getFieldDecorator('ciData2', {
                            rules: [
                                { required: true, message: 'Please input CI Data 2!' },
                                { validator: this.checkNumber },
                            ]
                        })(<InputNumber />)}
                    </Form.Item>
                    <Form.Item
                        label="Soil In Avg">
                        {getFieldDecorator('soilInvAvg', {
                            rules: [
                                { required: true, message: 'Please input Soil In Avg!' },
                                { validator: this.checkNumber },
                            ],
                        })(<InputNumber />)}
                    </Form.Item>
                    <Form.Item label="Water Consumption Avg">
                        {getFieldDecorator('waterConsAvg', {
                            rules: [
                                { required: true, message: 'Please input Water Consumption Avg!' },
                                { validator: this.checkNumber },
                            ]
                        })(<InputNumber />)}
                    </Form.Item>
                </Form>
            </Modal>
        )
    }
}

export default NewConsumption;