package arch.attanake.service;

import arch.attanake.dto.ProductRequest;
import arch.attanake.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductService {
    Page<ProductResponse> searchProducts(
            String name,
            String category,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );

    ProductResponse getProductById(String id);
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(String id, ProductRequest request);
    void deleteProduct(String id);
    void setProductPublished(String id, boolean published);
}
