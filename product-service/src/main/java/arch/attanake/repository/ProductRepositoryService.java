package arch.attanake.repository;

import arch.attanake.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;

public interface ProductRepositoryService {
    Page<ProductEntity> search(
            String name,
            String category,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );
}
