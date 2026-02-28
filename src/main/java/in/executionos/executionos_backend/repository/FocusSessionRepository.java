package in.executionos.executionos_backend.repository;

import in.executionos.executionos_backend.entity.FocusSession;
import in.executionos.executionos_backend.entity.User;
import in.executionos.executionos_backend.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FocusSessionRepository extends JpaRepository<FocusSession, Long> {

    Optional<FocusSession> findByUserAndStatus(User user, SessionStatus status);

    List<FocusSession> findByUser(User user);

    @Query("""
    SELECT fs.activityType.name, SUM(fs.durationMinutes)
    FROM FocusSession fs
    WHERE fs.user.id = :userId
      AND fs.workspace.id = :workspaceId
      AND fs.status = 'COMPLETED'
      AND fs.startTime BETWEEN :start AND :end
    GROUP BY fs.activityType.name
    """)
    List<Object[]> getActivityBreakdown(
            Long userId,
            Long workspaceId,
            LocalDateTime start,
            LocalDateTime end
    );


    @Query("""
    SELECT fs.user.name, SUM(fs.durationMinutes)
    FROM FocusSession fs
    WHERE fs.workspace.id = :workspaceId
      AND fs.status = 'COMPLETED'
      AND fs.startTime BETWEEN :start AND :end
    GROUP BY fs.user.name
    ORDER BY SUM(fs.durationMinutes) DESC
    """)
    List<Object[]> getWorkspaceLeaderboard(
            Long workspaceId,
            LocalDateTime start,
            LocalDateTime end
    );
}