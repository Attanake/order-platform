package arch.attanake.mapper;

import arch.attanake.dto.NotificationTemplateDto;
import arch.attanake.entity.NotificationTemplateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NotificationTemplateMapper {

    NotificationTemplateMapper INSTANCE = Mappers.getMapper(NotificationTemplateMapper.class);

    @Mapping(target = "id", ignore = true)
    NotificationTemplateEntity toEntity(NotificationTemplateDto dto);

    NotificationTemplateDto toDto(NotificationTemplateEntity entity);

    @Named("toDtoWithoutContent")
    @Mapping(target = "content", ignore = true)
    NotificationTemplateDto toDtoWithoutContent(NotificationTemplateEntity entity);

    @Mapping(target = "version", expression = "java(entity.getVersion() + 1)")
    NotificationTemplateEntity incrementVersion(NotificationTemplateEntity entity);
}
