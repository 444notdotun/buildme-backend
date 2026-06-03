package com.buildme.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "progress_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private LocalDate date;

    private String energyLevel;
    private int availableMinutes;

    @Column(columnDefinition = "TEXT")
    private String shipped;

    @Column(columnDefinition = "TEXT")
    private String planFocus;

    @Column(columnDefinition = "TEXT")
    private String reflectionNote;

    private boolean cheveningDayCounted;
    private LocalDateTime loggedAt;
}

