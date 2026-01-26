package com.example.activity_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.activity_service.entity.Activity;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    
    List<Activity> findByUserId(String userId);
}