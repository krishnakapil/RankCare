import React, { Component } from 'react';
import { PageHeader, Table, Tag, Button, Popconfirm, Icon, Modal } from 'antd';
import './UserList.css';
import { getAllUsers } from '../util/APIUtils';
import { ACCESS_TOKEN } from '../constants';
import NewUser from '../user/signup/NewUser'

class UserList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentUser: props.currentUser,
            isAuthenticated: localStorage.getItem(ACCESS_TOKEN) != null,
            isLoading: false,
            userModalVisible: false,
            users: [],
            columns: [
                {
                    title: 'Name',
                    dataIndex: 'name',
                    key: 'name',
                    render: (text, record) => <a className="user-list-name-link" onClick={this.handleUserClicked(record.id)}>{text}</a>,
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

    render() {
        return (
            <div>
                <div className="user-home-container">
                    <PageHeader className="user-list-page-title" title="Users List" onBack={this.handleManagerUsersClick} extra={this.renderNavigationButtons()} />
                    <Table columns={this.state.columns} dataSource={this.state.users} />
                </div>
                <Modal
                    title="Add new user"
                    visible={this.state.userModalVisible}
                    onOk={this.handleAddUserSubmit}
                    onCancel={this.handleAddUserCancel}
                    footer={[
                        <Button key="back" onClick={this.handleAddUserCancel}>
                          Cancel
                        </Button>,
                        <Button key="submit" type="primary" loading={this.state.isLoading} onClick={this.handleAddUserSubmit}>
                          Submit
                        </Button>,
                    ]}>
                    <NewUser />
                </Modal>
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

    }

    handleUserClicked(id) {

    }

    handleAddNewUserClick() {
        this.setState({
            userModalVisible: true,
        });
    }

    handleManagerUsersClick() {
        this.props.history.goBack()
    }

    handleAddUserSubmit() {
        this.setState({
            userModalVisible: false,
        });
    }

    handleAddUserCancel() {
        this.setState({
            userModalVisible: false,
        });
    }
}

export default UserList;