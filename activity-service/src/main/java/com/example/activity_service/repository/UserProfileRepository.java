package com.example.activity_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.activity_service.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
}