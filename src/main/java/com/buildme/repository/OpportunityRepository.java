package com.buildme.repository;

import com.buildme.entity.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, String> {
    List<Opportunity> findBySeenOrderByFetchedAtDesc(boolean seen);
    List<Opportunity> findBySeenAndTypeOrderByFetchedAtDesc(boolean seen, Opportunity.OpportunityType type);
    List<Opportunity> findAllByOrderByFetchedAtDesc();
}