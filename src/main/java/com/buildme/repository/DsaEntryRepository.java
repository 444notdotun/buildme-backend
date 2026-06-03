package com.buildme.repository;

import com.buildme.entity.DsaEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DsaEntryRepository extends JpaRepository<DsaEntry, String> {

    long countBySolvedAtGreaterThanEqual(LocalDate date);

    long count();

    List<DsaEntry> findTop10ByOrderByLoggedAtDesc();
}