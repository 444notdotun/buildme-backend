package com.buildme.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressLogRequest {
    private String shipped;
    private String energy;
    private int minutes;
    private String concept;
}

