package com.buildme.repository;

import com.buildme.entity.OpenSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpenSourceRepository extends JpaRepository<OpenSource, String> {
    @Query("SELECT o FROM OpenSource o WHERE o.seen = :seen ORDER BY o.fetchedAt DESC")
    List<OpenSource> findBySeen(boolean seen);

    @Query("SELECT o FROM OpenSource o ORDER BY o.fetchedAt DESC")
    List<OpenSource> findAll();
}

