package com.app.rankcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.rankcare.model.Toxicity;
@Repository
public interface ToxicityRepository extends JpaRepository<Toxicity, Long> {

}
