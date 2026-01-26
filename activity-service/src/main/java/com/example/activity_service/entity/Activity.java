package com.example.activity_service.entity;

import jakarta.persistence.*; 
import lombok.*;           
import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;      
    private String type;         // RUNNING, CYCLING, SWIMMING 등
    private Integer duration;    // 운동 시간 (분)
    
    private LocalDateTime timestamp;
}