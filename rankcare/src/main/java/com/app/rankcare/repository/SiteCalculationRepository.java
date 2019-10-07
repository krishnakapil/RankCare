package com.app.rankcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.rankcare.model.SiteCalculation;

@Repository
public interface SiteCalculationRepository extends JpaRepository<SiteCalculation, Long> {

	public List<SiteCalculation> findBySiteId(Long siteId);
   
  
    
}