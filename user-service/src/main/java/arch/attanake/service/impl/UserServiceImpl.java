package arch.attanake.service.impl;

import arch.attanake.dto.*;
import arch.attanake.entity.RoleEntity;
import arch.attanake.entity.RoleType;
import arch.attanake.entity.UserEntity;
import arch.attanake.exceprion.UserNotFoundException;
import arch.attanake.mapper.UserMapper;
import arch.attanake.repository.RoleRepository;
import arch.attanake.repository.UserRepository;
import arch.attanake.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        UserEntity user = userMapper.fromCreateRequest(request);

        Set<RoleEntity> roles = request.getRoles().stream()
                .map(roleName -> {
                    try {
                        RoleType roleType = RoleType.valueOf(roleName);
                        return roleRepository.findByName(roleType)
                                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Invalid role name: " + roleName);
                    }
                })
                .collect(Collectors.toSet());

        if (roles.isEmpty()) {
            roles.add(roleRepository.findByName(RoleType.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Default role ROLE_USER not found")));
        }

        user.setRoles(roles);
        user.setEmail(request.getEmail());

        UserEntity savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userMapper.updateFromRequest(request, user);
        UserEntity updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUserRoles(UUID userId, Set<String> newRoles) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Set<RoleEntity> roles = newRoles.stream()
                .map(RoleType::valueOf)
                .map(roleType -> roleRepository.findByName(roleType))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        user.setRoles(roles);
        UserEntity savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional
    public void deactivateUser(UUID id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void processUserCreatedEvent(UserCreatedEvent event) {
        if (userRepository.existsById(event.userId())) {
            log.warn("User already exists: {}", event.userId());
            return;
        }

        UserEntity user = new UserEntity();
        user.setUserId(event.userId());
        user.setEmail(event.email());
        user.setFirstName(event.firstName());
        user.setLastName(event.lastName());
        user.setActive(true);

        RoleEntity defaultRole = roleRepository.findByName(RoleType.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("Default role not found"));
        user.setRoles(Set.of(defaultRole));

        userRepository.save(user);
        log.info("Created user from event: {}", event.email());
    }

    @Override
    @Transactional
    public void processUserRolesUpdatedEvent(UserRolesUpdatedEvent event) {
        UserEntity user = userRepository.findById(event.userId())
                .orElseThrow(() -> new UserNotFoundException(event.userId()));

        Set<RoleEntity> roles = event.roles().stream()
                .map(RoleType::valueOf)
                .map(roleType -> roleRepository.findByName(roleType))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        user.setRoles(roles);
        userRepository.save(user);
        log.info("Updated roles for user: {}", event.userId());
    }
}