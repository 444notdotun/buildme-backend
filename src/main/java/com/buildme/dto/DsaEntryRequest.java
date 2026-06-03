package com.buildme.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsaEntryRequest {
    private String problem;
    private String difficulty;
    private String timeComplexity;
    private String spaceComplexity;
    private String pattern;
}

