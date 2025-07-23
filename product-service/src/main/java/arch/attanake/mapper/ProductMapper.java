package arch.attanake.mapper;

import arch.attanake.dto.ProductRequest;
import arch.attanake.dto.ProductResponse;
import arch.attanake.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ProductEntity toEntity(ProductRequest request);

    ProductResponse toResponse(ProductEntity product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromRequest(ProductRequest request, @MappingTarget ProductEntity product);
}
