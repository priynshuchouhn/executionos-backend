package in.executionos.executionos_backend.service;

import in.executionos.executionos_backend.entity.User;
import in.executionos.executionos_backend.entity.WorkspaceMember;
import in.executionos.executionos_backend.enums.Role;
import in.executionos.executionos_backend.repository.WorkspaceMemberRepository;
import in.executionos.executionos_backend.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final WorkspaceMemberRepository workspaceMemberRepository;

    public void requireOwnerOrAdmin(Long workspaceId) {

        User user = SecurityUtil.getCurrentUser();

        WorkspaceMember member =
                workspaceMemberRepository
                        .findByUserIdAndWorkspaceId(user.getId(), workspaceId)
                        .orElseThrow(() -> new RuntimeException("Not member"));

        if (member.getRole() == Role.MEMBER) {
            throw new RuntimeException("Access denied");
        }
    }
}
