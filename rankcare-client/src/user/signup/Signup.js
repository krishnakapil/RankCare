import React, { Component } from 'react';
import { requestAccess } from '../../util/APIUtils';
import '../login/Login.css';
import { ACCESS_TOKEN } from '../../constants';

import { Form, Input, Button, Icon, notification } from 'antd';
const FormItem = Form.Item;

class Signup extends Component {
    render() {
        const AntWrappedLoginForm = Form.create()(SignUpForm)
        return (
            <div className="login-container">
                <h1 className="page-title">Request Access</h1>
                <div className="login-content">
                    <AntWrappedLoginForm onRequestAccess={this.props.onRequestAccess} />
                </div>
            </div>
        );
    }
}

class SignUpForm extends Component {
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    state = {
        loading: false
    };

    handleSubmit(event) {
        event.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (!err) {
                this.setState({ loading: true });

                const loginRequest = Object.assign({}, values);
                requestAccess(loginRequest)
                    .then(response => {
                        notification.success({
                            message: 'rankCare',
                            description: 'Access request sent!'
                        });
                        this.props.onRequestAccess();
                    }).catch(error => {
                        this.setState({ loading: false });
                        notification.error({
                            message: 'rankCare',
                            description: error.message || 'Sorry! Something went wrong. Please try again!'
                        });
                    });
            }
        });
    }

    render() {
        const { getFieldDecorator } = this.props.form;

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
            <Form onSubmit={this.handleSubmit} {...formItemLayout}>
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
                <FormItem>
                    <Button type="primary" htmlType="submit" size="large" className="login-form-button" loading={this.state.loading}>Request Access</Button>
                </FormItem>
            </Form>
        );
    }
}


export default Signup;