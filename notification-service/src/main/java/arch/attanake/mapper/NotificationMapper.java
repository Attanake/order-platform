package arch.attanake.mapper;

import arch.attanake.dto.NotificationDto;
import arch.attanake.dto.NotificationResponseDto;
import arch.attanake.entity.NotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface NotificationMapper {

    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    NotificationDto toDto(NotificationEntity entity);

    @Mapping(target = "details", expression = "java(entity.getStatus().getDisplayName() + \" notification for order \" + entity.getOrderId())")
    NotificationResponseDto toResponseDto(NotificationEntity entity);

    NotificationEntity toEntity(NotificationDto dto);
}