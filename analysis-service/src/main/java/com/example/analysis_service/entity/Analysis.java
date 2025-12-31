package com.example.analysis_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Analysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Column(columnDefinition = "TEXT") // 답변이 길 수 있으니 TEXT 타입으로 설정
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();
}