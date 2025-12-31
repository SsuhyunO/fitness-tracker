package com.fitness.activity_service.repository;

import com.fitness.activity_service.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// JpaRepository<사용할 엔티티, ID의 타입>을 상속받습니다.
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    
    // 기본 기능(저장, 전체조회) 외에 필요한 기능을 정의합니다.
    // "특정 사용자의 데이터만 다 가져와!"라는 기능입니다.
    List<Activity> findByUserId(String userId);
}