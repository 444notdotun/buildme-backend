package com.buildme.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "dsa_entries")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsaEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String problem;

    private String difficulty;
    private String timeComplexity;
    private String spaceComplexity;
    private String pattern;
    private int attempts;

    @Column(nullable = false)
    private LocalDate solvedAt;

    private LocalDateTime loggedAt;
}

