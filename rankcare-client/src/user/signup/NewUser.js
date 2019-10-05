import React, { Component } from 'react';
import {
    Form,
    Input,
    Tooltip,
    Icon,
    Button,
    Modal,
    Switch,
    notification
} from 'antd';
import './NewUser.css';
import { signup, udpateUser } from '../../util/APIUtils';

class NewUser extends Component {
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

            const signUpRequest = Object.assign({}, values);
            signUpRequest.id = this.props.id

            if (isEdit) {
                udpateUser(signUpRequest)
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
                signup(signUpRequest)
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
                title={this.props.isEdit ? "Edit User" : "Add New User"}
                visible={visible}
                onOk={this.handleOkClick}
                onCancel={onCancel}
                confirmLoading={this.state.confirmLoading}
                footer={[
                    <Button key="back" onClick={onCancel}>
                        Cancel
                    </Button>,
                    <Button type="primary" loading={this.state.isLoading} onClick={this.handleOkClick}>
                        Submit
                    </Button>,
                ]}>
                <Form {...formItemLayout}>
                    <Form.Item
                        label="Name">
                        {getFieldDecorator('name', {
                            rules: [
                                { required: true, message: 'Please input name!' },
                                {
                                    min: 4,
                                    max: 40,
                                },
                            ],
                        })(<Input />)}
                    </Form.Item>
                    <Form.Item
                        label={
                            <span>
                                Username&nbsp;
                                <Tooltip title="This is used for login">
                                    <Icon type="question-circle-o" />
                                </Tooltip>
                            </span>
                        }
                    >
                        {getFieldDecorator('username', {
                            rules: [
                                { required: true, message: 'Please input username!' },
                                {
                                    min: 3,
                                    max: 15,
                                },
                            ],
                        })(<Input disabled={this.props.isEdit} />)}
                    </Form.Item>
                    <Form.Item label="Password" hasFeedback>
                        {getFieldDecorator('password', {
                            rules: [
                                {
                                    required: !this.props.isEdit,
                                    message: 'Please input password!',
                                },
                                {
                                    min: 6,
                                    max: 20,
                                },
                            ],
                        })(<Input.Password />)}
                    </Form.Item>
                    <Form.Item label="E-mail">
                        {getFieldDecorator('email', {
                            rules: [
                                {
                                    type: 'email',
                                    message: 'The input is not valid E-mail!',
                                },
                                {
                                    required: true,
                                    message: 'Please input E-mail!',
                                },
                                {
                                    max: 40,
                                },
                            ],
                        })(<Input disabled={this.props.isEdit} />)}
                    </Form.Item>
                    <Form.Item label="Phone Number">
                        {getFieldDecorator('phoneNumber', {
                            rules: [
                                { required: true, message: 'Please input phone number!' },
                                { regexp: '^[0-9]*$' },
                                {
                                    min: 6,
                                    max: 15,
                                },
                            ]
                        })(<Input style={{ width: '100%' }} />)}
                    </Form.Item>
                    <Form.Item
                        label="Organization">
                        {getFieldDecorator('organization', {
                            rules: [{ required: true, message: 'Please input organization!' }],
                        })(<Input />)}
                    </Form.Item>
                    <Form.Item
                        label="Designation">
                        {getFieldDecorator('designation', {
                            rules: [{ required: true, message: 'Please input designation!' }],
                        })(<Input />)}
                    </Form.Item>
                    <Form.Item
                        label="Is Admin">
                        {getFieldDecorator('isAdmin', { valuePropName: 'checked' })(<Switch disabled={this.props.isEdit}/>)}
                    </Form.Item>
                </Form>
            </Modal>
        )
    }
}

export default NewUser;