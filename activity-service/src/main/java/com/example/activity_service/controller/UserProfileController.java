package com.example.activity_service.controller;

import com.example.activity_service.entity.UserProfile;
import com.example.activity_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileRepository userProfileRepository;

    @PostMapping
    public UserProfile saveProfile(@RequestBody UserProfile profile) {
        return userProfileRepository.save(profile);
    }

    @GetMapping("/{userId}")
    public UserProfile getProfile(@PathVariable String userId) {
        return userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}