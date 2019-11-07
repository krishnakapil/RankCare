package com.app.rankcare.repository;

import com.app.rankcare.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT u FROM Project u WHERE u.createdByUser = ?1 order by u.projectName")
    List<Project> findUserCreatedProjects(Long userId);
}
