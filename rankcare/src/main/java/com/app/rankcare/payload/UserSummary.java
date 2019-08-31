package com.app.rankcare.payload;

public class UserSummary {
    private Long id;
    private String username;
    private String name;
    private Boolean isAdmin;

    public UserSummary(Long id, String username, String name, Boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.isAdmin = isAdmin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}