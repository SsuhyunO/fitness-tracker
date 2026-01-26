package com.example.analysis_service.service;

import com.example.analysis_service.dto.GeminiRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor // RestTemplate 주입을 위해 추가
public class GeminiService {

    @Value("${google.ai.api-key}")
    private String apiKey;

    private final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta";
    private final RestTemplate restTemplate; // 빈 주입 방식으로 변경
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private String cachedModel = "models/gemini-1.5-flash"; // 기본값 설정

    // 서버가 시작될 때 실행되어 모델 리스트를 한 번만 조회함
    @PostConstruct
    public void init() {
        try {
            this.cachedModel = getSelectedModel(apiKey.trim());
            log.info("Gemini Model Selected: {}", cachedModel);
        } catch (Exception e) {
            log.warn("기본 모델을 사용합니다: {}", cachedModel);
        }
    }

    public String getAnalysis(String userId, String workoutType, Integer durationMinutes, Integer age, Double height, Double weight) {
        String trimmedKey = apiKey.trim();

        try {
            String generateUrl = String.format("%s/%s:generateContent?key=%s", 
                                BASE_URL, cachedModel, trimmedKey);

            String prompt = String.format(
                "당신은 전문 피트니스 트레이너이자 영양사입니다.\n" +
                "사용자 정보: [나이: %d세, 키: %.1fcm, 몸무게: %.1fkg]\n" +
                "운동 정보: [%s]을/를 %d분 동안 수행했습니다.\n" +
                "지침:\n" +
                "1. 사용자의 신체 정보와 운동 종류, 시간을 바탕으로 '예상 소모 칼로리'를 계산해 알려주세요.\n" +
                "2. 운동 강도가 적절했는지 평가하고, 개선할 점과 추천 영양소를 한국어로 친절하게 조언하세요.\n" +
                "3. 전체 답변은 최대 세 문장을 넘기지 마세요.",
                age, height, weight, workoutType, durationMinutes
            );

            GeminiRequest request = new GeminiRequest(prompt);

            String jsonResponse = restTemplate.postForObject(generateUrl, request, String.class);
            
            Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, new TypeReference<Map<String, Object>>() {});
            String result = extractTextFromResponse(responseMap);

            if (result.startsWith("분석 결과를 추출")) {
                throw new RuntimeException(result);
            }

            return result;

        } catch (Exception e) {
            log.error("Gemini API 호출 중 상세 오류 발생: ", e);
            throw new RuntimeException("Gemini 분석 오류: " + e.getMessage());
        }
    }

    private String getSelectedModel(String apiKey) throws Exception {
        String listModelsUrl = BASE_URL + "/models?key=" + apiKey;
        String rawResponse = restTemplate.getForObject(listModelsUrl, String.class);
        Map<String, Object> response = objectMapper.readValue(rawResponse, new TypeReference<Map<String, Object>>() {});
        
        if (response != null && response.containsKey("models")) {
            List<Map<String, Object>> models = objectMapper.convertValue(response.get("models"), new TypeReference<List<Map<String, Object>>>() {});
            for (Map<String, Object> model : models) {
                String name = (String) model.get("name");
                List<String> methods = objectMapper.convertValue(model.get("supportedGenerationMethods"), new TypeReference<List<String>>() {});
                if (methods != null && methods.contains("generateContent") && name.contains("flash")) {
                    return name;
                }
            }
        }
        return "models/gemini-1.5-flash";
    }

    private String extractTextFromResponse(Map<String, Object> responseMap) {
        try {
            List<Map<String, Object>> candidates = objectMapper.convertValue(responseMap.get("candidates"), new TypeReference<List<Map<String, Object>>>() {});
            Map<String, Object> firstCandidate = candidates.get(0);
            Map<String, Object> content = objectMapper.convertValue(firstCandidate.get("content"), new TypeReference<Map<String, Object>>() {});
            List<Map<String, Object>> parts = objectMapper.convertValue(content.get("parts"), new TypeReference<List<Map<String, Object>>>() {});
            
            return parts.get(0).get("text").toString();
        } catch (Exception e) {
            return "분석 결과를 추출하는 중 오류가 발생했습니다.";
        }
    }
}