package com.example.analysis_service.client;

import com.example.analysis_service.dto.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "activity-service")
public interface ActivityClient {

    @GetMapping("/api/activity/user/search?userId={userId}")
    List<Object> getActivitiesByUserId(@PathVariable("userId") String userId);

    @GetMapping("/api/user-profile/{userId}") 
    UserProfileResponse getUserProfile(@PathVariable("userId") String userId);
}
