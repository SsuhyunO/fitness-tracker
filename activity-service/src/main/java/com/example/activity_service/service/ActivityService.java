package com.example.activity_service.service;

import com.example.activity_service.entity.Activity;
import com.example.activity_service.repository.ActivityRepository;
import com.example.activity_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserProfileRepository userProfileRepository;

    @Transactional
    public Activity createActivity(String userId, Activity activity) {
        // 1. 유효성 검증
        if (activity.getDuration() == null || activity.getDuration() <= 0) {
            throw new IllegalArgumentException("운동 시간은 0보다 커야 합니다.");
        }

        // 2. 유저 프로필 존재 여부만 확인 
        if (!userProfileRepository.existsById(userId)) {
            throw new IllegalArgumentException("유저 프로필이 없습니다. ID: " + userId);
        }

        // 3. 기본 정보 설정 
        activity.setUserId(userId);
        activity.setTimestamp(LocalDateTime.now());
        
        // 4. 저장
        return activityRepository.save(activity);
    }

    @Transactional(readOnly = true)
    public List<Activity> searchActivities(String userId, String type) {
        Activity probe = Activity.builder()
                .userId(userId)
                .type(type)
                .build();

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase();

        return activityRepository.findAll(Example.of(probe, matcher));
    }
}