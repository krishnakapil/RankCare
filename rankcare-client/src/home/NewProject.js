import React, { Component } from 'react';
import {
    Form,
    Button,
    Modal,
    Input,
    Icon,
    notification
} from 'antd';
import { createProject, updateProject } from '../util/APIUtils';

let id = 0;

class NewProject extends Component {
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

            const projectRequest = Object.assign({}, values);
            projectRequest.id = this.props.id

            if (isEdit) {
                updateProject(projectRequest)
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
                createProject(projectRequest)
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
                width="400px"
                title={this.props.isEdit ? "Edit Project" : "Add Project"}
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
                        label="Project Name">
                        {getFieldDecorator('projectName', {
                            rules: [
                                { required: true, message: 'Please input project name!' },
                            ],
                        })(<Input />)}
                    </Form.Item>
                </Form>
            </Modal>
        )
    }
}

export default NewProject;