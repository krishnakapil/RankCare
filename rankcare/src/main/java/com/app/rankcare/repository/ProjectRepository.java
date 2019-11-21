package com.app.rankcare.repository;

import com.app.rankcare.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    //@Query("SELECT u FROM Project u WHERE u.createdByUser = ?1 order by u.projectName")
    @Query(value = "SELECT * FROM project_data WHERE created_by = ?1 ORDER BY ?#{#pageable}",
            countQuery = "SELECT count(*) FROM project_data WHERE created_by = ?1",
            nativeQuery = true)
    Page<Project> findUserCreatedProjects(Long userId, Pageable pageable);

    @Query("SELECT u FROM Project u WHERE projectName LIKE ?1% order by u.projectName")
    List<Project> searchProjectsAdmin(String query);

    @Query("SELECT u FROM Project u WHERE u.createdByUser = ?1 AND projectName LIKE ?2% order by u.projectName")
    List<Project> searchProjects(Long userId, String query);
}
