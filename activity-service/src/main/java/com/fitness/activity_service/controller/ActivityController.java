package com.fitness.activity_service.controller;

import com.fitness.activity_service.model.Activity;
import com.fitness.activity_service.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor // Repository를 자동으로 연결(주입)해줍니다.
public class ActivityController {

    private final ActivityRepository repository;

    // 1. 모든 운동 기록 조회하기 (GET http://localhost:8081/activities)
    @GetMapping
    public List<Activity> getAllActivities() {
        return repository.findAll();
    }

    // 2. 운동 기록 저장하기 (POST http://localhost:8081/activities)
    @PostMapping
    public Activity createActivity(@RequestBody Activity activity) {
        // 현재 시간을 자동으로 설정해줍니다.
        activity.setTimestamp(LocalDateTime.now());
        return repository.save(activity);
    }

    // 3. 특정 사용자의 기록만 조회하기 (GET http://localhost:8081/activities/user/suhyu)
    @GetMapping("/user/{userId}")
    public List<Activity> getActivitiesByUserId(@PathVariable String userId) {
        return repository.findByUserId(userId);
    }
}