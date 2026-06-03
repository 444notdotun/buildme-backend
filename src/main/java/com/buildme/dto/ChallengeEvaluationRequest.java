package com.buildme.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeEvaluationRequest {
    private String question;
    private String answer;
    private String type;
}

