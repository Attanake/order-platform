package arch.attanake.mapper;

import arch.attanake.dto.CreateUserRequest;
import arch.attanake.dto.UpdateUserRequest;
import arch.attanake.dto.UserResponse;
import arch.attanake.entity.RoleEntity;
import arch.attanake.entity.UserEntity;
import org.mapstruct.*;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "roles", ignore = true)
    UserEntity fromCreateRequest(CreateUserRequest request);

    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    UserResponse toResponse(UserEntity user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(UpdateUserRequest request, @MappingTarget UserEntity user);

    default Set<String> mapRoles(Set<RoleEntity> roles) {
        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }
}