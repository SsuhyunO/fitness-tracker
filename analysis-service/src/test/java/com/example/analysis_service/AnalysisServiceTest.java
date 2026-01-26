package com.example.analysis_service;

import com.example.analysis_service.client.ActivityClient;
import com.example.analysis_service.dto.AnalysisRequestDto;
import com.example.analysis_service.dto.UserProfileResponse;
import com.example.analysis_service.entity.Analysis;
import com.example.analysis_service.repository.AnalysisRepository;
import com.example.analysis_service.service.AnalysisService;
import com.example.analysis_service.service.GeminiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@Transactional
public class AnalysisServiceTest {

    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private AnalysisRepository analysisRepository;

    @MockBean
    private ActivityClient activityClient;

    @MockBean
    private GeminiService geminiService;

    @Test
    @DisplayName("성공: 고유 PK와 별개로 activityId를 포함한 분석 결과가 올바르게 저장되어야 한다")
    void analyzeAndSave_Success() {
        // 1. Given
        String userId = "suhyeon_user";
        Long activityId = 100L;
        String workoutType = "SWIMMING";
        Integer duration = 45;

        // 가짜 유저 정보 설정
        UserProfileResponse mockProfile = new UserProfileResponse(25, 175.0, 70.0);
        Mockito.when(activityClient.getUserProfile(userId))
               .thenReturn(mockProfile);

        // 가짜 AI 피드백 설정
        String mockFeedback = "수영 45분은 아주 훌륭한 유산소 운동입니다!";
        Mockito.when(geminiService.getAnalysis(anyString(), anyString(), anyInt(), anyInt(), anyDouble(), anyDouble()))
               .thenReturn(mockFeedback);

        // 빌더 패턴을 사용하여 DTO 생성 (AllArgsConstructor 추가됨)
        AnalysisRequestDto requestDto = AnalysisRequestDto.builder()
                .userId(userId)
                .activityId(activityId)
                .workoutType(workoutType)
                .durationMinutes(duration)
                .build();

        // 2. When
        Analysis savedAnalysis = analysisService.analyzeAndSave(requestDto);

        // 3. Then
        assertThat(savedAnalysis.getId()).isNotNull(); // 자동 생성된 PK 확인
        assertThat(savedAnalysis.getActivityId()).isEqualTo(activityId); // 비즈니스 ID 확인
        assertThat(savedAnalysis.getFeedback()).isEqualTo(mockFeedback);

        // 레포지토리에 새로 만든 findByActivityId 메서드로 검증
        Analysis found = analysisRepository.findByActivityId(activityId)
                .orElseThrow(() -> new RuntimeException("저장된 분석 결과가 없습니다."));
        
        assertThat(found.getUserId()).isEqualTo(userId);
        assertThat(found.getFeedback()).contains("훌륭한 유산소 운동");

        System.out.println("테스트 성공! 생성된 PK ID: " + savedAnalysis.getId());
        System.out.println("AI 피드백 내용: " + found.getFeedback());
    }
}