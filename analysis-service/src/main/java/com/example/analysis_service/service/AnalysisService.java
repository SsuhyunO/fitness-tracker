package com.example.analysis_service.service;

import com.example.analysis_service.client.ActivityClient;
import com.example.analysis_service.dto.AnalysisRequestDto;
import com.example.analysis_service.dto.UserProfileResponse;
import com.example.analysis_service.entity.Analysis;
import com.example.analysis_service.repository.AnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final AnalysisRepository analysisRepository;
    private final GeminiService geminiService;
    private final ActivityClient activityClient;

    @Transactional
    public Analysis analyzeAndSave(AnalysisRequestDto dto) {
        System.out.println("Calling Activity-Service for userId: " + dto.getUserId());

        // 1. 유효성 검증 
        if (dto.getWorkoutType() == null || dto.getWorkoutType().trim().isEmpty()) {
            throw new IllegalArgumentException("운동 종류를 입력해주세요.");
        }
        if (dto.getDurationMinutes() == null || dto.getDurationMinutes() <= 0) {
            throw new IllegalArgumentException("운동 시간은 0보다 커야 합니다.");
        }

        // 2. ActivityClient를 사용하여 유저 신체 정보 가져오기
        UserProfileResponse profile = activityClient.getUserProfile(dto.getUserId());
        
        if (profile == null) {
            throw new RuntimeException("사용자의 신체 정보를 불러올 수 없습니다.");
        }

        // 3. GeminiService 호출 (칼로리 파라미터를 빼고 운동 종류와 시간을 전달)
        // 주의: GeminiService.java의 getAnalysis 메서드 파라미터 구성도 아래와 같이 맞춰야 합니다.
        String feedback = geminiService.getAnalysis(
            dto.getUserId(), 
            dto.getWorkoutType(), 
            dto.getDurationMinutes(),
            profile.getAge(), 
            profile.getHeight(), 
            profile.getWeight()
        );

        // 4. 분석 결과(Entity) 생성 및 저장
        // Entity 클래스에도 workoutType과 durationMinutes 필드를 추가했다고 가정합니다.
        Analysis analysis = Analysis.builder()
                .userId(dto.getUserId())
                .activityId(dto.getActivityId())
                .workoutType(dto.getWorkoutType())
                .durationMinutes(dto.getDurationMinutes())
                .feedback(feedback)
                .build();

        return analysisRepository.save(analysis);
    }
}