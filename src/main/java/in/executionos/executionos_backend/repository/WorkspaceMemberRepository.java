package in.executionos.executionos_backend.repository;

import in.executionos.executionos_backend.entity.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Long> {
    Optional<WorkspaceMember> findByUserId(Long userId);

    Optional<WorkspaceMember> findByUserIdAndWorkspaceId(Long id, Long workspaceId);
}
