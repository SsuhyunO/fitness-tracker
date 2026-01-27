package com.example.activity_service.controller;

import com.example.activity_service.entity.Activity;
import com.example.activity_service.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    // 저장 API: userId를 경로변수로 받음
    @PostMapping("/{userId}")
    public Activity create(@PathVariable String userId, @RequestBody Activity activity) {
        return activityService.createActivity(userId, activity);
    }

    // 유연한 검색 API: ?type=RUNNING 처럼 쿼리 파라미터 사용 가능
    @GetMapping("/search")
    public List<Activity> search(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String type) {
        return activityService.searchActivities(userId, type);
    }
}