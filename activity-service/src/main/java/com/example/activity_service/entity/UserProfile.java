package com.example.activity_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_profiles")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    private String userId;      // Activity의 userId와 연결될 Key

    private Integer age;
    private Double weight;      
    private Double height;
    private String gender;      
}