package in.executionos.executionos_backend.service;

import in.executionos.executionos_backend.dto.auth.AuthResponse;
import in.executionos.executionos_backend.dto.auth.LoginRequest;
import in.executionos.executionos_backend.dto.auth.RegisterRequest;
import in.executionos.executionos_backend.entity.User;
import in.executionos.executionos_backend.entity.Workspace;
import in.executionos.executionos_backend.entity.WorkspaceMember;
import in.executionos.executionos_backend.enums.Role;
import in.executionos.executionos_backend.repository.UserRepository;
import in.executionos.executionos_backend.repository.WorkspaceMemberRepository;
import in.executionos.executionos_backend.repository.WorkspaceRepository;
import in.executionos.executionos_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final ActivityService activityService;

    public AuthResponse register(RegisterRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.OWNER);

        userRepository.save(user);

        Workspace workspace = new Workspace();
        workspace.setName(user.getName() + "'s Workspace");

        workspaceRepository.save(workspace);

        activityService.createDefaultActivities(workspace);

        WorkspaceMember member = new WorkspaceMember();
        member.setUser(user);
        member.setWorkspace(workspace);
        member.setRole(Role.OWNER);

        workspaceMemberRepository.save(member);

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return new AuthResponse(accessToken, refreshToken);
    }
}
