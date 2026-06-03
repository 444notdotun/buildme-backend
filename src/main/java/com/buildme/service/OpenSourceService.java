package com.buildme.service;

import com.buildme.entity.OpenSource;
import com.buildme.repository.OpenSourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * OpenSource Service - Business logic for open source opportunities
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OpenSourceService {

    private final OpenSourceRepository openSourceRepository;

    /**
     * Get open source items by seen status
     */
    public List<OpenSource> getBySeen(boolean seen) {
        return openSourceRepository.findBySeen(seen);
    }

    /**
     * Mark as seen
     */
    public void markAsSeen(String id) {
        OpenSource item = openSourceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("OpenSource item not found"));
        item.setSeen(true);
        openSourceRepository.save(item);
    }

    /**
     * Save open source item
     */
    public OpenSource save(OpenSource item) {
        return openSourceRepository.save(item);
    }

    /**
     * Get all
     */
    public List<OpenSource> getAll() {
        return openSourceRepository.findAll();
    }
}

