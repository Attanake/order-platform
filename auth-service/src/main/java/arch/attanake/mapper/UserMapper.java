package arch.attanake.mapper;

import arch.attanake.dto.UserDto;
import arch.attanake.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDto toDto(UserEntity user);
    UserEntity toEntity(UserDto userDto);
}
