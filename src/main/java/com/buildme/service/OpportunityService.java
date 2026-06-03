package com.buildme.service;

import com.buildme.entity.Opportunity;
import com.buildme.repository.OpportunityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OpportunityService {

    private final OpportunityRepository opportunityRepository;

    public List<Opportunity> getBySeenStatus(boolean seen) {
        return opportunityRepository.findBySeenOrderByFetchedAtDesc(seen);
    }

    public List<Opportunity> getBySeenAndType(boolean seen, Opportunity.OpportunityType type) {
        return opportunityRepository.findBySeenAndTypeOrderByFetchedAtDesc(seen, type);
    }

    public void markAsSeen(String id) {
        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));
        opportunity.setSeen(true);
        opportunityRepository.save(opportunity);
    }


    public List<Opportunity> getAll() {
        return opportunityRepository.findAllByOrderByFetchedAtDesc();
    }
}