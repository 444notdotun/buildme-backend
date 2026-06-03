package com.buildme.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Claude AI Service - Handles all Claude API interactions
 * Follows Single Responsibility Principle - only responsible for Claude integration
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ClaudeService {

    @Value("${app.claude.api-key}")
    private String apiKey;

    @Value("${app.claude.model}")
    private String model;

    @Value("${app.claude.max-tokens}")
    private int maxTokens;

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private static final String CLAUDE_URL = "https://api.anthropic.com/v1/messages";

    // -------------------------------------------------------
    // Core call method
    // -------------------------------------------------------
    public String call(String prompt, String system) {
        try {
            ObjectNode body = mapper.createObjectNode();
            body.put("model", model);
            body.put("max_tokens", maxTokens);
            if (system != null && !system.isBlank()) body.put("system", system);

            ArrayNode messages = body.putArray("messages");
            ObjectNode msg = messages.addObject();
            msg.put("role", "user");
            msg.put("content", prompt);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CLAUDE_URL))
                .header("Content-Type", "application/json")
                .header("x-api-key", apiKey)
                .header("anthropic-version", "2023-06-01")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode responseJson = mapper.readTree(response.body());
            return responseJson.path("content").get(0).path("text").asText();

        } catch (Exception e) {
            log.error("Claude API call failed: {}", e.getMessage());
            return null;
        }
    }

    // -------------------------------------------------------
    // Call with web search tool
    // -------------------------------------------------------
    public String callWithSearch(String prompt, String system) {
        try {
            ObjectNode body = mapper.createObjectNode();
            body.put("model", model);
            body.put("max_tokens", maxTokens);
            if (system != null && !system.isBlank()) body.put("system", system);

            ArrayNode tools = body.putArray("tools");
            ObjectNode searchTool = tools.addObject();
            searchTool.put("type", "web_search_20250305");
            searchTool.put("name", "web_search");

            ArrayNode messages = body.putArray("messages");
            ObjectNode msg = messages.addObject();
            msg.put("role", "user");
            msg.put("content", prompt);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CLAUDE_URL))
                .header("Content-Type", "application/json")
                .header("x-api-key", apiKey)
                .header("anthropic-version", "2023-06-01")
                .header("anthropic-beta", "web-search-2025-03-05")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode responseJson = mapper.readTree(response.body());

            // Extract all text blocks
            StringBuilder text = new StringBuilder();
            for (JsonNode block : responseJson.path("content")) {
                if ("text".equals(block.path("type").asText())) {
                    text.append(block.path("text").asText()).append("\n");
                }
            }
            return text.toString().trim();

        } catch (Exception e) {
            log.error("Claude search call failed: {}", e.getMessage());
            return null;
        }
    }

    // -------------------------------------------------------
    // Multi-turn mentor conversation
    // -------------------------------------------------------
    public String mentorChat(String question, String userLevel, String userStack,
                              List<String> recentConcepts, List<Map<String, String>> history) {
        String system = String.format("""
            You are the personal engineering mentor for Adedotun (Dotman), a %s Java/Spring Boot backend engineer from Lagos, Nigeria.
            Stack: %s
            Recent concepts: %s
            Today: %s

            Rules:
            - Never give the full answer first. Make them think.
            - Push back on surface-level answers.
            - Connect concepts to real projects they've built (Insighta Labs, Cinema Booking, Ghost Coach, Wallet system).
            - End every response with a follow-up question.
            - Be direct, warm, and honest. No filler.
            - When they show good thinking, call it out specifically.
            """,
            userLevel,
            userStack,
            String.join(", ", recentConcepts),
            LocalDate.now().toString()
        );

        try {
            ObjectNode body = mapper.createObjectNode();
            body.put("model", model);
            body.put("max_tokens", maxTokens);
            body.put("system", system);

            ArrayNode messages = body.putArray("messages");

            // Add conversation history
            for (Map<String, String> m : history) {
                ObjectNode msg = messages.addObject();
                msg.put("role", m.get("role"));
                msg.put("content", m.get("content"));
            }

            // Add current question
            ObjectNode current = messages.addObject();
            current.put("role", "user");
            current.put("content", question);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CLAUDE_URL))
                .header("Content-Type", "application/json")
                .header("x-api-key", apiKey)
                .header("anthropic-version", "2023-06-01")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode responseJson = mapper.readTree(response.body());
            return responseJson.path("content").get(0).path("text").asText();

        } catch (Exception e) {
            log.error("Mentor chat failed: {}", e.getMessage());
            return "I couldn't process that right now. Try again.";
        }
    }

    // -------------------------------------------------------
    // Parse JSON from Claude response
    // -------------------------------------------------------
    public JsonNode parseJson(String response) {
        if (response == null) return null;
        try {
            String clean = response
                .replaceAll("```json\\n?", "")
                .replaceAll("```\\n?", "")
                .trim();
            // Find first { or [
            int start = Math.min(
                clean.indexOf('{') == -1 ? Integer.MAX_VALUE : clean.indexOf('{'),
                clean.indexOf('[') == -1 ? Integer.MAX_VALUE : clean.indexOf('[')
            );
            if (start == Integer.MAX_VALUE) return null;
            clean = clean.substring(start);
            return mapper.readTree(clean);
        } catch (Exception e) {
            log.warn("Failed to parse JSON from Claude response: {}", e.getMessage());
            return null;
        }
    }

    // -------------------------------------------------------
    // Generate daily plan
    // -------------------------------------------------------
    public Map<String, Object> generateDailyPlan(String level, String energy, int minutes) {
        String today = LocalDate.now().toString();
        String dayOfWeek = LocalDate.now().getDayOfWeek().toString();

        String prompt = String.format("""
            You are BuildMe, personal mentor for Adedotun (Dotman), %s Java/Spring Boot engineer, Lagos Nigeria.
            Today: %s (%s). Energy: %s. Available: %d minutes.

            Open loops: Ghost Coach assignment pending, Wallet system not started, HNG Stage 3 resubmission needed.

            Generate a warm, focused daily plan. Return ONLY valid JSON:
            {"greeting":"warm 1-line greeting","focus":"one main focus today (max 8 words)","tasks":[{"title":"task","minutes":30,"category":"DSA|CONCEPT|BUILD|READ|WRITE"}],"motivation":"one honest sentence","tip":"one practical tip"}
            Max %d tasks.
            """,
            level, today, dayOfWeek, energy, minutes, Math.max(2, minutes / 30)
        );

        JsonNode result = parseJson(call(prompt, null));
        if (result == null) return defaultPlan(energy, minutes);

        Map<String, Object> plan = new HashMap<>();
        plan.put("greeting", result.path("greeting").asText("Good day, Dotman!"));
        plan.put("focus", result.path("focus").asText("Build something today"));
        plan.put("motivation", result.path("motivation").asText("Keep building."));
        plan.put("tip", result.path("tip").asText(""));
        plan.put("tasks", result.path("tasks"));
        plan.put("generatedAt", System.currentTimeMillis());
        plan.put("date", today);
        plan.put("dayOfWeek", dayOfWeek);
        return plan;
    }

    private Map<String, Object> defaultPlan(String energy, int minutes) {
        return Map.of(
            "greeting", "Good day, Dotman! Let's build.",
            "focus", "One step forward today",
            "motivation", "Every line of code is progress.",
            "tip", "Bad path before happy path. Always.",
            "tasks", List.of(
                Map.of("title", "Solve one DSA problem", "minutes", 45, "category", "DSA"),
                Map.of("title", "Review yesterday's code", "minutes", 30, "category", "BUILD")
            ),
            "date", LocalDate.now().toString()
        );
    }

    // -------------------------------------------------------
    // Generate weekly challenge
    // -------------------------------------------------------
    public String generateWeeklyChallenge(String level, List<String> recentConcepts) {
        String prompt = String.format("""
            Generate 3 challenge questions for Adedotun, %s Java engineer.
            Recently studied: %s.
            Today: %s.

            Return ONLY valid JSON:
            {
              "concept":{"q":"specific technical question","hint":"short hint","depth":"complete answer looks like"},
              "tradeoff":{"q":"you have [scenario]. Compare [A] vs [B]. What do you choose and why?","hint":"think about performance, consistency, complexity","depth":"expert reasoning"},
              "intellect":{"q":"why does [fundamental] work this way? What breaks if it worked differently?","hint":"first principles","depth":"systems-level thinking"}
            }
            """,
            level,
            String.join(", ", recentConcepts),
            LocalDate.now().toString()
        );
        return call(prompt, null);
    }

    // -------------------------------------------------------
    // Evaluate challenge answer
    // -------------------------------------------------------
    public String evaluateAnswer(String question, String answer, String type) {
        String prompt = String.format("""
            Evaluate this %s answer strictly but fairly.
            Question: %s
            Answer: %s

            Return ONLY valid JSON:
            {"score":7,"verdict":"STRONG|PARTIAL|SURFACE","good":"what was done well","missing":"what was missing","pushback":"harder follow-up question","model":"complete answer in 3 sentences"}
            """,
            type, question, answer
        );
        return call(prompt, null);
    }

    // -------------------------------------------------------
    // Interest of the week
    // -------------------------------------------------------
    public String generateInterestOfWeek() {
        String[] topics = {
            "why Rust outperforms C++ at memory safety without a garbage collector",
            "how CRISPR gene editing is converging with software pipelines and bioinformatics",
            "quantum computing timelines and what it breaks in current cryptography",
            "how AlphaFold changed biology the way chess engines changed chess",
            "why TCP/IP was designed with these fundamental tradeoffs and what the internet sacrificed",
            "how GPS accuracy matters for financial systems and what happens when it drifts",
            "why Go was created and what Java problems it solved and didn't solve",
            "how the brain processes information vs how transformers do it — key differences"
        };
        String topic = topics[new Random().nextInt(topics.length)];

        String prompt = String.format("""
            Research and explain this topic for a software engineer who cares about fintech, security, and distributed systems:
            "%s"

            Today: %s.

            Return ONLY valid JSON:
            {"topic":"%s","why":"2 sentences why every engineer should know this","career":"how this connects to backend/security career","world":"how this is changing the world right now","future":"what shifts this creates in next 5-10 years","analogy":"one brilliant analogy that makes this click","question":"a directed question connecting this to Java/fintech/security work","source":"best resource to learn more"}
            """,
            topic, LocalDate.now().toString(), topic
        );
        return callWithSearch(prompt, null);
    }

    // -------------------------------------------------------
    // Search jobs
    // -------------------------------------------------------
    public String searchJobs() {
        return callWithSearch(String.format("""
            Find current Java Spring Boot backend engineer job openings as of %s.
            Include: Lagos fintech (Moniepoint, Renmoney, Flutterwave, Kuda, PiggyVest, Paystack, Quidax) AND global remote roles for Nigerian engineers.
            Return ONLY valid JSON array (max 8):
            [{"title":"...","company":"...","location":"Lagos|Remote|Hybrid","salary":"...","url":"...","skills":["Java","Spring Boot"],"score":85}]
            """, LocalDate.now().toString()), null);
    }

    // -------------------------------------------------------
    // Search scholarships
    // -------------------------------------------------------
    public String searchScholarships() {
        return callWithSearch(String.format("""
            Find scholarship and fellowship applications currently open or opening soon for Nigerian software engineers for Masters degrees abroad as of %s.
            Focus on: Chevening, MasterCard Foundation, DAAD, Commonwealth, Ireland Fellows, Erasmus, MEXT, Australia Awards, Korea GKS.
            Return ONLY valid JSON array (max 8):
            [{"name":"...","country":"...","deadline":"...","status":"OPEN|OPENING_SOON|CLOSED","url":"...","type":"Masters|PhD|Fellowship"}]
            """, LocalDate.now().toString()), null);
    }

    // -------------------------------------------------------
    // Search open source
    // -------------------------------------------------------
    public String searchOpenSource() {
        return callWithSearch(String.format("""
            Find open source opportunities for Java/Spring Boot engineers as of %s:
            1. GitHub repos actively seeking contributors (good-first-issue, Java, Spring Boot, fintech)
            2. Paid bounties on Algora.io or IssueHunt for backend/Java issues
            Return ONLY valid JSON array (max 6):
            [{"name":"...","type":"BUILD|PAID","url":"...","desc":"...","bounty":"$50 or null","lang":"Java","level":"beginner|intermediate"}]
            """, LocalDate.now().toString()), null);
    }

    // -------------------------------------------------------
    // Generate session commit
    // -------------------------------------------------------
    public String generateSessionCommit(String level, String phase,
                                         String recentProgress, int dsaStreak,
                                         int cheveningHours, List<String> openLoops) {
        String prompt = String.format("""
            Generate a session commit summary for Adedotun (Dotman).
            Level: %s. Phase: %s.
            Recent progress: %s.
            DSA streak: %d days.
            Chevening hours: %d/2800.
            Open loops: %s.
            Today: %s.

            Return ONLY valid JSON:
            {"summary":"3-sentence honest summary of where they are","loops":["open loop 1","open loop 2","open loop 3"],"next":["step 1","step 2","step 3"],"prompt":"# SESSION COMMIT — Adedotun (Dotman)\\n# %s\\n\\n## PROFILE\\nLevel: %s → Senior (12 months target)\\nPhase: %s\\nStack: Java, Spring Boot, Spring Security, PostgreSQL, Docker, React\\n\\n## RECENT PROGRESS\\n%s\\n\\n## DSA\\nStreak: %d days\\n\\n## CHEVENING HOURS\\n%d / 2800\\n\\n## OPEN LOOPS\\n%s\\n\\n## NEXT STEPS\\n[add from next array]"}
            """,
            level, phase, recentProgress, dsaStreak, cheveningHours,
            String.join(", ", openLoops),
            LocalDate.now().toString(),
            LocalDate.now().toString(),
            level, phase, recentProgress, dsaStreak, cheveningHours,
            String.join("\\n", openLoops.stream().map(l -> "- " + l).toList())
        );
        return call(prompt, null);
    }
}

