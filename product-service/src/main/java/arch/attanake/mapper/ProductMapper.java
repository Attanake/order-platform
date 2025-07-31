package arch.attanake.mapper;

import arch.attanake.dto.ProductRequestDto;
import arch.attanake.dto.ProductResponseDto;
import arch.attanake.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "published", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ProductEntity toEntity(ProductRequestDto request);

    ProductResponseDto toResponse(ProductEntity product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromRequest(ProductRequestDto request, @MappingTarget ProductEntity product);

    ProductResponseDto toDto(ProductEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "published", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ProductRequestDto dto, @MappingTarget ProductEntity entity);
}
