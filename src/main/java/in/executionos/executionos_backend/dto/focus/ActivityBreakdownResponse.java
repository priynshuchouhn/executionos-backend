package in.executionos.executionos_backend.dto.focus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActivityBreakdownResponse {
    private String activity;
    private Long totalMinutes;
}