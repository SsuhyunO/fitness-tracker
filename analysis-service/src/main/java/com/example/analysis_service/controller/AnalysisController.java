package com.example.analysis_service.controller;

import com.example.analysis_service.client.ActivityClient;
import com.example.analysis_service.service.GeminiService;
import com.fasterxml.jackson.databind.ObjectMapper; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private final ActivityClient activityClient;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper; 

    public AnalysisController(ActivityClient activityClient, GeminiService geminiService) {
        this.activityClient = activityClient;
        this.geminiService = geminiService;
        this.objectMapper = new ObjectMapper(); 
    }

    @GetMapping("/user/{userId}")
    public String getAnalysis(@PathVariable String userId) {
        System.out.println("=== [Step 1] 분석 요청 수신. 대상 사용자: " + userId);

        try {
            // 1. FeignClient를 통해 8081 포트에서 운동 기록(List) 가져오기
            List<Object> activities = activityClient.getActivitiesByUserId(userId);

            // 2. 데이터가 없는 경우 처리
            if (activities == null || activities.isEmpty()) {
                System.out.println("=== [Step 2] 데이터가 비어있음");
                return userId + "님의 운동 기록을 찾을 수 없습니다. 8081 포트의 데이터를 먼저 확인해주세요.";
            }

            // 3. [핵심] List 객체를 AI가 읽을 수 있는 JSON 텍스트로 변환
            String workoutDataJson = objectMapper.writeValueAsString(activities);
            
            System.out.println("=== [Step 3] AI에게 전달될 최종 데이터: " + workoutDataJson);

            // 4. Gemini 서비스에 가공된 텍스트 전달 및 결과 반환
            return geminiService.getAnalysis(workoutDataJson);

        } catch (Exception e) {
            System.err.println("=== [Error] 분석 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return "분석 중 오류가 발생했습니다: " + e.getMessage();
        }
    }
}