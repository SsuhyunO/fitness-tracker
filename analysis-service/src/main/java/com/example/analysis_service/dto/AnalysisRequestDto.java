package com.example.analysis_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor 
@Builder            
public class AnalysisRequestDto {
    private String userId;
    private Long activityId;
    private String workoutType;      
    private Integer durationMinutes; 
}