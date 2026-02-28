package in.executionos.executionos_backend.controller;

import in.executionos.executionos_backend.dto.focus.StartSessionRequest;
import in.executionos.executionos_backend.service.FocusSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}