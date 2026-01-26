package com.example.activity_service.controller;

import com.example.activity_service.entity.UserProfile;
import com.example.activity_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileRepository userProfileRepository;

    // 1. 유저 신체 정보 등록/수정 (POST http://localhost:8081/users)
    @PostMapping
    public UserProfile saveProfile(@RequestBody UserProfile profile) {
        return userProfileRepository.save(profile);
    }

    // 2. 등록된 정보 확인 (GET http://localhost:8081/users/userId)
    @GetMapping("/{userId}")
    public UserProfile getProfile(@PathVariable String userId) {
        return userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}