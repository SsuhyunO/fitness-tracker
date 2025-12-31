package com.example.analysis_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

// 8081 포트의 activity-service로 전화를 거는 리모컨입니다.
@FeignClient(name = "activity-service", url = "http://localhost:8081")
public interface ActivityClient {

    @GetMapping("/activities/user/{userId}")
    List<Object> getActivitiesByUserId(@PathVariable("userId") String userId);
}
