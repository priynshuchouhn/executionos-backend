package in.executionos.executionos_backend.service;

import in.executionos.executionos_backend.dto.focus.ActivityBreakdownResponse;
import in.executionos.executionos_backend.dto.focus.LeaderboardResponse;
import in.executionos.executionos_backend.dto.focus.StartSessionRequest;
import in.executionos.executionos_backend.entity.ActivityType;
import in.executionos.executionos_backend.entity.FocusSession;
import in.executionos.executionos_backend.entity.User;
import in.executionos.executionos_backend.entity.WorkspaceMember;
import in.executionos.executionos_backend.enums.SessionStatus;
import in.executionos.executionos_backend.enums.SessionType;
import in.executionos.executionos_backend.repository.ActivityTypeRepository;
import in.executionos.executionos_backend.repository.FocusSessionRepository;
import in.executionos.executionos_backend.repository.WorkspaceMemberRepository;
import in.executionos.executionos_backend.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FocusSessionService {

    private final FocusSessionRepository focusSessionRepository;
    private final ActivityTypeRepository activityTypeRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    public FocusSession startSession(StartSessionRequest request) {

        User user = SecurityUtil.getCurrentUser();

        // Check if already running session
        focusSessionRepository
                .findByUserAndStatus(user, SessionStatus.RUNNING)
                .ifPresent(session -> {
                    throw new RuntimeException("Session already running");
                });

        WorkspaceMember member = workspaceMemberRepository
                .findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        ActivityType activity = activityTypeRepository
                .findById(request.getActivityTypeId())
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        FocusSession session = new FocusSession();
        session.setUser(user);
        session.setWorkspace(member.getWorkspace());
        session.setActivityType(activity);
        session.setStartTime(LocalDateTime.now());
        session.setSessionType(request.getSessionType());
        session.setStatus(SessionStatus.RUNNING);
        if (request.getSessionType() == SessionType.POMODORO) {
            session.setExpectedEndTime(
                    LocalDateTime.now().plusMinutes(25)
            );
        }

        return focusSessionRepository.save(session);
    }

    public FocusSession completeSession(Long sessionId) {

        User user = SecurityUtil.getCurrentUser();

        FocusSession session = focusSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        session.setEndTime(LocalDateTime.now());
        session.setStatus(SessionStatus.COMPLETED);

        long minutes = Duration.between(
                session.getStartTime(),
                session.getEndTime()
        ).toMinutes();

        long finalDuration = minutes - session.getPausedDurationMinutes();

        session.setDurationMinutes((int) finalDuration);

        return focusSessionRepository.save(session);
    }

    public FocusSession getActiveSession() {

        User user = SecurityUtil.getCurrentUser();

        return focusSessionRepository
                .findByUserAndStatus(user, SessionStatus.RUNNING)
                .orElse(null);
    }

    public List<FocusSession> getMySessions() {

        User user = SecurityUtil.getCurrentUser();

        return focusSessionRepository.findByUser(user);
    }

    public FocusSession pauseSession(Long sessionId) {

        User user = SecurityUtil.getCurrentUser();

        FocusSession session = focusSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        if (session.getStatus() != SessionStatus.RUNNING) {
            throw new RuntimeException("Session not running");
        }

        session.setStatus(SessionStatus.PAUSED);
        session.setPausedAt(LocalDateTime.now());

        return focusSessionRepository.save(session);
    }

    public FocusSession resumeSession(Long sessionId) {

        User user = SecurityUtil.getCurrentUser();

        FocusSession session = focusSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        if (session.getStatus() != SessionStatus.PAUSED) {
            throw new RuntimeException("Session not paused");
        }

        long pausedMinutes = Duration.between(
                session.getPausedAt(),
                LocalDateTime.now()
        ).toMinutes();

        session.setPausedDurationMinutes(
                session.getPausedDurationMinutes() + pausedMinutes
        );

        session.setPausedAt(null);
        session.setStatus(SessionStatus.RUNNING);

        return focusSessionRepository.save(session);
    }

    public List<ActivityBreakdownResponse> getActivityAnalytics(LocalDateTime start, LocalDateTime end) {

        User user = SecurityUtil.getCurrentUser();

        WorkspaceMember member = workspaceMemberRepository
                .findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        List<Object[]> results = focusSessionRepository
                .getActivityBreakdown(
                        user.getId(),
                        member.getWorkspace().getId(),
                        start,
                        end
                );

        return results.stream()
                .map(r -> new ActivityBreakdownResponse(
                        (String) r[0],
                        (Long) r[1]
                ))
                .toList();
    }

    public List<LeaderboardResponse> getWorkspaceLeaderboard(LocalDateTime start, LocalDateTime end) {

        User user = SecurityUtil.getCurrentUser();

        WorkspaceMember member = workspaceMemberRepository
                .findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        List<Object[]> results = focusSessionRepository
                .getWorkspaceLeaderboard(
                        member.getWorkspace().getId(),
                        start,
                        end
                );

        return results.stream()
                .map(r -> new LeaderboardResponse(
                        (String) r[0],
                        (Long) r[1]
                ))
                .toList();
    }
}
