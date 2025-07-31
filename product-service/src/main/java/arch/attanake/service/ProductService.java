package arch.attanake.service;

import arch.attanake.dto.ProductRequestDto;
import arch.attanake.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigDecimal;

public interface ProductService {
    Page<ProductResponseDto> searchProducts(
            String name,
            String category,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );

    ProductResponseDto getProductById(String id);
    ProductResponseDto createProduct(ProductRequestDto request);
    ProductResponseDto updateProduct(String id, ProductRequestDto request);
    void deleteProduct(String id);
    void setProductPublished(String id, boolean published);
}
