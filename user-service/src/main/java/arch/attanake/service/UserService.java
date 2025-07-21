package arch.attanake.service;

import arch.attanake.dto.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(UUID id);
    UserResponse createUser(CreateUserRequest request);
    UserResponse updateUser(UUID id, UpdateUserRequest request);
    UserResponse updateUserRoles(UUID userId, Set<String> newRoles);
    void processUserCreatedEvent(UserCreatedEvent event);
    void processUserRolesUpdatedEvent(UserRolesUpdatedEvent event);
    void deactivateUser(UUID id);
}
