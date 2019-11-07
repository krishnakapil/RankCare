package com.app.rankcare.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ProjectRequest {
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String projectName;

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
}
