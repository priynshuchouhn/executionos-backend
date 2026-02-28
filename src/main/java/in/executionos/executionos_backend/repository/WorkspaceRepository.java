package in.executionos.executionos_backend.repository;

import in.executionos.executionos_backend.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
}
