package com.app.rankcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.rankcare.model.Site;

@Repository
public interface SiteDataRepository extends JpaRepository<Site, Long> {
   
    //Site findById(String siteId);

    
}