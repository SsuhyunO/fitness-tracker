package com.fitness.activity_service.model;

import jakarta.persistence.*; // 데이터베이스 연결 도구
import lombok.*;           // 반복 코드를 줄여주는 도구
import java.time.LocalDateTime;

@Entity // 1. 이 클래스가 데이터베이스의 테이블임을 선언합니다.
@Table(name = "activities") // 2. 실제 DB에 'activities'라는 이름의 테이블을 만듭니다.
@Getter @Setter // 3. 롬복이 Getter/Setter를 자동으로 만들어줍니다.
@NoArgsConstructor // 4. 파라미터가 없는 기본 생성자를 만듭니다.
@AllArgsConstructor // 5. 모든 필드를 가진 생성자를 만듭니다.
@Builder // 6. 객체를 생성할 때 빌더 패턴을 쓸 수 있게 합니다.
public class Activity {

    @Id // 7. 이 필드가 기본키(PK)임을 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 8. 숫자를 1, 2, 3... 자동으로 늘려줍니다.
    private Long id;

    private String userId;      // 누구의 운동 기록인가?
    private String type;        // 운동 종류 (예: RUNNING, CYCLING)
    private Integer duration;   // 운동 시간 (분)
    private Double distance;    // 운동 거리 (km)
    private LocalDateTime timestamp; // 운동한 시간
}