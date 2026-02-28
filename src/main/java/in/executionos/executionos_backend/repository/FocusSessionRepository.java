package in.executionos.executionos_backend.repository;

import in.executionos.executionos_backend.entity.FocusSession;
import in.executionos.executionos_backend.entity.User;
import in.executionos.executionos_backend.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FocusSessionRepository extends JpaRepository<FocusSession, Long> {

    Optional<FocusSession> findByUserAndStatus(User user, SessionStatus status);

    List<FocusSession> findByUser(User user);
}