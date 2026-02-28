package in.executionos.executionos_backend.dto.focus;

import in.executionos.executionos_backend.enums.SessionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartSessionRequest {
    private Long activityTypeId;
    private SessionType sessionType; // CUSTOM or POMODORO
}
