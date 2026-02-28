package in.executionos.executionos_backend.service;

import in.executionos.executionos_backend.entity.ActivityType;
import in.executionos.executionos_backend.entity.User;
import in.executionos.executionos_backend.entity.Workspace;
import in.executionos.executionos_backend.entity.WorkspaceMember;
import in.executionos.executionos_backend.repository.ActivityTypeRepository;
import in.executionos.executionos_backend.repository.WorkspaceMemberRepository;
import in.executionos.executionos_backend.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityTypeRepository activityTypeRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final AuthorizationService authorizationService;
    private static final List<String> DEFAULT_ACTIVITIES = List.of(
            "Reading",
            "Coding",
            "Research",
            "Writing",
            "Solving"
    );


    public ActivityType createActivity(String name) {

        User user = SecurityUtil.getCurrentUser();

        WorkspaceMember member = workspaceMemberRepository
                .findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        Workspace workspace = member.getWorkspace();
        authorizationService.requireOwnerOrAdmin(workspace.getId());


        if (activityTypeRepository.existsByNameAndWorkspaceAndDeletedFalse(name, workspace)) {
            throw new RuntimeException("Activity already exists");
        }

        ActivityType activity = new ActivityType();
        activity.setName(name);
        activity.setWorkspace(workspace);
        activity.setSystemDefault(false);

        return activityTypeRepository.save(activity);
    }

    public List<ActivityType> getMyActivities() {

        User user = SecurityUtil.getCurrentUser();

        WorkspaceMember member = workspaceMemberRepository
                .findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        return activityTypeRepository
                .findByWorkspaceAndDeletedFalse(member.getWorkspace());
    }

    public void createDefaultActivities(Workspace workspace) {

        for (String activityName : DEFAULT_ACTIVITIES) {

            boolean exists = activityTypeRepository
                    .existsByNameAndWorkspaceAndDeletedFalse(activityName, workspace);

            if (!exists) {
                ActivityType activity = new ActivityType();
                activity.setName(activityName);
                activity.setWorkspace(workspace);
                activity.setSystemDefault(true);
                activityTypeRepository.save(activity);
            }
        }
    }

}