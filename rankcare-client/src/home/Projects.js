import React, { Component } from 'react';
import { PageHeader, Empty, Button, Input, Icon, Form, notification, Popconfirm, Card } from 'antd';
import { getProjects, deleteProject } from '../util/APIUtils';
import NewProject from './NewProject';
import './Home.css';
import { makeStyles } from '@material-ui/core/styles';
import GridList from '@material-ui/core/GridList';
import GridListTile from '@material-ui/core/GridListTile';

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
            selectedProject: null
        }

        this.renderProjects = this.renderProjects.bind(this);
        this.handleBackClick = this.handleBackClick.bind(this)
        this.handleAddNewProjectClick = this.handleAddNewProjectClick.bind(this)
        this.handleDelete = this.handleDelete.bind(this)
        this.handleAddProjectSubmit = this.handleAddProjectSubmit.bind(this)
        this.handleAddProjectCancel = this.handleAddProjectCancel.bind(this)
        this.handleProjectClicked = this.handleProjectClicked.bind(this);
        this.handleEditClicked = this.handleEditClicked.bind(this);
    }

    componentDidMount() {
        this.loadData();
    }

    loadData() {
        this.setState({
            isLoading: true
        });
        getProjects()
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
                    {this.renderProjects()}
                </div>
                <CollectionCreateForm
                    wrappedComponentRef={this.saveFormRef}
                    visible={this.state.modalVisible}
                    onCancel={this.handleAddProjectCancel}
                    onCreate={this.handleAddProjectSubmit}
                    isEdit={this.state.selectedProject != null}
                    id={this.state.selectedProject ? this.state.selectedProject.id : null}
                />
            </div>
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
            const projects = this.state.projects;

            return (
                <div>
                    <div className="search-box">
                        <Search
                            placeholder="Search Projects"
                            onSearch={value => console.log(value)}
                            style={{ width: 400 }}
                            enterButton
                        />
                    </div>
                    <div className={classes.root}>
                        <GridList cellHeight={120} cols={4} className={classes.gridList}>
                            {projects.map(project => (
                                <GridListTile key={project.id}>
                                    <div className="project-box">
                                        <a class="fill-div" onClick={() => this.handleProjectClicked(project.id)}>
                                            <h4 style={{ fontWeight: 400 }}>{project.projectName}</h4>
                                        </a>
                                        <span style={{ float: "right" }}>
                                            <a style={{ marginRight: 20 }} onClick={() => this.handleEditClicked(project)}>
                                                <Icon type="edit" />
                                            </a>
                                            <Popconfirm title="Sure to delete?" onConfirm={() => this.handleDelete(project.id)}>
                                                <a className="user-list-delete-link">
                                                    <Icon type="delete" />
                                                </a>
                                            </Popconfirm>

                                        </span>
                                    </div>
                                </GridListTile>
                            ))}
                        </GridList>
                    </div>
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