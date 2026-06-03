package com.buildme.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "opportunities")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    private String company;
    private String location;
    private String salary;
    private String url;
    private String skills;

    @Enumerated(EnumType.STRING)
    private OpportunityType type;

    private boolean seen;
    private String appliedAt;
    private int matchScore;

    @Column(nullable = false)
    private LocalDateTime fetchedAt;

    public enum OpportunityType {
        JOB, SCHOLARSHIP, FELLOWSHIP, OPEN_SOURCE
    }
}

