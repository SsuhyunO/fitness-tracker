package com.example.analysis_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {
    private Integer age;
    private Double height;
    private Double weight;
}