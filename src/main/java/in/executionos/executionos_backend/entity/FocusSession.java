package in.executionos.executionos_backend.entity;

import in.executionos.executionos_backend.enums.SessionStatus;
import in.executionos.executionos_backend.enums.SessionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class FocusSession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @ManyToOne
    @JoinColumn(name = "activity_type_id", nullable = false)
    private ActivityType activityType;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer durationMinutes;

    @Column
    private LocalDateTime pausedAt;

    @Column
    private Long pausedDurationMinutes = 0L;

    private LocalDateTime expectedEndTime;

    @Enumerated(EnumType.STRING)
    private SessionType sessionType;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;
}