package in.executionos.executionos_backend.repository;

import in.executionos.executionos_backend.entity.ActivityType;
import in.executionos.executionos_backend.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityTypeRepository extends JpaRepository<ActivityType, Long> {

    // Get all activities for a workspace
    List<ActivityType> findByWorkspaceAndDeletedFalse(Workspace workspace);

    // Find specific activity in workspace
    Optional<ActivityType> findByIdAndWorkspaceAndDeletedFalse(Long id, Workspace workspace);

    // Check duplicate activity name inside workspace
    boolean existsByNameAndWorkspaceAndDeletedFalse(String name, Workspace workspace);
}