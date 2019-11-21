package com.app.rankcare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.rankcare.model.Site;

@Repository
public interface SiteDataRepository extends JpaRepository<Site, Long> {
    @Query(value = "SELECT * FROM site_data WHERE project_id = ?1 ORDER BY ?#{#pageable}",
            countQuery = "SELECT count(*) FROM site_data WHERE project_id = ?1",
            nativeQuery = true)
    Page<Site> findSitesByProject(Long projectId, Pageable pageable);
}