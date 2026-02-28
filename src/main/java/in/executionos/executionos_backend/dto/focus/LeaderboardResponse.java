package in.executionos.executionos_backend.dto.focus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LeaderboardResponse {
    private String user;
    private Long totalMinutes;
}
