package com.buildme.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "search_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String searchType;
    private int resultsFound;
    private int newResults;
    private boolean success;
    private String errorMessage;
    private LocalDateTime searchedAt;
}

