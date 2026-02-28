package in.executionos.executionos_backend.scheduler;

import in.executionos.executionos_backend.entity.FocusSession;
import in.executionos.executionos_backend.enums.SessionStatus;
import in.executionos.executionos_backend.enums.SessionType;
import in.executionos.executionos_backend.repository.FocusSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FocusSessionScheduler {

    private final FocusSessionRepository focusSessionRepository;

    @Scheduled(fixedRate = 60000)
    public void autoCompletePomodoroSessions() {

        List<FocusSession> sessions =
                focusSessionRepository.findAllByStatus(SessionStatus.RUNNING);

        for (FocusSession session : sessions) {

            if (session.getSessionType() == SessionType.POMODORO &&
                    session.getExpectedEndTime() != null &&
                    session.getExpectedEndTime().isBefore(LocalDateTime.now())) {

                session.setEndTime(LocalDateTime.now());
                session.setStatus(SessionStatus.COMPLETED);

                long minutes = Duration.between(
                        session.getStartTime(),
                        session.getEndTime()
                ).toMinutes();

                session.setDurationMinutes((int) minutes);

                focusSessionRepository.save(session);
            }
        }
    }
}