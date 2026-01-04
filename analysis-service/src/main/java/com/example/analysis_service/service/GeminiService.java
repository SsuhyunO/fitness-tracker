package com.example.analysis_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${google.ai.api-key}")
    private String apiKey;

    private final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta";

    public String getAnalysis(String workoutData) {
        RestTemplate restTemplate = new RestTemplate();
        String trimmedKey = apiKey.trim();

        try {
            // 1. 사용 가능한 모델 목록 가져오기
            String listModelsUrl = BASE_URL + "/models?key=" + trimmedKey;
            Map<String, Object> modelListResponse = restTemplate.getForObject(listModelsUrl, Map.class);
            
            // 2. 분석에 적합한 모델 하나 선택 (예: gemini-1.5-flash 등)
            String selectedModel = "models/gemini-1.5-flash"; // 기본값
            if (modelListResponse != null && modelListResponse.containsKey("models")) {
                List<Map<String, Object>> models = (List<Map<String, Object>>) modelListResponse.get("models");
                for (Map<String, Object> model : models) {
                    String name = (String) model.get("name");
                    List<String> methods = (List<String>) model.get("supportedGenerationMethods");
                    // 콘텐츠 생성이 가능하고 'flash'가 포함된 가벼운 모델 우선 선택
                    if (methods.contains("generateContent") && name.contains("flash")) {
                        selectedModel = name;
                        break;
                    }
                }
            }

            // 3. 선택된 모델로 분석 요청
            String generateUrl = BASE_URL + "/" + selectedModel + ":generateContent?key=" + trimmedKey;
            System.out.println("선택된 모델: " + selectedModel);

            String prompt = "당신은 전문 피트니스 트레이너이자 영양사입니다.\n" +
                    "아래 제공된 사용자의 최근 운동 기록(JSON 데이터)을 바탕으로 분석을 진행해 주세요.\n\n" +
                    "운동 데이터: " + workoutData + "\n" +
                    "분석 지침:\n" +
                    "1. 사용자가 수행한 운동의 종류, 거리, 시간 등을 정확히 파악하세요.\n" +
                    "2. 운동 강도가 적절한지 평가하고, 개선할 점을 한 문장으로 조언하세요.\n" +
                    "3. 운동 후 섭취하면 좋은 영양소나 간단한 식단을 포함해 주세요.\n" +
                    "4. 반드시 한국어로 답변하고, 친절하면서도 전문적인 어조를 유지하세요.\n" +
                    "5. 전체 답변은 최대 두 문장을 넘기지 마세요.";

            Map<String, Object> body = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
            );

            Map<String, Object> response = restTemplate.postForObject(generateUrl, body, Map.class);
            
            // 4. 답변 추출
            List candidates = (List) response.get("candidates");
            Map firstCandidate = (Map) candidates.get(0);
            Map content = (Map) firstCandidate.get("content");
            List parts = (List) content.get("parts");
            Map firstPart = (Map) parts.get(0);
            
            return firstPart.get("text").toString();

        } catch (Exception e) {
            System.err.println("에러 상세: " + e.getMessage());
            return "분석 시도 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
        }
    }
}