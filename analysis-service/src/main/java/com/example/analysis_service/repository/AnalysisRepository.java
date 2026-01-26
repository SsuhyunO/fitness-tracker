package com.example.analysis_service.repository;

import java.util.List;
import java.util.Optional;

import com.example.analysis_service.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
    List<Analysis> findByUserIdOrderByCreatedAtDesc(String userId);
    Optional<Analysis> findByActivityId(Long activityId);
}