package com.example.analysis_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Table(name = "analyses")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Analysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long activityId; 
    private String userId;      
    private String workoutType;    
    private Integer durationMinutes; 
    
    @Column(columnDefinition = "TEXT")
    private String feedback;    

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}