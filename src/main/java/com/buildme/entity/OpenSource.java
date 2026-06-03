package com.buildme.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "open_source")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenSource {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String url;
    private String description;
    private String bounty;
    private String language;
    private String level;

    @Enumerated(EnumType.STRING)
    private OSType type;

    private boolean seen;
    private boolean contributed;
    private LocalDateTime fetchedAt;

    public enum OSType {
        BUILD, PAID
    }
}

