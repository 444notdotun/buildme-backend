package com.buildme.repository;

import com.buildme.entity.ProgressLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProgressLogRepository extends JpaRepository<ProgressLog, String> {

    List<ProgressLog> findByDateOrderByLoggedAtDesc(LocalDate date);

    List<ProgressLog> findTop5ByLoggedAtGreaterThanEqualOrderByLoggedAtDesc(LocalDateTime cutoff);

    List<ProgressLog> findTop10ByShippedIsNotNull();

    List<ProgressLog> findTopByOrderByLoggedAtDesc();
}