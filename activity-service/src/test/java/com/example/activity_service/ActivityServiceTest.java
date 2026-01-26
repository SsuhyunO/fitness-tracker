package com.example.activity_service;

import com.example.activity_service.entity.Activity;
import com.example.activity_service.entity.UserProfile;
import com.example.activity_service.repository.ActivityRepository;
import com.example.activity_service.repository.UserProfileRepository;
import com.example.activity_service.service.ActivityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class ActivityServiceTest {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Test
    @DisplayName("성공: 운동 기록이 올바르게 DB에 저장되어야 한다 (칼로리는 AI가 처리하므로 검증 제외)")
    void saveActivitySuccess() {
        // 1. Given: 유저 생성
        String userId = "tester123";
        UserProfile user = UserProfile.builder()
                .userId(userId)
                .age(25)
                .weight(70.0)
                .build();
        userProfileRepository.save(user);

        // 2. When: 운동 기록 저장 (distance는 옵션이므로 필수가 아니면 제외 가능)
        Activity activity = Activity.builder()
                .type("RUNNING")
                .duration(30)
                .build();
        
        Activity saved = activityService.createActivity(userId, activity);

        // 3. Then: 데이터 저장 결과 검증
        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getType()).isEqualTo("RUNNING");
        assertThat(saved.getDuration()).isEqualTo(30);

        Activity foundInDb = activityRepository.findById(saved.getId()).orElse(null);
        assertThat(foundInDb).isNotNull();
        assertThat(foundInDb.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("실패: 유저 정보가 없는 상태에서 기록 저장 시 예외가 발생해야 한다")
    void saveActivityWithoutUserFail() {
        String userId = "none_user";
        Activity activity = Activity.builder().type("RUNNING").duration(30).build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            activityService.createActivity(userId, activity);
        });
        
        assertThat(exception.getMessage()).contains("유저 프로필이 없습니다");
    }

    @Test
    @DisplayName("실패: 운동 시간이 0분 이하이면 예외가 발생해야 한다")
    void saveActivityWithZeroDurationFail() {
        String userId = "tester123";
        Activity activity = Activity.builder().type("RUNNING").duration(0).build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            activityService.createActivity(userId, activity);
        });
        
        assertThat(exception.getMessage()).isEqualTo("운동 시간은 0보다 커야 합니다.");
    }
}