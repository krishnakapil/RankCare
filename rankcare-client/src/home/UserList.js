import React, { Component } from 'react';
import { PageHeader, Table, Tag, Button, Popconfirm, Icon, Form, notification } from 'antd';
import './UserList.css';
import { getAllUsers, deleteUser } from '../util/APIUtils';
import { ACCESS_TOKEN } from '../constants';
import NewUser from '../user/signup/NewUser';

class UserList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentUser: props.currentUser,
            isAuthenticated: localStorage.getItem(ACCESS_TOKEN) != null,
            isLoading: false,
            userModalVisible: false,
            selectedUser: null,
            users: [],
            columns: [
                {
                    title: 'Name',
                    dataIndex: 'name',
                    key: 'name',
                    render: (text, record) => <a className="user-list-name-link" onClick={() => this.handleUserClicked(record)}>{text}</a>,
                    sorter: (a, b) => a.name.length - b.name.length,
                    sortDirections: ['descend', 'ascend'],
                },
                {
                    title: 'User Name',
                    dataIndex: 'username',
                    key: 'username',
                },
                {
                    title: 'Email',
                    dataIndex: 'email',
                    key: 'email',
                },
                {
                    title: 'Phone Number',
                    dataIndex: 'phoneNumber',
                    key: 'phoneNumber',
                },
                {
                    title: 'Org',
                    dataIndex: 'organization',
                    key: 'organization',
                    sorter: (a, b) => a.organization.length - b.organization.length,
                    sortDirections: ['descend', 'ascend'],
                },
                {
                    title: 'Role',
                    dataIndex: 'roles',
                    key: 'roles',
                    sorter: (a, b) => a.roles[0].name.length - b.roles[0].name.length,
                    sortDirections: ['descend', 'ascend'],
                    render: roles => (
                        <span>
                            {roles.map(role => {
                                let color = role.name == "ROLE_ADMIN" ? 'red' : 'green';
                                let roleName = role.name == "ROLE_ADMIN" ? 'Admin' : 'Client'
                                return (
                                    <Tag color={color} key={roleName}>
                                        {roleName.toUpperCase()}
                                    </Tag>
                                );
                            })}
                        </span>
                    ),
                },
                {
                    title: 'Action',
                    dataIndex: 'id',
                    key: 'id',
                    render: (text, record) =>
                        this.state.users.length >= 1 && record.id != this.state.currentUser.id ? (
                            <Popconfirm title="Sure to delete?" onConfirm={() => this.handleDelete(record.id)}>
                                <a className="user-list-delete-link">
                                    <Icon type="delete" className="nav-icon" />
                                </a>
                            </Popconfirm>
                        ) : null,
                },
            ]
        }

        this.handleManagerUsersClick = this.handleManagerUsersClick.bind(this)
        this.handleAddNewUserClick = this.handleAddNewUserClick.bind(this)
        this.handleDelete = this.handleDelete.bind(this)
        this.handleAddUserSubmit = this.handleAddUserSubmit.bind(this)
        this.handleAddUserCancel = this.handleAddUserCancel.bind(this)
        this.handleUserClicked = this.handleUserClicked.bind(this)
    }

    componentDidMount() {
        this.loadAllUsers();
    }

    loadAllUsers() {
        this.setState({
            isLoading: true
        });
        getAllUsers()
            .then(response => {
                this.setState({
                    isLoading: false,
                    users: response
                });
            }).catch(error => {
                this.setState({
                    isLoading: false,
                    users: []
                });
            });
    }

    saveFormRef = formRef => {
        this.formRef = formRef;
    };

    render() {
        const selectedUser = this.state.selectedUser

        const CollectionCreateForm = Form.create(
            {
                name: 'form_in_modal',
                mapPropsToFields(props) {
                    if (selectedUser) {
                        return {
                            name: Form.createFormField({
                                ...props.name,
                                value: selectedUser.name
                            }),
                            username: Form.createFormField({
                                ...props.username,
                                value: selectedUser.username
                            }),
                            email: Form.createFormField({
                                ...props.email,
                                value: selectedUser.email
                            }),
                            phoneNumber: Form.createFormField({
                                ...props.phoneNumber,
                                value: selectedUser.phoneNumber
                            }),
                            organization: Form.createFormField({
                                ...props.organization,
                                value: selectedUser.organization
                            }),
                            designation: Form.createFormField({
                                ...props.designation,
                                value: selectedUser.designation
                            }),
                            isAdmin: Form.createFormField({
                                ...props.isAdmin,
                                value: selectedUser.admin
                            }),
                        };
                    } else {
                        return {};
                    }
                },
            }
        )(NewUser)

        return (
            <div>
                <div className="user-home-container">
                    <PageHeader className="user-list-page-title" title="Manage Users" onBack={this.handleManagerUsersClick} extra={this.renderNavigationButtons()} />
                    <Table rowKey={record => record.id} columns={this.state.columns} dataSource={this.state.users} />
                </div>
                <CollectionCreateForm
                    wrappedComponentRef={this.saveFormRef}
                    visible={this.state.userModalVisible}
                    onCancel={this.handleAddUserCancel}
                    onCreate={this.handleAddUserSubmit}
                    isEdit={this.state.selectedUser != null}
                    id={this.state.selectedUser ? this.state.selectedUser.id : null}
                />
            </div>
        );
    }

    renderNavigationButtons() {
        return (
            [
                <Button key="1" onClick={this.handleAddNewUserClick}>Add New User</Button>,
            ]
        )
    }

    handleDelete(id) {
        this.setState({
            isLoading: true
        });

        deleteUser(id)
            .then(response => {
                notification.success({
                    message: 'rankCare',
                    description: "User deleted successfully!",
                });
                this.loadAllUsers();
            }).catch(error => {
                this.setState({ isLoading: false });
                notification.error({
                    message: 'rankCare',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            });
    }

    handleUserClicked(user) {
        this.setState({
            userModalVisible: true,
            selectedUser: user
        });
    }

    handleAddNewUserClick() {
        this.setState({
            userModalVisible: true,
            selectedUser: null
        });
    }

    handleManagerUsersClick() {
        this.props.history.goBack()
    }

    handleAddUserSubmit() {
        this.setState({
            userModalVisible: false,
            selectedUser: null
        });

        this.loadAllUsers()
    }

    handleAddUserCancel() {
        this.setState({
            userModalVisible: false,
            selectedUser: null
        });
    }
}

export default UserList;