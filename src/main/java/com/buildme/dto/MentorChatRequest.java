package com.buildme.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorChatRequest {
    private String question;
    private List<Map<String, String>> history;
}

