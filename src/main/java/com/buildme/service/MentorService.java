package com.buildme.service;

import com.buildme.entity.MentorMessage;
import com.buildme.repository.MentorMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mentor Service - Business logic for mentor chat and messaging
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MentorService {

    private final MentorMessageRepository mentorMessageRepository;
    private final ClaudeService claudeService;
    private final UserProfileService userProfileService;
    private final ProgressService progressService;

    /**
     * Chat with mentor
     */
    public String chat(String question, List<Map<String, String>> history) {
        String level = userProfileService.getUserLevel();
        List<String> concepts = progressService.getRecentConcepts();

        String reply = claudeService.mentorChat(
            question, level,
            "Java, Spring Boot, Spring Security, PostgreSQL, Docker",
            concepts, history
        );

        // Save messages
        saveMentorMessage("USER", question);
        saveMentorMessage("ASSISTANT", reply);

        return reply;
    }

    /**
     * Get mentor message history
     */
    public List<MentorMessage> getHistory(int limit) {
        return mentorMessageRepository.findRecent(limit);
    }

    /**
     * Save mentor message
     */
    private void saveMentorMessage(String role, String content) {
        try {
            MentorMessage message = MentorMessage.builder()
                .role(MentorMessage.MessageRole.valueOf(role))
                .content(content)
                .sentAt(LocalDateTime.now())
                .build();
            mentorMessageRepository.save(message);
        } catch (Exception e) {
            log.warn("Failed to save mentor message: {}", e.getMessage());
        }
    }
}

