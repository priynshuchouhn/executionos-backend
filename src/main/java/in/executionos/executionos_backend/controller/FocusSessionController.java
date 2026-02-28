package in.executionos.executionos_backend.controller;

import in.executionos.executionos_backend.dto.focus.StartSessionRequest;
import in.executionos.executionos_backend.service.FocusSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/focus")
@RequiredArgsConstructor
public class FocusSessionController {

    private final FocusSessionService focusSessionService;

    @PostMapping("/start")
    public ResponseEntity<?> start(@RequestBody StartSessionRequest request) {
        return ResponseEntity.ok(
                focusSessionService.startSession(request)
        );
    }

    @PostMapping("/complete/{id}")
    public ResponseEntity<?> complete(@PathVariable Long id) {
        return ResponseEntity.ok(
                focusSessionService.completeSession(id)
        );
    }

    @GetMapping("/active")
    public ResponseEntity<?> active() {
        return ResponseEntity.ok(
                focusSessionService.getActiveSession()
        );
    }

    @GetMapping("/history")
    public ResponseEntity<?> history() {
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