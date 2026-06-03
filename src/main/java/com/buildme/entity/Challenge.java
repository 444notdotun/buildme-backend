package com.buildme.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "challenges")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChallengeType type;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String questionsJson;

    private String weekKey;
    private String monthKey;
    private String eraKey;

    @Column(columnDefinition = "TEXT")
    private String answersJson;

    @Column(columnDefinition = "TEXT")
    private String evaluationsJson;

    private boolean completed;
    private LocalDateTime generatedAt;
    private LocalDateTime completedAt;

    public enum ChallengeType {
        WEEKLY, MONTHLY, ERA_REVIEW, INTEREST_OF_WEEK
    }
}

