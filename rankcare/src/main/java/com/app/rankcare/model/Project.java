package com.app.rankcare.model;

import javax.persistence.*;

@Entity
@Table(name = "project_data")
public class Project {
    private static final long serialVersionUID = 7280440263317772534L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "created_by")
    private Long createdByUser;

    public Project() {

    }

    public Project(Long id, String projectName, Long createdByUser) {
        this.id = id;
        this.projectName = projectName;
        this.createdByUser = createdByUser;
    }

    public Project(String projectName, Long createdByUser) {
        this.projectName = projectName;
        this.createdByUser = createdByUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(Long createdByUser) {
        this.createdByUser = createdByUser;
    }
}
