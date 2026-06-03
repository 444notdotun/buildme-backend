package com.buildme.repository;

import com.buildme.entity.MentorMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentorMessageRepository extends JpaRepository<MentorMessage, String> {
    @Query("SELECT m FROM MentorMessage m ORDER BY m.sentAt DESC LIMIT ?1")
    List<MentorMessage> findRecent(int limit);
}

