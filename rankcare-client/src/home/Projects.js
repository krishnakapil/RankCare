import React, { Component } from 'react';
import { PageHeader, Empty, Button, Input, Icon, Form, notification, Popconfirm, Table } from 'antd';
import { getProjects, deleteProject, searchProjects } from '../util/APIUtils';
import NewProject from './NewProject';
import './Home.css';
import { makeStyles } from '@material-ui/core/styles';

const { Search } = Input;

class Projects extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentUser: props.currentUser,
            isAdmin: props.currentUser.isAdmin,
            isLoading: false,
            modalVisible: false,
            projects: [],
            selectedProject: null,
            currentPage: 0,
            pageSize: 20,
            totalRecords: 0,
            columns: [
                {
                    title: 'Project Name',
                    dataIndex: 'projectName',
                    key: 'projectName',
                    render: (text, record) => <a className="user-list-name-link" onClick={() => this.handleProjectClicked(record.id)}>{text}</a>,
                    sorter: (a, b) => a.chemicalName.length - b.chemicalName.length,
                    sortDirections: ['descend', 'ascend'],
                },
                {
                    title: 'Action',
                    dataIndex: 'id',
                    key: 'id',
                    render: (text, record) =>
                        <span>
                            <a style={{ marginRight: 50 }} onClick={() => this.handleEditClicked(record)}>
                                <Icon type="edit" className="nav-icon" />
                            </a>

                            <Popconfirm title="Sure to delete?" onConfirm={() => this.handleDelete(record.id)}>
                                <a className="user-list-delete-link">
                                    <Icon type="delete" className="nav-icon" />
                                </a>
                            </Popconfirm>
                        </span>,
                },
            ]
        }

        this.renderProjects = this.renderProjects.bind(this);
        this.handleBackClick = this.handleBackClick.bind(this)
        this.handleAddNewProjectClick = this.handleAddNewProjectClick.bind(this)
        this.handleDelete = this.handleDelete.bind(this)
        this.handleAddProjectSubmit = this.handleAddProjectSubmit.bind(this)
        this.handleAddProjectCancel = this.handleAddProjectCancel.bind(this)
        this.handleProjectClicked = this.handleProjectClicked.bind(this);
        this.handleEditClicked = this.handleEditClicked.bind(this);
        this.searchProjects = this.searchProjects.bind(this);
        this.onPageChanged = this.onPageChanged.bind(this);
    }

    componentDidMount() {
        this.loadData(0);
    }

    loadData(page) {
        this.setState({
            isLoading: true
        });
        getProjects(page, this.state.pageSize)
            .then(response => {
                this.setState({
                    isLoading: false,
                    projects: response.data,
                    selectedProject: null,
                    totalRecords: (response.pageCnt * this.state.pageSize)
                });
            }).catch(error => {
                this.setState({
                    isLoading: false,
                    projects: [],
                    selectedProject: null,
                    totalRecords: 0
                });
            });
    }

    searchProjects(query) {
        if (!query || query.trim() == "") {
            this.loadData(0)
        } else {
            this.setState({
                isLoading: true,
                totalRecords: 0,
                pageCnt: 0,
                projects: []
            });
            searchProjects(query)
                .then(response => {
                    this.setState({
                        isLoading: false,
                        projects: response,
                        selectedProject: null
                    });
                }).catch(error => {
                    this.setState({
                        isLoading: false,
                        projects: [],
                        selectedProject: null
                    });
                });
        }
    }

    onPageChanged(pagination, filters, sorter) {
        this.loadData(pagination.current - 1)
    }

    saveFormRef = formRef => {
        this.formRef = formRef;
    };

    render() {
        const selectedProject = this.state.selectedProject;

        const CollectionCreateForm = Form.create(
            {
                name: 'form_in_modal',
                mapPropsToFields(props) {
                    if (selectedProject) {
                        return {
                            projectName: Form.createFormField({
                                ...props.projectName,
                                value: selectedProject.projectName
                            }),
                        };
                    } else {
                        return {};
                    }
                },
            }
        )(NewProject)

        return (
            <div>
                <div className="user-home-container">
                    <PageHeader className="user-list-page-title" title="Projects" onBack={this.handleBackClick} extra={this.renderNavigationButtons()} />
                    <div className="search-box">
                        <Input
                            placeholder="Search Projects"
                            onChange={e => this.searchProjects(e.target.value)}
                            style={{ width: 400 }}
                        />
                    </div>
                </div>
                {this.renderProjects()}
                <CollectionCreateForm
                    wrappedComponentRef={this.saveFormRef}
                    visible={this.state.modalVisible}
                    onCancel={this.handleAddProjectCancel}
                    onCreate={this.handleAddProjectSubmit}
                    isEdit={this.state.selectedProject != null}
                    id={this.state.selectedProject ? this.state.selectedProject.id : null}
                />
            </div >
        );
    }

    renderProjects() {
        if (this.state.projects.length > 0) {
            const classes = makeStyles(theme => ({
                root: {
                    display: 'flex',
                    flexWrap: 'wrap',
                    justifyContent: 'space-around',
                    overflow: 'hidden',
                    backgroundColor: theme.palette.background.paper,
                },
                gridList: {
                    width: 500,
                    height: 450,
                },
                icon: {
                    color: 'rgba(255, 255, 255, 1)',
                },
            }));

            return (
                <div className={classes.root}>
                    <Table
                        rowKey={record => record.id}
                        columns={this.state.columns}
                        dataSource={this.state.projects}
                        onChange={this.onPageChanged}
                        pagination={{ total: this.state.totalRecords, defaultPageSize: this.state.pageSize, current: this.state.currentPage }}
                    />
                </div>
            )
        } else {
            return (<Empty />)
        }
    }

    renderNavigationButtons() {
        return (
            [
                <Button key="1" onClick={this.handleAddNewProjectClick}>Add Project</Button>,
            ]
        )
    }

    handleEditClicked(project) {
        this.setState({
            modalVisible: true,
            selectedProject: project
        });
    }

    handleDelete(id) {
        this.setState({
            isLoading: true
        });

        deleteProject(id)
            .then(response => {
                notification.success({
                    message: 'rankCare',
                    description: "Project deleted successfully!",
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

    handleAddNewProjectClick() {
        this.setState({
            modalVisible: true,
            selectedSite: null
        });
    }

    handleBackClick() {
        this.props.history.goBack()
    }

    handleAddProjectSubmit() {
        this.setState({
            modalVisible: false,
            selectedSite: null
        });

        this.loadData(this.state.currentPage)
    }

    handleAddProjectCancel() {
        this.setState({
            modalVisible: false,
            selectedSite: null
        });
    }

    handleProjectClicked(id) {
        this.props.history.push("/project?id=" + id);
    }
}

export default Projects;