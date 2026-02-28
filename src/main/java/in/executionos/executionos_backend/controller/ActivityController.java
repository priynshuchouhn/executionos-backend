package in.executionos.executionos_backend.controller;

import in.executionos.executionos_backend.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
    public ResponseEntity<?> create(@RequestParam String name) {
        return ResponseEntity.ok(
                activityService.createActivity(name)
        );
    }

    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(
                activityService.getMyActivities()
        );
    }
}
