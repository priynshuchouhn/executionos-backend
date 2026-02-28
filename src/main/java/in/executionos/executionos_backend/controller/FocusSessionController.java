package in.executionos.executionos_backend.controller;

import in.executionos.executionos_backend.dto.focus.StartSessionRequest;
import in.executionos.executionos_backend.entity.FocusSession;
import in.executionos.executionos_backend.service.FocusSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/focus")
@RequiredArgsConstructor
public class FocusSessionController {

    private final FocusSessionService focusSessionService;

    @PostMapping("/start")
    public ResponseEntity<FocusSession> start(@RequestBody StartSessionRequest request) {
        return ResponseEntity.ok(
                focusSessionService.startSession(request)
        );
    }

    @PostMapping("/pause")
    public ResponseEntity<FocusSession> pause(@RequestBody Long sessionID) {
        return ResponseEntity.ok(
                focusSessionService.pauseSession(sessionID)
        );
    }

    @PostMapping("/resume")
    public ResponseEntity<FocusSession> resume(@RequestBody Long sessionID) {
        return ResponseEntity.ok(
                focusSessionService.resumeSession(sessionID)
        );
    }

    @PostMapping("/complete/{id}")
    public ResponseEntity<FocusSession> complete(@PathVariable Long id) {
        return ResponseEntity.ok(
                focusSessionService.completeSession(id)
        );
    }

    @GetMapping("/active")
    public ResponseEntity<FocusSession> active() {
        return ResponseEntity.ok(
                focusSessionService.getActiveSession()
        );
    }

    @GetMapping("/history")
    public ResponseEntity<List<FocusSession>> history() {
        return ResponseEntity.ok(
                focusSessionService.getMySessions()
        );
    }

    @GetMapping("/analytics/activity")
    public ResponseEntity<?> activityAnalytics(@RequestParam String start, @RequestParam String end) {

        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);

        return ResponseEntity.ok(
                focusSessionService.getActivityAnalytics(startTime, endTime)
        );
    }

    @GetMapping("/analytics/leaderboard")
    public ResponseEntity<?> leaderboard(@RequestParam String start, @RequestParam String end) {

        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);

        return ResponseEntity.ok(
                focusSessionService.getWorkspaceLeaderboard(startTime, endTime)
        );
    }
}