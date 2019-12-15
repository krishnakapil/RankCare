import React, { Component } from 'react';
import {
    Form,
    Button,
    Modal,
    notification,
    InputNumber,
    Select
} from 'antd';

import { createConsumption, updateConsumtion } from '../util/APIUtils';

const { Option } = Select;

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
        if ((rule.field === "ageTo" && value === null) || value > 0) {
            callback();
            return;
        }
        callback('This field should be number!');
    };

    render() {
        const { visible, onCancel, form } = this.props;
        const { getFieldDecorator, getFieldValue } = form;

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
                width="800px"
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
                        {getFieldDecorator('ageGrp', {
                            rules: [
                                { required: true, message: 'Please input age group!' },
                            ],
                        })(
                            <Select
                                value={getFieldValue('ageGrp')}
                                style={{ width: '25%' }}
                            >
                                <Option value="0-6">0-6</Option>
                                <Option value="6-18">6-18</Option>
                                <Option value="18+">18+</Option>
                            </Select>
                        )}
                    </Form.Item>
                    <Form.Item
                        label="Body Weight Mean">
                        {getFieldDecorator('bodyWtMean', {
                            rules: [
                                { required: true, message: 'Please input Body Weight Mean!' },
                                { validator: this.checkNumber },
                            ],
                        })(<InputNumber />)}
                    </Form.Item>
                    <Form.Item
                        label="Body Weight Standard Deviation">
                        {getFieldDecorator('bodyWtSd', {
                            rules: [
                                { required: true, message: 'Please input Body Weight Standard Deviation!' },
                                { validator: this.checkNumber },
                            ],
                        })(<InputNumber />)}
                    </Form.Item>
                    <Form.Item
                        label="Soil Ingestion Avg (mg/day)">
                        {getFieldDecorator('soilInvAvg', {
                            rules: [
                                { required: true, message: 'Please input Soil In Avg!' },
                                { validator: this.checkNumber },
                            ],
                        })(<InputNumber />)}
                    </Form.Item>
                    <Form.Item
                        label="Confidence Limit 95% of soil">
                        {getFieldDecorator('ciData1', {
                            rules: [
                                { required: true, message: 'Please input CI Data for Soil!' },
                                { validator: this.checkNumber },
                            ],
                        })(<InputNumber />)}
                    </Form.Item>
                    <Form.Item
                        label="Soil Ingestion Geo Mean">
                        {getFieldDecorator('soilInvGomMean', {
                            rules: [
                                { required: true, message: 'Please input Soil Ingestion Geo Mean!' },
                                { validator: this.checkNumber },
                            ],
                        })(<InputNumber />)}
                    </Form.Item>
                    <Form.Item
                        label="Soil Ingestion Geo Sd">
                        {getFieldDecorator('soilInvGomSd', {
                            rules: [
                                { required: true, message: 'Please input Soil Ingestion Standard Deviation!' },
                                { validator: this.checkNumber },
                            ],
                        })(<InputNumber />)}
                    </Form.Item>
                    <Form.Item label="Water Consumption Avg (ltr/day)">
                        {getFieldDecorator('waterConsAvg', {
                            rules: [
                                { required: true, message: 'Please input Water Consumption Avg!' },
                                { validator: this.checkNumber },
                            ]
                        })(<InputNumber />)}
                    </Form.Item>
                    <Form.Item label="Confidence Limit 95% of water">
                        {getFieldDecorator('ciData2', {
                            rules: [
                                { required: true, message: 'Please input CI Data of water!' },
                                { validator: this.checkNumber },
                            ]
                        })(<InputNumber />)}
                    </Form.Item>
                    <Form.Item label="Water Consumption Geo Mean">
                        {getFieldDecorator('waterInvGomMean', {
                            rules: [
                                { required: true, message: 'Please input Water Geo Mean!' },
                                { validator: this.checkNumber },
                            ]
                        })(<InputNumber />)}
                    </Form.Item>
                    <Form.Item label="Water Consumption Geo Sd">
                        {getFieldDecorator('waterInvGomSd', {
                            rules: [
                                { required: true, message: 'Please input Water Geo Sd!' },
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